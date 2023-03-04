package com.mars.common.utils.file;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * 媒体类型工具类
 *
 * @author mars
 */
public class MimeTypeUtils {
    public static final String IMAGE_PNG = "image/png";

    public static final String IMAGE_JPG = "image/jpg";

    public static final String IMAGE_JPEG = "image/jpeg";

    public static final String IMAGE_BMP = "image/bmp";

    public static final String IMAGE_GIF = "image/gif";

    public static final String[] IMAGE_EXTENSION = {"bmp", "gif", "jpg", "jpeg", "png"};

    public static final String[] FLASH_EXTENSION = {"swf", "flv"};

    public static final String[] MEDIA_EXTENSION = {"swf", "flv", "mp3", "wav", "wma", "wmv", "mid", "avi", "mpg", "asf", "rm", "rmvb"};
    /**
     * 图片 word excel powerpoint   压缩文件 pdf  wps
     */
    public static final String[] DEFAULT_ALLOWED_EXTENSION = {"bmp", "gif", "jpg", "jpeg", "png", "doc", "docx", "xls", "xlsx", "ppt", "pptx", "html", "htm", "txt", "rar", "zip", "gz", "bz2", "pdf", "wps", "svg", "xml"};

    public static String getExtension(String prefix) {
        switch (prefix) {
            case IMAGE_PNG:
                return "png";
            case IMAGE_JPG:
                return "jpg";
            case IMAGE_JPEG:
                return "jpeg";
            case IMAGE_BMP:
                return "bmp";
            case IMAGE_GIF:
                return "gif";
            default:
                return "";
        }
    }

    /**
     * 允许上传的文件
     *
     * @return
     */
    public static Map<String, String> allowUploadFileContentType() {
        final Map<String, String> fileTypeMap = new HashMap<String, String>(16);
        fileTypeMap.put("jpg", "FFD8FF".toLowerCase(Locale.ROOT));
        fileTypeMap.put("png", "89504E47".toLowerCase(Locale.ROOT));
        fileTypeMap.put("gif", "47494638".toLowerCase(Locale.ROOT));
        fileTypeMap.put("tif", "49492A00".toLowerCase(Locale.ROOT));
        fileTypeMap.put("bmp", "424d228c010000000000".toLowerCase(Locale.ROOT));
        // 24位位图(bmp)
        fileTypeMap.put("bmp", "424d8240090000000000".toLowerCase(Locale.ROOT));
        // 256色位图(bmp)
        fileTypeMap.put("bmp", "424d8e1b030000000000".toLowerCase(Locale.ROOT));
        fileTypeMap.put("dwg", "41433130".toLowerCase(Locale.ROOT));
        fileTypeMap.put("html", "68746D6C3E".toLowerCase(Locale.ROOT));
        fileTypeMap.put("rtf", "7B5C727466".toLowerCase(Locale.ROOT));
        fileTypeMap.put("xml", "3C3F786D6C".toLowerCase(Locale.ROOT));
        fileTypeMap.put("zip", "504B0304".toLowerCase(Locale.ROOT));
        fileTypeMap.put("rar", "52617221".toLowerCase(Locale.ROOT));
        fileTypeMap.put("psd", "38425053".toLowerCase(Locale.ROOT));
        fileTypeMap.put("eml", "44656C69766572792D646174653A".toLowerCase(Locale.ROOT));
        fileTypeMap.put("dbx", "CFAD12FEC5FD746F".toLowerCase(Locale.ROOT));
        fileTypeMap.put("pst", "2142444E".toLowerCase(Locale.ROOT));
        fileTypeMap.put("office", "D0CF11E0".toLowerCase(Locale.ROOT));
        fileTypeMap.put("mdb", "000100005374616E64617264204A".toLowerCase(Locale.ROOT));
        fileTypeMap.put("wpd", "FF575043".toLowerCase(Locale.ROOT));
        fileTypeMap.put("eps", "252150532D41646F6265".toLowerCase(Locale.ROOT));
        fileTypeMap.put("ps", "252150532D41646F6265".toLowerCase(Locale.ROOT));
        fileTypeMap.put("pdf", "255044462D312E".toLowerCase(Locale.ROOT));
        fileTypeMap.put("qdf", "AC9EBD8F".toLowerCase(Locale.ROOT));
        fileTypeMap.put("pwl", "E3828596".toLowerCase(Locale.ROOT));
        fileTypeMap.put("wav", "57415645".toLowerCase(Locale.ROOT));
        fileTypeMap.put("avi", "41564920".toLowerCase(Locale.ROOT));
        fileTypeMap.put("ram", "2E7261FD".toLowerCase(Locale.ROOT));
        fileTypeMap.put("rm", "2E524D46".toLowerCase(Locale.ROOT));
        fileTypeMap.put("mpg", "000001BA".toLowerCase(Locale.ROOT));
        fileTypeMap.put("mov", "6D6F6F76".toLowerCase(Locale.ROOT));
        fileTypeMap.put("asf", "3026B2758E66CF11".toLowerCase(Locale.ROOT));
        fileTypeMap.put("mid", "4D546864".toLowerCase(Locale.ROOT));
        fileTypeMap.put("exe", "4d5a50000200000004000f00ffff0000".toLowerCase(Locale.ROOT));
        return fileTypeMap;
    }

    public static final Map<String, String> getFileNameType() {
        Map<String, String> resultMap = new HashMap<>(16);
        for (String fileTypeName : DEFAULT_ALLOWED_EXTENSION) {
            resultMap.put(fileTypeName, fileTypeName);
        }
        return resultMap;
    }
}
