package com.mars.student.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mars.student.domain.Student;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author: East
 * @date 2023/2/12
 */
@Mapper
public interface StudentMapper extends BaseMapper<Student> {

    /**
     * 列表查询
     *
     * @return
     */
    List<Student> queryList();

    /**
     * 自定义条件查询
     *
     * @param name
     * @param sex
     * @return
     */
    List<Student> queryListOneParam(@Param("name") String name, @Param("sex") String sex);

    /**
     * 通过学生id查询文章列表
     *
     * @param id
     * @return
     */
    List<Map<String, String>> queryArticleListByStudentId(@Param("id") String id);

    /**
     * 插入学生已有文章
     *
     * @param studentId
     * @param articleIds
     * @return
     */
    void saveArticleByStudentId(@Param("studentId") String studentId, @Param("articleIds") List<String> articleIds);

    /**
     * 查询学生头像
     * @param id
     * @return
     */
    Map<String, Object> getStudentImage(@Param("id") String id);
}
