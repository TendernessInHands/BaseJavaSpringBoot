package com.mars.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 读取项目相关配置
 *
 * @author mars
 */
@Component
@ConfigurationProperties(prefix = "mars")
public class MarsConfig {
    /**
     * 项目名称
     */
    private String name;

    /**
     * 版本
     */
    private String version;

    /**
     * 版权年份
     */
    private String copyrightYear;

    /**
     * 实例演示开关
     */
    private boolean demoEnabled;

    /**
     * 上传路径
     */
    private static String profile;

    /**
     * 获取地址开关
     */
    private static boolean addressEnabled;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getCopyrightYear() {
        return copyrightYear;
    }

    public void setCopyrightYear(String copyrightYear) {
        this.copyrightYear = copyrightYear;
    }

    public boolean isDemoEnabled() {
        return demoEnabled;
    }

    public void setDemoEnabled(boolean demoEnabled) {
        this.demoEnabled = demoEnabled;
    }

    public static String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        MarsConfig.profile = profile;
    }

    public static boolean isAddressEnabled() {
        return addressEnabled;
    }

    public static void setAddressEnabled(boolean addressEnabled) {
        MarsConfig.addressEnabled = addressEnabled;
    }

    /**
     * 获取头像上传路径
     */
    public static String getAvatarPath() {
        return getProfile() + "/avatar";
    }

    /**
     * 获取下载路径
     */
    public static String getDownloadPath() {
        return getProfile() + "/download/";
    }

    /**
     * 获取上传路径
     */
    public static String getUploadPath() {
        return getProfile() + "/upload";
    }

    /**
     * 获取Banner图上传路径
     *
     * @param id
     * @param date
     * @return
     */
    public static String getBanner(String id, String year, String month, String date) {
        return getProfile() + "/banner/" + year + "/" + month + "/" + date + "/" + id;
    }

    /**
     * 获取Banner图上传路径
     *
     * @param id
     * @param date
     * @return
     */
    public static String getBannerContent(String date, String id) {
        return getProfile() + "/banner/" + date + "/" + id;
    }

    public static String getMenuIcon() {
        return getProfile() + "/app/menuIcon";
    }

    /**
     * 养殖动态
     */
    public static String getArticle(String year, String month, String day) {
        return getProfile() + "/article/" + year + "/" + month + "/" + day;
    }

}
