package com.art.app.orm.service.impl;

import com.art.app.orm.dao.UserFocusAssociationMapper;
import com.art.app.orm.entity.UserFocusAssociation;
import com.art.app.orm.service.UserFocusAssociationService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 用户关注表 服务实现类
 * </p>
 *
 * @author dragon123
 * @since 2019-12-17
 */
@Service
public class UserFocusAssociationServiceImpl extends ServiceImpl<UserFocusAssociationMapper, UserFocusAssociation> implements UserFocusAssociationService {

    @Resource
    private UserFocusAssociationMapper userFocusAssociationMapper;

    @Override
    public List<Integer> findFocusArtists(int userId) {
        return userFocusAssociationMapper.findFocusArtists(userId);
    }
}
