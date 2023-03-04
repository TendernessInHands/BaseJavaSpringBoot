package com.mars.article.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author: mars
 * @date 2023/2/5
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName("t_article")
public class Article implements Serializable {

    /**
     *  ID
     */
    private String id;

    /**
     *  标题
     */
    private String title;

    /**
     *  文章内容
     */
    private String content;

    /**
     *  文章状态，0-未发布，1-已发布
     */
    private String status;

    /**
     * 机构编码
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


    /**
     *  附件
     */
    @TableField(exist = false)
    private List<ArticleFile> articleFileList;


}
