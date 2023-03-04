package com.mars.common.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 数字处理工具类
 * @author mars
 */
public class NumberValidUtil {

    private static boolean isMatch(String regex, String orginal) {
        if (orginal == null || orginal.trim().equals("")) {
            return false;
        }
        Pattern pattern = Pattern.compile(regex);
        Matcher isNum = pattern.matcher(orginal);
        return isNum.matches();
    }

    public static boolean isPositiveInteger(String orginal) {
        return isMatch("^\\+{0,1}[1-9]\\d*", orginal);
    }

    public static boolean isNegativeInteger(String orginal) {
        return isMatch("^-[1-9]\\d*", orginal);
    }

    public static boolean isWholeNumber(String orginal) {
        return isMatch("[+-]{0,1}0", orginal) || isPositiveInteger(orginal) || isNegativeInteger(orginal);
    }

    public static boolean isPositiveDecimal(String orginal) {
        return isMatch("\\+{0,1}[0]\\.[1-9]*|\\+{0,1}[1-9]\\d*\\.\\d*", orginal);
    }

    public static boolean isNegativeDecimal(String orginal) {
        return isMatch("^-[0]\\.[1-9]*|^-[1-9]\\d*\\.\\d*", orginal);
    }

    public static boolean isDecimal(String orginal) {
        return isMatch("[-+]{0,1}\\d+\\.\\d*|[-+]{0,1}\\d*\\.\\d+", orginal);
    }

    public static boolean isRealNumber(String orginal) {

        return isWholeNumber(orginal) || isDecimal(orginal) || isRealNumber2(orginal);
    }

    /**
     * 验证科学技术发
     * @param orginal
     * @return
     */
    public static boolean isRealNumber2(String orginal) {
        try {
            if (null == orginal || "".equals(orginal)) {
                return false;
            }
            //科学计数法正则表达式
            String regx = "^((-?\\d+.?\\d*)[Ee]{1}(-?\\d+))$";
            Pattern pattern = Pattern.compile(regx);

            return pattern.matcher(orginal).matches();


        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 把阿拉伯数字转换为罗马数字
     *
     * @param number
     * @return
     */
    public static String a2r(int number) {
//        String rNumber = "";
        StringBuffer rNumberBuf = new StringBuffer("");
        int[] aArray = {1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};
        String[] rArray = {"M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};
        int maxBumber = 3999;
        if (number < 1 || number > maxBumber) {
//            rNumber = "-1";
            rNumberBuf = rNumberBuf.append("-1");
        } else {
            for (int i = 0; i < aArray.length; i++) {
                while (number >= aArray[i]) {
                    rNumberBuf = rNumberBuf.append(rArray[i]);
                    number -= aArray[i];
                }
            }
        }
        return rNumberBuf.toString();
    }

    public static void main(String[] args) {
        //sout(NumberValidUtil.isRealNumber("7.0000000000000007E-2"));
        //sout(NumberValidUtil.isRealNumber("101"));
        //sout(NumberValidUtil.isRealNumber("0101"));
    }
}  
