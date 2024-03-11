package com.art.app.orm.service;

import com.art.app.orm.entity.UserFocusAssociation;
import com.baomidou.mybatisplus.service.IService;

import java.util.List;

/**
 * <p>
 * 用户关注表 服务类
 * </p>
 *
 * @author john
 * @since 2019-12-17
 */
public interface UserFocusAssociationService extends IService<UserFocusAssociation> {


    List<Integer> findFocusArtists(int userId);
}
