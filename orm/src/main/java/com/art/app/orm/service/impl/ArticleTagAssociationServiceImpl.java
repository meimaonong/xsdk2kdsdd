package com.art.app.orm.service.impl;

import com.art.app.orm.entity.ArticleTagAssociation;
import com.art.app.orm.dao.ArticleTagAssociationMapper;
import com.art.app.orm.service.ArticleTagAssociationService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 文章与标签关联表 服务实现类
 * </p>
 *
 * @author dragon123
 * @since 2019-12-17
 */
@Service
public class ArticleTagAssociationServiceImpl extends ServiceImpl<ArticleTagAssociationMapper, ArticleTagAssociation> implements ArticleTagAssociationService {

}
