package com.mars.common.constant;

/**
 * 通用常量信息
 * 
 * @author mars
 */
public class Constants {
    /**
     * UTF-8 字符集
     */
    public static final String UTF8 = "UTF-8";

    /**
     * GBK 字符集
     */
    public static final String GBK = "GBK";

    /**
     * http请求
     */
    public static final String HTTP = "http://";

    /**
     * https请求
     */
    public static final String HTTPS = "https://";

    /**
     * 通用成功标识
     */
    public static final String SUCCESS = "0";

    /**
     * 通用失败标识
     */
    public static final String FAIL = "1";

    /**
     * 登录成功
     */
    public static final String LOGIN_SUCCESS = "Success";

    /**
     * 注销
     */
    public static final String LOGOUT = "Logout";

    /**
     * 登录失败
     */
    public static final String LOGIN_FAIL = "Error";

    /**
     * 验证码 redis key
     */
    public static final String CAPTCHA_CODE_KEY = "captcha_codes:";

    /**
     * 登录用户 redis key
     */
    public static final String LOGIN_TOKEN_KEY = "login_tokens:";
    
    /**
     * 防重提交 redis key
     */
    public static final String REPEAT_SUBMIT_KEY = "repeat_submit:";

    /**
     * 验证码有效期（分钟）
     */
    public static final Integer CAPTCHA_EXPIRATION = 2;

    /**
     * 令牌
     */
    public static final String TOKEN = "token";

    /**
     * 令牌前缀
     */
    public static final String TOKEN_PREFIX = "Bearer ";

    /**
     * 令牌前缀
     */
    public static final String LOGIN_USER_KEY = "login_user_key";

    /**
     * 用户ID
     */
    public static final String JWT_USERID = "userid";

    /**
     * 用户名称
     */
    public static final String JWT_USERNAME = "sub";

    /**
     * 用户头像
     */
    public static final String JWT_AVATAR = "avatar";

    /**
     * 创建时间
     */
    public static final String JWT_CREATED = "created";

    /**
     * 用户权限
     */
    public static final String JWT_AUTHORITIES = "authorities";

    /**
     * 参数管理 cache key
     */
    public static final String SYS_CONFIG_KEY = "sys_config:";

    /**
     * 字典管理 cache key
     */
    public static final String SYS_DICT_KEY = "sys_dict:";

    /**
     * 资源映射路径 前缀
     */
    public static final String RESOURCE_PREFIX = "/profile";

    /**
     *  删除标识
     */
    public static final String DELETED = "1";
    public static final String UNDELETE = "0";

    /**非法字符字段编码*/
    public static final String ILLEGAL_CHARACTER_DICT_TYPE = "sensitive_words";

    /**初始化密码*/
    public static final String INITIAL_PASSWORD = "1qaz@WSX3edc";

    /**是否管理员*/
    public static final String IS_ADMIN = "1";
    public static final String NOT_ADMIN = "0";

    /**
     * 状态响应码
     */
    public static final Integer SUCCESS_CODE = 200;
    public static final Integer FAILED_CODE = 500;

    /**
     *  默认父级ID
     */
    public static final String PARENT_ID = "1";

    /**
     *  字符串状态&类型
     */
    public static final String STRING_0 = "0";
    public static final String STRING_1 = "1";
    public static final String STRING_2 = "2";
    public static final String STRING_3 = "3";
    public static final String STRING_4 = "4";
    public static final String STRING_5 = "5";
    public static final String STRING_6 = "6";

    /**
     *  数字类型状态
     */
    public static final Integer INTEGER_STATUS_0 = 0;
    public static final Integer INTEGER_STATUS_1 = 1;
    public static final Integer INTEGER_STATUS_2 = 2;
    public static final Integer INTEGER_STATUS_3 = 3;
    public static final Integer INTEGER_STATUS_4 = 4;
    public static final Integer INTEGER_STATUS_5 = 5;
    public static final Integer INTEGER_STATUS_6 = 6;

    /**
     *  字典类型
     */
    public static final String SUPPLIER_TYPE = "supplier_type";

    /**
     *  养殖动态
     */
    public static final String ARTICLE_FILE_PATH = "";
    public static final String SEPARATOR_FILE_PATH = "/";
}
