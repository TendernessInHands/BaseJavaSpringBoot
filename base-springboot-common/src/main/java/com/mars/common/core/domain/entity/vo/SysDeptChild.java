package com.mars.common.core.domain.entity.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * @author: mars
 * @date 2022/10/22
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class SysDeptChild implements Serializable {

    private String deptId;

    private String deptName;

    private String parentId;

    private List<SysDeptChild> childList;

}
