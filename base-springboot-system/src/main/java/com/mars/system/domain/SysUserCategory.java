package com.mars.system.domain;

import java.io.Serializable;

/**
 * 用户分类
 * @author mars
 */
public class SysUserCategory implements Serializable {

    private String userId;

    private String categoryId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }
}
