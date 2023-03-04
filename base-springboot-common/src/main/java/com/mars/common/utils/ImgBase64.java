//package com.mars.common.utils;
//
//import org.apache.commons.codec.binary.Base64;
//import sun.misc.BASE64Decoder;
//import sun.misc.BASE64Encoder;
//
//import java.io.*;
//
///**
// * 图片转码
// * @author mars
// */
//public class ImgBase64 {
//    /**
//     * 将图片转换成Base64编码
//     *
//     * @param imgFile 待处理图片
//     * @return
//     */
//    public static String getImgStr(String imgFile) {
//        // 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
//
//        InputStream in = null;
//        byte[] data = null;
//        // 读取图片字节数组
//        try {
//            in = new FileInputStream(imgFile);
//            data = new byte[in.available()];
//            in.read(data);
//            in.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return Base64.encodeBase64String(data);
//    }
//
//    /**
//     * 对字节数组字符串进行Base64解码并生成图片
//     *
//     * @param imgStr      图片数据
//     * @param imgFilePath 保存图片全路径地址
//     * @return
//     */
//    public static boolean generateImage(String imgStr, String imgFilePath) {
//        // 图像数据为空
//        if (imgStr == null) {
//            return false;
//        }
//        BASE64Decoder decoder = new BASE64Decoder();
//        try {
//            // Base64解码
//            byte[] b = decoder.decodeBuffer(imgStr);
//            for (int i = 0; i < b.length; ++i) {
//                // 调整异常数据
//                if (b[i] < 0) {
//                    b[i] += 256;
//                }
//            }
//            // 生成jpg图片
//            OutputStream out = new FileOutputStream(imgFilePath);
//            out.write(b);
//            out.flush();
//            out.close();
//            return true;
//        } catch (Exception e) {
//            return false;
//        }
//    }
//
//    public static String convertFileToBase64(String imgPath) {
//        byte[] data = null;
//        // 读取图片字节数组
//        try {
//            InputStream in = new FileInputStream(imgPath);
//            data = new byte[in.available()];
//            in.read(data);
//            in.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        // 对字节数组进行Base64编码，得到Base64编码的字符串
//        BASE64Encoder encoder = new BASE64Encoder();
//        String base64Str = encoder.encode(data);
//        base64Str = base64Str.replaceAll("(\r\n|\r|\n|\n\r|\\r\\n)", "");
//        return base64Str;
//    }
//}