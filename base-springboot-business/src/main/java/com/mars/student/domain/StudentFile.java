package com.mars.student.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author East
 * @date 2023/2/18
 */
@Data
@TableName("t_student_file")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class StudentFile {

    private String id;

    /**
     * 学生id
     */
    private String userId;

    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 文件路径
     */
    private String filPath;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date createTime;

    /**
     * 创建人名称
     */
    private String createUserName;
}
