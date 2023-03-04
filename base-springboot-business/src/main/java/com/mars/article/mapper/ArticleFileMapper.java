package com.mars.article.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mars.article.domain.ArticleFile;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author: mars
 * @date 2023/2/5
 */
@Mapper
public interface ArticleFileMapper extends BaseMapper<ArticleFile> {
    /**
     *  根据文章ID查询列表
     * @param articleIdList
     * @return
     */
    List<ArticleFile> selectByArticleIdList(@Param("articleIdList") List<String> articleIdList);
}
