package com.mars.web.common;

import com.mars.common.config.MarsConfig;
import com.mars.common.constant.Constants;
import com.mars.common.core.domain.AjaxResult;
import com.mars.common.utils.LocalStringUtils;
import com.mars.common.utils.file.FileUploadUtils;
import com.mars.common.utils.file.LocalFileUtils;
import com.mars.framework.config.ServerConfig;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 通用请求处理
 *
 * @author mars
 */
@RestController
@Api("通用文件请求处理")
public class CommonController {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommonController.class);

    @Autowired
    private ServerConfig serverConfig;

    /**
     * 通用下载请求
     *
     * @param fileName 文件名称
     * @param delete   是否删除
     */
    @GetMapping("common/download")
    public void fileDownload(String fileName, Boolean delete, HttpServletResponse response, HttpServletRequest request) {
        try {
            if (!LocalFileUtils.isValidFilename(fileName)) {
                throw new Exception(LocalStringUtils.format("文件名称({})非法，不允许下载。 ", fileName));
            }
            String realFileName = System.currentTimeMillis() + fileName.substring(fileName.indexOf("_") + 1);
            String filePath = MarsConfig.getDownloadPath() + fileName;

            response.setCharacterEncoding("utf-8");
            response.setContentType("multipart/form-data");
            response.setHeader("Content-Disposition",
                    "attachment;fileName=" + LocalFileUtils.setFileDownloadHeader(request, realFileName));
            LocalFileUtils.writeBytes(filePath, response.getOutputStream());
            if (delete) {
                LocalFileUtils.deleteFile(filePath);
            }
        } catch (Exception e) {
            LOGGER.error("下载文件失败", e);
        }
    }

    /**
     * 通用上传请求
     */
    @PostMapping("/common/upload")
    @ApiOperation("通用文件上传")
    public AjaxResult uploadFile(MultipartFile file) throws Exception {
        try {
            // 上传文件路径
            String filePath = MarsConfig.getUploadPath();
            // 上传并返回新文件名称
            String fileName = FileUploadUtils.upload(filePath, file);
            String url = serverConfig.getUrl() + fileName;
            AjaxResult ajax = AjaxResult.success();
            ajax.put("fileName", fileName);
            ajax.put("url", url);
            return ajax;
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 本地资源通用下载
     */
    @GetMapping("/common/download/resource")
    @ApiOperation("本地资源通用下载")
    public void resourceDownload(String filePath, HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 本地资源路径
        String localPath = MarsConfig.getProfile();
        if (LocalStringUtils.isNotEmpty(filePath)) {
            if (!filePath.contains(Constants.RESOURCE_PREFIX)) {
                filePath = Constants.RESOURCE_PREFIX + filePath;
            }
        }
        // 数据库资源地址
        String downloadPath = localPath + LocalStringUtils.substringAfter(filePath, Constants.RESOURCE_PREFIX);
        // 下载名称
        String downloadName = LocalStringUtils.substringAfterLast(downloadPath, "/");
        response.setCharacterEncoding("utf-8");
        response.setContentType("multipart/form-data");
        response.setHeader("Content-Disposition",
                "attachment;fileName=" + LocalFileUtils.setFileDownloadHeader(request, downloadName));
        LocalFileUtils.writeBytes(downloadPath, response.getOutputStream());
    }

    @GetMapping("/common/checkUrl")
    @ApiOperation("通过URL地址读取文件流")
    public boolean getFileStream(@RequestParam String url) {
        boolean flag = true;
        try {
            URL httpUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) httpUrl.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5 * 1000);
            InputStream inStream = conn.getInputStream();
            byte[] btImg = readInputStream(inStream);
            if (null != btImg && btImg.length > 0) {
                return flag;
            } else {
                return !flag;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return !flag;
    }

    /**
     * 从输入流中获取数据
     *
     * @param inStream 输入流
     * @return
     * @throws Exception
     */
    public static byte[] readInputStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        inStream.close();
        return outStream.toByteArray();
    }
}
