package com.mars.student.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author: East
 * @date 2023/2/12
 */
@Data
@TableName("t_student")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class Student {

    private String id;

    private String name;

    private String age;

    private String sex;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date createTime;

    private String delFlag;
}
