package com.mars.common.utils.ip;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.alibaba.fastjson.JSONObject;
import com.mars.common.config.MarsConfig;
import com.mars.common.constant.Constants;
import com.mars.common.utils.LocalStringUtils;
import com.mars.common.utils.http.HttpUtils;

/**
 * 获取地址类
 *
 * @author mars
 */
public class AddressUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(AddressUtils.class);

    /**
     * IP地址查询
     */
    public static final String IP_URL = "http://whois.pconline.com.cn/ipJson.jsp";

    /**
     * 未知地址
     */
    public static final String UNKNOWN = "XX XX";

    public static String getRealAddressByIP(String ip) {
        String address = UNKNOWN;
        // 内网不查询
        if (IpUtils.internalIp(ip)) {
            return "内网IP";
        }
        if (MarsConfig.isAddressEnabled()) {
            try {
                String rspStr = HttpUtils.sendGet(IP_URL, "ip=" + ip + "&json=true", Constants.GBK);
                if (LocalStringUtils.isEmpty(rspStr)) {
                    LOGGER.error("获取地理位置异常 {}", ip);
                    return UNKNOWN;
                }
                JSONObject obj = JSONObject.parseObject(rspStr);
                String region = obj.getString("pro");
                String city = obj.getString("city");
                return String.format("%s %s", region, city);
            } catch (Exception e) {
                LOGGER.error("获取地理位置异常 {}", ip);
            }
        }
        return address;
    }
}
