package com.mars.common.utils.image;

import net.coobird.thumbnailator.Thumbnails;

import java.io.File;

/**
 * @author: mars
 * @date 2022/9/17
 */
public class ImageUtils {


    /**
     * 图片压缩工具类
     */
    public static void compressImage(String filePath, float proportion, float quality) {
        try {
            File file = new File(filePath);
            if (null != file) {
                Thumbnails.of(file)
                        //图片大小（长宽）压缩比例 从0-1，1表示原图
                        .scale(proportion)
                        //图片质量压缩比例 从0-1，越接近1质量越好
                        .outputQuality(quality)
                        .toFile(filePath);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
