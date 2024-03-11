package com.art.app.orm.service.impl;

import com.art.app.orm.entity.Tag;
import com.art.app.orm.dao.TagMapper;
import com.art.app.orm.service.TagService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 文章标签（视频、作品、活动，基地） 服务实现类
 * </p>
 *
 * @author dragon123
 * @since 2019-12-17
 */
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {

}
