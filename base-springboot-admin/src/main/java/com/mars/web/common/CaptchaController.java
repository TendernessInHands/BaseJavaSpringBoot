package com.mars.web.common;

import com.google.code.kaptcha.Producer;
import com.mars.common.constant.Constants;
import com.mars.common.core.domain.AjaxResult;
import com.mars.common.core.redis.RedisCache;
import com.mars.common.utils.sign.Base64;
import com.mars.common.utils.uuid.IdUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.FastByteArrayOutputStream;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * 验证码操作处理
 *
 * @author mars
 */
@RestController
public class CaptchaController {
    @Resource(name = "captchaProducer")
    private Producer captchaProducer;

    @Resource(name = "captchaProducerMath")
    private Producer captchaProducerMath;

    @Autowired
    private RedisCache redisCache;

    /**
     * 验证码类型
     */
    @Value("${mars.captchaType}")
    private String captchaType;

    /**
     * 生成验证码
     */
    @GetMapping("/captchaImage")
    public AjaxResult getCode(HttpServletResponse response) throws IOException {
        // 保存验证码信息
        String uuid = IdUtils.simpleUUID();
        String verifyKey = Constants.CAPTCHA_CODE_KEY + uuid;

        String capStr = null;
        String code = null;
        BufferedImage image = null;

        // 生成验证码
        String math = "math";
        String cha = "char";
        if (math.equals(captchaType)) {
            String capText = captchaProducerMath.createText();
            capStr = capText.substring(0, capText.lastIndexOf("@"));
            code = capText.substring(capText.lastIndexOf("@") + 1);
            image = captchaProducerMath.createImage(capStr);
        } else if (cha.equals(captchaType)) {
            capStr = code = captchaProducer.createText();
            image = captchaProducer.createImage(capStr);
        }

        redisCache.setCacheObject(verifyKey, code, Constants.CAPTCHA_EXPIRATION, TimeUnit.MINUTES);
        // 转换流信息写出
        FastByteArrayOutputStream os = new FastByteArrayOutputStream();
        image = imgColorContrast(image, 64);
        try {
            ImageIO.write(image, "jpg", os);
        } catch (IOException e) {
            return AjaxResult.error(e.getMessage());
        }

        AjaxResult ajax = AjaxResult.success();
        ajax.put("uuid", uuid);
        ajax.put("img", Base64.encode(os.toByteArray()));
        return ajax;
    }

    public static BufferedImage imgColorContrast(BufferedImage img, int contrast) {
        try {
            int contrastAverage = 64;
            int width = img.getWidth();
            int height = img.getHeight();
            //创建一个不带透明度的图片
            BufferedImage back = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            int pix;
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    int pixel = img.getRGB(j, i);
                    Color color = new Color(pixel);

                    if (color.getRed() < contrastAverage) {
                        pix = color.getRed() - Math.abs(contrast);
                        if (pix < 0) {
                            pix = 0;
                        }
                    } else {
                        pix = color.getRed() + Math.abs(contrast);
                        if (pix > 255) {
                            pix = 255;
                        }
                    }
                    int red = pix;
                    if (color.getGreen() < contrastAverage) {
                        pix = color.getGreen() - Math.abs(contrast);
                        if (pix < 0) {
                            pix = 0;
                        }
                    } else {
                        pix = color.getGreen() + Math.abs(contrast);
                        if (pix > 255) {
                            pix = 255;
                        }
                    }
                    int green = pix;
                    if (color.getBlue() < contrastAverage) {
                        pix = color.getBlue() - Math.abs(contrast);
                        if (pix < 0) {
                            pix = 0;
                        }
                    } else {
                        pix = color.getBlue() + Math.abs(contrast);
                        if (pix > 255) {
                            pix = 255;
                        }
                    }
                    int blue = pix;

                    color = new Color(red, green, blue);
                    int x = color.getRGB();
                    back.setRGB(j, i, x);
                }
            }
            return back;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
