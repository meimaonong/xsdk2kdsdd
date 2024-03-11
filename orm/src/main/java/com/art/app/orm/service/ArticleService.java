package com.art.app.orm.service;

import com.art.app.orm.entity.Article;
import com.baomidou.mybatisplus.service.IService;

import java.util.List;

/**
 * <p>
 * 文章表 服务类
 * </p>
 *
 * @author dragon123
 * @since 2019-12-17
 */
public interface ArticleService extends IService<Article> {

    List<Article> findTagArticles(List<Integer> userIds);

    Integer findNonVideoTagArticleCount(String keywords);

    List<Article> findNonTagArticlesByKeywords(String keywords, int pageIndex, int pageSize);

    Integer findVideoTagArticleCount(String keywords);

    List<Article> findVideoTagArticlesByKeywords(String keywords, int pageIndex, int pageSize);

    Integer findViewCountByAuthorId(Integer userId);
}
