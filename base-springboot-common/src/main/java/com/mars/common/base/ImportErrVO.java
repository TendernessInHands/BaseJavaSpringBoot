package com.mars.common.base;

import io.swagger.annotations.ApiModelProperty;

/**
 * 导入错误信息类
 * @author mars
 */
public class ImportErrVO {
    @ApiModelProperty(value = "第几行")
    private Integer cellNum;
    @ApiModelProperty(value = "错误信息")
    private String errMessage;

    public Integer getCellNum() {
        return cellNum;
    }

    public void setCellNum(Integer cellNum) {
        this.cellNum = cellNum;
    }

    public String getErrMessage() {
        return errMessage;
    }

    public void setErrMessage(String errMessage) {
        this.errMessage = errMessage;
    }

    @Override
    public String toString() {
        return "ImportErrVO{" +
                "cellNum=" + cellNum +
                ", errMessage='" + errMessage + '\'' +
                '}';
    }
}
