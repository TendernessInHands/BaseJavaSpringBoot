package com.mars.common.utils;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import com.mars.common.core.text.Convert;

/**
 * 客户端工具类
 *
 * @author mars
 */
public class ServletUtils {
    /**
     * 获取String参数
     */
    public static String getParameter(String name) {
        return getRequest().getParameter(name);
    }

    /**
     * 获取String参数
     */
    public static String getParameter(String name, String defaultValue) {
        return Convert.toStr(getRequest().getParameter(name), defaultValue);
    }

    /**
     * 获取Integer参数
     */
    public static Integer getParameterToInt(String name) {
        return Convert.toInt(getRequest().getParameter(name));
    }

    /**
     * 获取Integer参数
     */
    public static Integer getParameterToInt(String name, Integer defaultValue) {
        return Convert.toInt(getRequest().getParameter(name), defaultValue);
    }

    /**
     * 获取request
     */
    public static HttpServletRequest getRequest() {
        return getRequestAttributes().getRequest();
    }

    /**
     * 获取response
     */
    public static HttpServletResponse getResponse() {
        return getRequestAttributes().getResponse();
    }

    /**
     * 获取session
     */
    public static HttpSession getSession() {
        return getRequest().getSession();
    }

    public static ServletRequestAttributes getRequestAttributes() {
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        return (ServletRequestAttributes) attributes;
    }

    /**
     * 将字符串渲染到客户端
     *
     * @param response 渲染对象
     * @param string   待渲染的字符串
     * @return null
     */
    public static String renderString(HttpServletResponse response, String string) {
        try {
            response.setStatus(200);
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            response.getWriter().print(string);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 是否是Ajax异步请求
     *
     * @param request
     */
    public static boolean isAjaxRequest(HttpServletRequest request) {
        String accept = request.getHeader("accept");
        String application = "application/json";
        if (accept != null && accept.indexOf(application) != -1) {
            return true;
        }
        String xmlhttprequest = "XMLHttpRequest";
        String xRequestedWith = request.getHeader("X-Requested-With");
        if (xRequestedWith != null && xRequestedWith.indexOf(xmlhttprequest) != -1) {
            return true;
        }
        String ajson = ".json";
        String axml = ".xml";
        String uri = request.getRequestURI();
        if (LocalStringUtils.inStringIgnoreCase(uri, ajson, axml)) {
            return true;
        }

        String json = "json";
        String xml = "xml";
        String ajax = request.getParameter("__ajax");
        if (LocalStringUtils.inStringIgnoreCase(ajax, json, xml)) {
            return true;
        }
        return false;
    }

    public static String getIpAddr(HttpServletRequest request) {
        String unKnow = "unknown";
        String directory = "../";
        String directoryOne = "..\\";
        String ipv6 = "0:0:0:0:0:0:0:1";
        String ip = request.getHeader("X-Real-IP");
        if (!LocalStringUtils.isBlank(ip) && !unKnow.equalsIgnoreCase(ip)) {
            if (ip.contains(directory) || ip.contains(directoryOne)) {
                return "";
            }
            return ip;
        }
        ip = request.getHeader("X-Forwarded-For");
        if (!LocalStringUtils.isBlank(ip) && !unKnow.equalsIgnoreCase(ip)) {
            // 多次反向代理后会有多个IP值，第一个为真实IP。
            int index = ip.indexOf(',');
            if (index != -1) {
                ip = ip.substring(0, index);
            }
            if (ip.contains(directory) || ip.contains(directoryOne)) {
                return "";
            }
            return ip;
        } else {
            ip = request.getRemoteAddr();
            if (ip.contains(directory) || ip.contains(directoryOne)) {
                return "";
            }
            if (ipv6.equals(ip)) {
                ip = "127.0.0.1";
            }
            return ip;
        }

    }

}
