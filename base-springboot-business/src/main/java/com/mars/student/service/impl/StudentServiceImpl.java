package com.mars.student.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mars.common.constant.Constants;
import com.mars.common.core.controller.BaseController;
import com.mars.common.core.domain.AjaxResult;
import com.mars.common.core.page.TableDataInfo;
import com.mars.student.domain.Student;
import com.mars.student.domain.StudentFile;
import com.mars.student.mapper.StudentFileMapper;
import com.mars.student.mapper.StudentMapper;
import com.mars.student.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author: East
 * @date 2023/2/12
 */
@Service
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements StudentService {

    @Autowired
    StudentMapper studentMapper;

    @Autowired
    StudentFileMapper studentFileMapper;

    @Override
    public AjaxResult add(Student student) {
        student.setDelFlag(Constants.UNDELETE);
        student.setCreateTime(new Date());
        this.save(student);
        return new AjaxResult(200, "添加成功!");
    }

    @Override
    public List<Student> queryList() {
        BaseController.startPage();
        List<Student> list = studentMapper.queryList();
        return list;
    }

    @Override
    public AjaxResult edit(Student student) {
        return AjaxResult.toAjax(studentMapper.updateById(student));
    }

    @Override
    public Student queryById(String id) {
        Student student = studentMapper.selectById(id);
        return student;
    }

    @Override
    public AjaxResult queryListParams(Student student) {
        BaseController.startPage();
        LambdaQueryWrapper<Student> studentWrapper = new LambdaQueryWrapper<>();
        studentWrapper.eq(Student::getDelFlag, Constants.UNDELETE);
        studentWrapper.like(Student::getName, student.getName());
        List<Student> list = studentMapper.selectList(studentWrapper);
        return new AjaxResult(200, "查询成功", list);
    }

    @Override
    public AjaxResult queryListOneParam(String name, String sex) {
        BaseController.startPage();
        List<Student> list = studentMapper.queryListOneParam(name, sex);
        return new AjaxResult(200, "查询Service下name：" + name + ", sex:" + sex, list);
    }

    @Override
    public AjaxResult saveArticleByStudentId(Map<String, Object> data) {
        List<String> articleIds = (List<String>) data.get("articleIds");
        String studentId = (String) data.get("studentId");
        studentMapper.saveArticleByStudentId(studentId, articleIds);
        return new AjaxResult(200, "添加成功");
    }

    @Override
    public TableDataInfo queryArticleListByStudentId(String id) {
        BaseController.startPage();
        List<Map<String, String>> articleList = studentMapper.queryArticleListByStudentId(id);
        return BaseController.getDataTable(articleList);
    }

    @Override
    public AjaxResult saveFileById(StudentFile studentFile) {
        studentFileMapper.insert(studentFile);
        return new AjaxResult(200, "添加成功");
    }

    @Override
    public AjaxResult getStudentImage(String id) {
        Map<String, Object> data = studentMapper.getStudentImage(id);
        return new AjaxResult(200, "查询成功", data);
    }
}

