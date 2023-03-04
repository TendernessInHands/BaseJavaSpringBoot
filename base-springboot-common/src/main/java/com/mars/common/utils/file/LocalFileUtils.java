package com.mars.common.utils.file;

import com.mars.common.core.redis.RedisCache;
import com.mars.common.exception.BaseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

/**
 * 文件处理工具类
 *
 * @author mars
 */
@Component
public class LocalFileUtils extends org.apache.commons.io.FileUtils {
    public static final String FILENAME_PATTERN = "[a-zA-Z0-9_\\-\\|\\.\\u4e00-\\u9fa5]+";

    @Autowired
    private RedisCache redisCache;


    /**
     * 输出指定文件的byte数组
     *
     * @param filePath 文件路径
     * @param os       输出流
     * @return
     */
    public static void writeBytes(String filePath, OutputStream os) throws IOException {
        FileInputStream fis = null;
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                throw new FileNotFoundException(filePath);
            }
            fis = new FileInputStream(file);
            byte[] b = new byte[1024];
            int length;
            while ((length = fis.read(b)) > 0) {
                os.write(b, 0, length);
            }
        } catch (IOException e) {
            throw e;
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    /**
     * 删除文件
     *
     * @param filePath 文件
     * @return
     */
    public static boolean deleteFile(String filePath) {
        boolean flag = false;
        File file = new File(filePath);
        // 路径为文件且不为空则进行删除
        if (file.isFile() && file.exists()) {
            file.delete();
            flag = true;
        }
        return flag;
    }

    /**
     * 文件名称验证
     *
     * @param filename 文件名称
     * @return true 正常 false 非法
     */
    public static boolean isValidFilename(String filename) {
        return filename.matches(FILENAME_PATTERN);
    }

    /**
     * 下载文件名重新编码
     *
     * @param request  请求对象
     * @param fileName 文件名
     * @return 编码后的文件名
     */
    public static String setFileDownloadHeader(HttpServletRequest request, String fileName)
            throws UnsupportedEncodingException {
        final String agent = request.getHeader("USER-AGENT");
        String filename = fileName;
        String msie = "MSIE";
        String firefox = "Firefox";
        String chrome = "Chrome";
        if (agent.contains(msie)) {
            // IE浏览器
            filename = URLEncoder.encode(filename, "utf-8");
            filename = filename.replace("+", " ");
        } else if (agent.contains(firefox)) {
            // 火狐浏览器
            filename = new String(fileName.getBytes(), "ISO8859-1");
        } else {
            // 其它浏览器
            filename = URLEncoder.encode(filename, "utf-8");
        }
/*        else if (agent.contains(chrome)) {
            // google浏览器
            filename = URLEncoder.encode(filename, "utf-8");
        } */
        return filename;
    }

    public static boolean getFileAllowUpload(MultipartFile file) {
        InputStream is;
        boolean flag = true;
        try {
            is = file.getInputStream();
            byte[] b = new byte[16];
            is.read(b, 0, b.length);
            String fileType = getFileType(file);
            HashMap<String, String> fileTypeMap = new HashMap<>(16);
            String[] typeArr = MimeTypeUtils.DEFAULT_ALLOWED_EXTENSION;
            if (null != typeArr && typeArr.length > 0) {
                for (String s : typeArr) {
                    fileTypeMap.put(s, s);
                }
            }
            if (null != fileType && !"".equals(fileType)) {
                if (!fileTypeMap.containsKey(fileType.toLowerCase(Locale.ROOT))) {
                    flag = false;
                }
            } else {
                flag = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 通过读取文件头部获得文件类型
     *
     * @param file
     * @return 文件类型
     * @throws BaseException
     */
    public static String getFileType(MultipartFile file) {
        Map<String, String> fileTypeMap = MimeTypeUtils.allowUploadFileContentType();
        String fileExtendName = null;
        InputStream is;
        String officeStr = "office";
        try {
            is = file.getInputStream();
            byte[] b = new byte[16];
            is.read(b, 0, b.length);
            String filetypeHex = String.valueOf(bytesToHexString(b));
            Iterator<Map.Entry<String, String>> entryIterator = fileTypeMap.entrySet().iterator();
            while (entryIterator.hasNext()) {
                Map.Entry<String, String> entry = entryIterator.next();
                String fileTypeHexValue = entry.getValue();
                if (filetypeHex.startsWith(fileTypeHexValue)) {
                    fileExtendName = entry.getKey();

                    if (officeStr.equals(fileExtendName)) {
                        fileExtendName = getOfficeFileType(is);
                    }
                    is.close();
                    break;
                }
            }
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileExtendName;
    }

    /**
     * 判断office文件的具体类型
     *
     * @param fileInputStream
     * @return office文件具体类型
     * @throws BaseException
     */
    private static String getOfficeFileType(InputStream fileInputStream) {
        String officeFileType = "doc";
        byte[] b = new byte[512];
        try {
            fileInputStream.read(b, 0, b.length);
            String filetypeHex = String.valueOf(bytesToHexString(b));
            String flagString = filetypeHex.substring(992, filetypeHex.length());
            String docCode = "eca5c";
            String xlsCode = "fdffffff09";
            String xlsCode2 = "09081000000";
            if (flagString.toLowerCase().startsWith(docCode)) {
                officeFileType = "doc";
            } else if (flagString.toLowerCase().startsWith(xlsCode)) {
                officeFileType = "xls";

            } else if (flagString.toLowerCase().startsWith(xlsCode2)) {
                officeFileType = "xls";
            } else {
                officeFileType = "ppt";
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return officeFileType;
    }

    /**
     * 获得文件头部字符串
     *
     * @param src
     * @return
     */
    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder();
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }
}
