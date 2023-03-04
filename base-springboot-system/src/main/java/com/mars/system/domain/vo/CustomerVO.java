package com.mars.system.domain.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author: mars
 * @date 2023/1/16
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class CustomerVO implements Serializable {

    /**
     *  用户ID
     */
    private String userId;

    /**
     *  客户昵称
     */
    private String nickName;

    /**
     *  客户手机号码
     */
    private String phoneNumber;

    /**
     *  单位名称
     */
    private String unitName;

    /**
     * 用户邮箱
     */
    private String email;

    /**
     * 用户性别
     */
    private String sex;

    /**
     *  省
     */
    private String province;

    /**
     *  市
     */
    private String city;

    /**
     *  区
     */
    private String area;

    /**
     *  详细地址
     */
    private String address;

}
