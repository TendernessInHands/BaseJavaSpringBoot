package com.mars.framework.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.DelegatingFilterProxy;

/**
 * 基础配置
 * @author
 */
@Configuration
public class ConfigBase {

    @Bean
    public BaseFilterFirst baseFilterFirst() {
        return new BaseFilterFirst();
    }

    @Bean
    public FilterRegistrationBean companyUrlFilterRegister() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        //注入过滤器
        registration.setFilter(new DelegatingFilterProxy("baseFilterFirst"));
           /* //拦截规则
            registration.addUrlPatterns("/api/*");*/
        //过滤器名称
        registration.setName("baseFilterFirst");
        //过滤器顺序
        registration.setOrder(FilterRegistrationBean.LOWEST_PRECEDENCE);
        return registration;
    }

}
