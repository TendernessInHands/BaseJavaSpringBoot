package com.mars.article.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mars.article.domain.Article;
import com.mars.article.domain.ArticleFile;
import com.mars.article.mapper.ArticleFileMapper;
import com.mars.article.mapper.ArticleMapper;
import com.mars.article.service.ArticleService;
import com.mars.common.constant.Constants;
import com.mars.common.core.controller.BaseController;
import com.mars.common.core.domain.AjaxResult;
import com.mars.common.core.domain.entity.SysUser;
import com.mars.common.utils.ServletUtils;
import com.mars.common.utils.SnowflakeIdWorker;
import com.mars.common.utils.LocalStringUtils;
import com.mars.framework.web.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author: mars
 * @date 2023/2/5
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private ArticleFileMapper articleFileMapper;

    @Autowired
    private TokenService tokenService;

    @Value("${mars.profile}")
    private String uploadFolder;

    @Override
    public AjaxResult add(Article article) {
        SysUser user = tokenService.getLoginUser(ServletUtils.getRequest()).getUser();
        article
                .setId(SnowflakeIdWorker.getSnowId())
                .setCreateTime(new Date())
                .setCreateUserId(user.getUserId())
                .setCreateUserName(user.getNickName())
                .setDelFlag(Constants.UNDELETE)
                .setOrgCode(user.getOrgCode())
                .setStatus(Constants.STRING_0);
        if (LocalStringUtils.isNotEmpty(article.getArticleFileList())) {
            List<ArticleFile> articleFileList = article.getArticleFileList();
            for (ArticleFile articleFile : articleFileList) {
                articleFile
                        .setId(SnowflakeIdWorker.getSnowId())
                        .setArticleId(article.getId())
                        .setCreateTime(new Date())
                        .setCreateUserId(user.getUserId())
                        .setCreateUserName(user.getNickName())
                        .setDelFlag(Constants.UNDELETE)
                        .setOrgCode(user.getOrgCode());
                articleFileMapper.insert(articleFile);
            }
        }
        return AjaxResult.toAjax(articleMapper.insert(article));
    }

    @Override
    public List<Article> queryList(String startTime, String endTime, String status, String title) {
        SysUser user = tokenService.getLoginUser(ServletUtils.getRequest()).getUser();
        BaseController.startPage();
        String orgCode = "";
        if (!user.isAdmin()) {
            orgCode = user.getOrgCode();
        }
        BaseController.startPage();
        List<Article> list = articleMapper.queryList(startTime, endTime, status, orgCode, title);
        if (LocalStringUtils.isNotEmpty(list)) {
            List<String> articleIdList = list.stream().map(Article::getId).collect(Collectors.toList());
            List<ArticleFile> articleFileList = articleFileMapper.selectByArticleIdList(articleIdList);
            if (LocalStringUtils.isNotEmpty(articleFileList)) {
                Map<String, List<ArticleFile>> fileMap = articleFileList.stream().collect(Collectors.groupingBy(ArticleFile::getArticleId));
                for (Article article : list) {
                    if (LocalStringUtils.isNotEmpty(fileMap.get(article.getId()))) {
                        List<ArticleFile> fileList = fileMap.get(article.getId());
                        article.setArticleFileList(fileList);
                    }
                }
            }
        }
        return list;
    }

    @Override
    public AjaxResult edit(Article article) {
        SysUser user = tokenService.getLoginUser(ServletUtils.getRequest()).getUser();
        article
                .setUpdateTime(new Date())
                .setUpdateUserId(user.getUserId())
                .setUpdateUserName(user.getNickName());

        LambdaQueryWrapper<ArticleFile> articleFileWrapper = new LambdaQueryWrapper<>();
        articleFileWrapper
                .eq(ArticleFile::getDelFlag, Constants.UNDELETE)
                .eq(ArticleFile::getArticleId, article.getId());
        List<ArticleFile> list = articleFileMapper.selectList(articleFileWrapper);
        if (LocalStringUtils.isNotEmpty(list)) {
            for (ArticleFile articleFile : list) {
                articleFile
                        .setDelFlag(Constants.DELETED)
                        .setDelTime(new Date())
                        .setDelUserId(user.getUserId())
                        .setDelUserName(user.getNickName());
                articleFileMapper.updateById(articleFile);
            }
        }
        if (LocalStringUtils.isNotEmpty(article.getArticleFileList())) {
            List<ArticleFile> articleFileList = article.getArticleFileList();
            for (ArticleFile articleFile : articleFileList) {
                articleFile
                        .setId(SnowflakeIdWorker.getSnowId())
                        .setArticleId(article.getId())
                        .setCreateTime(new Date())
                        .setCreateUserId(user.getUserId())
                        .setCreateUserName(user.getNickName())
                        .setDelFlag(Constants.UNDELETE)
                        .setOrgCode(user.getOrgCode());
                articleFileMapper.insert(articleFile);
            }
        }
        return AjaxResult.toAjax(articleMapper.updateById(article));
    }

    @Override
    public AjaxResult delete(String id) {
        SysUser user = tokenService.getLoginUser(ServletUtils.getRequest()).getUser();
        Article article = this.getById(id);
        article
                .setDelFlag(Constants.DELETED)
                .setDelUserId(user.getUserId())
                .setDelTime(new Date())
                .setDelUserName(user.getNickName());
        LambdaQueryWrapper<ArticleFile> articleFileWrapper = new LambdaQueryWrapper<>();
        articleFileWrapper
                .eq(ArticleFile::getDelFlag, Constants.UNDELETE)
                .eq(ArticleFile::getArticleId, id);
        List<ArticleFile> list = articleFileMapper.selectList(articleFileWrapper);
        if (LocalStringUtils.isNotEmpty(list)) {
            for (ArticleFile articleFile : list) {
                articleFile
                        .setDelFlag(Constants.DELETED)
                        .setDelUserId(user.getUserId())
                        .setDelTime(new Date())
                        .setDelUserName(user.getNickName());
                articleFileMapper.updateById(articleFile);
                File file = new File(uploadFolder + articleFile.getFilePath());
                if (null != file) {
                    file.delete();
                }
            }
        }
        return AjaxResult.toAjax(articleMapper.updateById(article));
    }
}
