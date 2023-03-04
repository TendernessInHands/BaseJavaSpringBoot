package com.mars.article.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mars.article.domain.Article;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author: mars
 * @date 2023/2/5
 */
@Mapper
public interface ArticleMapper extends BaseMapper<Article> {

    /**
     * 列表
     *
     * @param startTime
     * @param endTime
     * @param status
     * @param orgCode
     * @param title
     * @return
     */
    List<Article> queryList(@Param("startTime") String startTime, @Param("endTime") String endTime,
                            @Param("status") String status, @Param("orgCode") String orgCode, @Param("title") String title);

}
