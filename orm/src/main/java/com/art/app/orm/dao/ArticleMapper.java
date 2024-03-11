package com.art.app.orm.dao;

import com.art.app.orm.entity.Article;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 文章表 Mapper 接口
 * </p>
 *
 * @author dragon123
 * @since 2019-12-17
 */
public interface ArticleMapper extends BaseMapper<Article> {

    List<Article> findTagArticles(List<Integer> userIds);

    Integer findViewCountByAuthorId(@Param("userId") Integer userId);

    Integer findNonVideoTagArticleCount(@Param("keywords") String keywords);

    List<Article> findNonTagArticlesByKeywords(@Param("keywords") String keywords, @Param("pageIndex") Integer pageIndex, @Param("pageSize") Integer pageSize);

    Integer findVideoTagArticleCount(@Param("keywords") String keywords);

    List<Article> findVideoTagArticlesByKeywords(@Param("keywords") String keywords, @Param("pageIndex") Integer pageIndex, @Param("pageSize") Integer pageSize);
}
