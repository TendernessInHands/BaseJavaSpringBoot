package com.mars.article.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mars.article.domain.ArticleFile;
import com.mars.article.mapper.ArticleFileMapper;
import com.mars.article.service.ArticleFileService;
import org.springframework.stereotype.Service;

/**
 * @author: mars
 * @date 2023/2/5
 */
@Service
public class ArticleFileServiceImpl extends ServiceImpl<ArticleFileMapper, ArticleFile> implements ArticleFileService {
}
