package com.mars.framework.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


/**
 * @author
 * @Title: BaseConfig
 * @Description: TODO
 * @Date 2019/10/18 10:24
 */

@Component
public class BaseConfig {
    @Value(value = "${attributes.url}")
    private  String url;
    @Value(value = "${attributes.rs}")
    private  String resource;

    public String   getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public BaseConfig() {
    }

    public BaseConfig(String url, String resource) {
        this.url = url;
        this.resource = resource;
    }

}
