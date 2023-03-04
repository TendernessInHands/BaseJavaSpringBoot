package com.mars.web.controller.student;

import com.mars.common.config.MarsConfig;
import com.mars.common.core.controller.BaseController;
import com.mars.common.core.domain.AjaxResult;
import com.mars.common.core.page.TableDataInfo;
import com.mars.common.utils.LocalDateUtils;
import com.mars.common.utils.file.FileUploadUtils;
import com.mars.student.domain.Student;
import com.mars.student.domain.StudentFile;
import com.mars.student.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author: East
 * @date 2023/2/17
 */
@RestController
@RequestMapping("/student")
public class StudentController extends BaseController {

    @Autowired
    StudentService studentService;

    @PostMapping("/add")
    public AjaxResult add(@RequestBody Student student) {
        return studentService.add(student);
    }

    @PostMapping("/edit")
    public AjaxResult edit(@RequestBody Student student) {
        student.setId("1625902358020640770");
        return studentService.edit(student);
    }

    @GetMapping("/query")
    public AjaxResult selectOne(String id) {
        Student student = studentService.queryById(id);
        return new AjaxResult(200, "查询成功", student);
    }

    @GetMapping("/list")
    public TableDataInfo list() {
        List<Student> list = studentService.queryList();
        return getDataTable(list);
    }

    @GetMapping("/queryList")
    public AjaxResult queryList(Student student) {
        return studentService.queryListParams(student);
    }

    @GetMapping("/queryListOneParam")
    public AjaxResult queryListOneParam(@RequestParam(required = false) String name,
                                        @RequestParam(required = false) String sex) {
        return studentService.queryListOneParam(name, sex);
    }

    @PostMapping("/saveArticleByStudentId")
    public AjaxResult saveArticleByStudentId(@RequestBody Map<String, Object> data) {
        return studentService.saveArticleByStudentId(data);
    }

    @GetMapping("/queryArticleListByStudentId")
    public TableDataInfo queryArticleListByStudentId(String id) {
        return studentService.queryArticleListByStudentId(id);
    }

    @PostMapping("/upload")
    public AjaxResult upload(@RequestBody MultipartFile file, @RequestParam("name") String name, @RequestParam("id") String id) {
        try {
            String date = LocalDateUtils.getDate();
            String[] dateSplit = date.split("-");
            String path = MarsConfig.getArticle(dateSplit[0], dateSplit[1], dateSplit[2]);
            String uploadFile = FileUploadUtils.upload(path, file);
            StudentFile studentFile = new StudentFile();
            studentFile.setUserId(id);
            studentFile.setFileName(file.getOriginalFilename());
            studentFile.setFilPath(uploadFile);
            studentFile.setCreateTime(new Date());
            studentFile.setCreateUserName(name);
            return studentService.saveFileById(studentFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new AjaxResult(203, "请选择文件进行上传");
    }

    @GetMapping("/getStudentImage")
    public AjaxResult getStudentImage(String id) {
        return studentService.getStudentImage(id);
    }
}
