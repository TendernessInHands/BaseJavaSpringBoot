package com.mars.article.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mars.article.domain.Article;
import com.mars.common.core.domain.AjaxResult;

import java.util.List;

/**
 * @author: mars
 * @date 2023/2/5
 */
public interface ArticleService extends IService<Article> {

    /**
     *  新增
     * @param article
     * @return
     */
    AjaxResult add(Article article);

    /**
     *  列表
     * @param startTime
     * @param endTime
     * @param status
     * @param title
     * @return
     */
    List<Article> queryList(String startTime, String endTime, String status, String title);

    /**
     *  修改
     * @param article
     * @return
     */
    AjaxResult edit(Article article);

    /**
     *  删除
     * @param id
     * @return
     */
    AjaxResult delete(String id);
}
