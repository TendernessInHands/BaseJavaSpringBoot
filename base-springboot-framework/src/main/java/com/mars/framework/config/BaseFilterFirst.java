package com.mars.framework.config;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.FilterConfig;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author
 * @Title: WxBaseFilterFirst
 * @Description: TODO
 * @Date 2019/10/18 10:24
 */

public class BaseFilterFirst implements Filter {
    private static final Logger LOGGER = LoggerFactory.getLogger(BaseFilterFirst.class);

    @Autowired
    private BaseConfig baseConfig;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String uri = request.getRequestURI();
        String newUri = uri.replace("//", "/");
        // 使用HttpServletRequestWrapper重写Request请求参数
        request = new HttpServletRequestWrapper(request) {
            @Override
            public String getRequestURI() {
                return newUri;
            }
        };

        List<String> configList = new ArrayList<>();
        configList.addAll(Arrays.asList(baseConfig.getUrl().split(",")));
        LOGGER.info("dizhi==" + request.getRequestURI());
        Map<String, String[]> maps = request.getParameterMap();
        for (Map.Entry<String, String[]> entry : maps.entrySet()) {
            LOGGER.info("请求参数:  " + entry.getKey() + ":" + Arrays.toString(entry.getValue()) + ";");
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }


}
