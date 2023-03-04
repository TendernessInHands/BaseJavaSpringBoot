package com.mars.system.domain.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author  mars
 * @Date 2022/7/11 12:12
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class SysUserVO implements Serializable {

    /**
     *  用户ID
     */
    private String userId;

    /**
     * 用户名称
     */
    private String userName;

}