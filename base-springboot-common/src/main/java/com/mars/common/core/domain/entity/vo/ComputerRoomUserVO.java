package com.mars.common.core.domain.entity.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 *  @author mars
 *  @Date 2022/7/7 16:30
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class ComputerRoomUserVO implements Serializable {

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 用户昵称
     */
    private String nickName;


}