package com.mars.address.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * 收货地址
 * @author: mars
 * @date 2023/1/30
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName("t_address")
public class Address implements Serializable {

    private String id;

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

    /**
     *  客户ID
     */
    private String customerUserId;

    /**
     *  客户名称
     */
    private String customerUserName;

    /**
     *  客户电话
     */
    private String customerUserPhone;

    /**
     *  是否默认
     */
    private String isDefault;

    /**
     *  机构编码
     */
    private String orgCode;

    /**
     * 删除位
     */
    private String delFlag;

    /**
     * 创建人ID
     */
    private String createUserId;

    /**
     * 创建人名称
     */
    private String createUserName;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date createTime;

    /**
     * 修改人ID
     */
    private String updateUserId;

    /**
     * 修改人名称
     */
    private String updateUserName;

    /**
     * 修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date updateTime;

    /**
     * 删除人ID
     */
    private String delUserId;

    /**
     * 删除人名称
     */
    private String delUserName;

    /**
     * 删除时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date delTime;
}
