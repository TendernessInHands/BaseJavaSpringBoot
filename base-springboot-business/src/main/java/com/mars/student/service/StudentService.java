package com.mars.student.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mars.common.core.domain.AjaxResult;
import com.mars.common.core.page.TableDataInfo;
import com.mars.student.domain.Student;
import com.mars.student.domain.StudentFile;

import java.util.List;
import java.util.Map;

/**
 * @author: East
 * @date 2023/2/12
 */
public interface StudentService extends IService<Student> {

    /**
     * 新增student
     *
     * @param student
     * @return
     */
    AjaxResult add(Student student);

    /**
     * 无条件列表查询
     *
     * @return
     */
    List<Student> queryList();

    /**
     * 修改
     *
     * @param student
     * @return
     */
    AjaxResult edit(Student student);

    /**
     * 根据id查询详情
     *
     * @param id
     * @return
     */
    Student queryById(String id);

    /**
     * 多条件查询
     *
     * @param student
     * @return
     */
    AjaxResult queryListParams(Student student);

    /**
     * 自定义查询
     *
     * @param name
     * @param sex
     * @return
     */
    AjaxResult queryListOneParam(String name, String sex);

    /**
     * 通过userId存储article中间表
     *
     * @param data
     * @return
     */
    AjaxResult saveArticleByStudentId(Map<String, Object> data);

    /**
     * 通过用户id查询已有文章
     *
     * @param id
     * @return
     */
    TableDataInfo queryArticleListByStudentId(String id);

    /**
     * 保存上传文件
     *
     * @param studentFile
     * @return
     */
    AjaxResult saveFileById(StudentFile studentFile);

    /**
     * 通过学生id获取头像信息
     * @param id
     * @return
     */
    AjaxResult getStudentImage(String id);
}
