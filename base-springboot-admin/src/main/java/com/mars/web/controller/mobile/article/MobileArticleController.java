package com.mars.web.controller.mobile.article;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mars.article.domain.Article;
import com.mars.article.domain.ArticleFile;
import com.mars.article.service.ArticleFileService;
import com.mars.article.service.ArticleService;
import com.mars.common.config.MarsConfig;
import com.mars.common.constant.Constants;
import com.mars.common.core.controller.BaseController;
import com.mars.common.core.domain.AjaxResult;
import com.mars.common.core.domain.entity.SysUser;
import com.mars.common.core.page.TableDataInfo;
import com.mars.common.utils.LocalDateUtils;
import com.mars.common.utils.ServletUtils;
import com.mars.common.utils.file.FileUploadUtils;
import com.mars.framework.web.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

/**
 * @author: mars
 * @date 2023/2/5
 */
@RestController
@RequestMapping("/mobile/article")
public class MobileArticleController extends BaseController {
    @Autowired
    private ArticleService articleService;

    @Autowired
    private ArticleFileService articleFileService;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/add")
    public AjaxResult add(@RequestBody Article article) {
        return articleService.add(article);
    }

    @GetMapping("/list")
    public TableDataInfo queryList(@RequestParam(required = false) String startTime,
                                   @RequestParam(required = false) String title,
                                   @RequestParam(required = false) String endTime,
                                   @RequestParam(required = false) String status) {
        List<Article> list = articleService.queryList(startTime, endTime, status, title);
        return getDataTable(list);
    }

    @PostMapping("/edit")
    public AjaxResult edit(@RequestBody Article article) {
        return articleService.edit(article);
    }

    @GetMapping("/queryById")
    public AjaxResult queryById(@RequestParam String id) {
        Article article = articleService.getById(id);
        LambdaQueryWrapper<ArticleFile> articleFileWrapper = new LambdaQueryWrapper<>();
        articleFileWrapper
                .eq(ArticleFile::getDelFlag, Constants.UNDELETE)
                .eq(ArticleFile::getArticleId, id);
        List<ArticleFile> list = articleFileService.list(articleFileWrapper);
        article.setArticleFileList(list);
        return new AjaxResult(200, "成功", article);
    }

    @GetMapping("/changeStatus")
    public AjaxResult changeStatus(@RequestParam String id,
                                   @RequestParam String status) {
        Article article = articleService.getById(id);
        SysUser user = tokenService.getLoginUser(ServletUtils.getRequest()).getUser();
        article
                .setUpdateTime(new Date())
                .setUpdateUserId(user.getUserId())
                .setUpdateUserName(user.getNickName())
                .setStatus(status);
        articleService.updateById(article);
        return new AjaxResult(200, "成功");
    }

    @GetMapping("/delete")
    public AjaxResult delete(@RequestParam String id) {
        return articleService.delete(id);
    }

    @PostMapping("/upload")
    public AjaxResult uploadFile(@RequestBody MultipartFile multipartFile) {
        try {
            String date = LocalDateUtils.getDate();
            String[] split = date.split("-");
            String upload = FileUploadUtils.upload(MarsConfig.getArticle(split[0], split[1], split[2]), multipartFile);
            ArticleFile articleFile = new ArticleFile();
            articleFile.setFilePath(upload);
            articleFile.setFileName(multipartFile.getOriginalFilename());
            return new AjaxResult(200, "成功", articleFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new AjaxResult(500, "上传失败");
    }
}
