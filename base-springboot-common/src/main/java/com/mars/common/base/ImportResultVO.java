package com.mars.common.base;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 *  导入返回信息
 *  @Description
 *  @author mars
 *  @Date 2019-10-23
 */
public class ImportResultVO {

    @ApiModelProperty(value = "成功 1成功  2 数据问题")
    private String success;

    @ApiModelProperty(value = "导入成功条数")
    private Integer number;

    @ApiModelProperty(value = "错误信息")
    private List<ImportErrVO> errList;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public List<ImportErrVO> getErrList() {
        return errList;
    }

    public void setErrList(List<ImportErrVO> errList) {
        this.errList = errList;
    }

    @Override
    public String toString() {
        return "ImportResultVO{" +
                "success='" + success + '\'' +
                ", number=" + number +
                ", errList=" + errList +
                '}';
    }
}
