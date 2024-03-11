package com.art.app.orm.service.impl;

import com.art.app.orm.dao.ArticleMapper;
import com.art.app.orm.entity.Article;
import com.art.app.orm.service.ArticleService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 文章表 服务实现类
 * </p>
 *
 * @author dragon123
 * @since 2019-12-17
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

    @Resource
    private ArticleMapper articleMapper;

    @Override
    public List<Article> findTagArticles(List<Integer> userIds) {
        return articleMapper.findTagArticles(userIds);
    }

    @Override
    public Integer findNonVideoTagArticleCount(String keywords) {
        return articleMapper.findNonVideoTagArticleCount(keywords);
    }

    @Override
    public List<Article> findNonTagArticlesByKeywords(String keywords, int pageIndex, int pageSize) {
        return articleMapper.findNonTagArticlesByKeywords(keywords, pageIndex, pageSize);
    }

    @Override
    public Integer findVideoTagArticleCount(String keywords) {
        return articleMapper.findVideoTagArticleCount(keywords);
    }

    @Override
    public List<Article> findVideoTagArticlesByKeywords(String keywords, int pageIndex, int pageSize) {
        return articleMapper.findVideoTagArticlesByKeywords(keywords, pageIndex, pageSize);
    }

    @Override
    public Integer findViewCountByAuthorId(Integer userId) {
        return articleMapper.findViewCountByAuthorId(userId);
    }
}
