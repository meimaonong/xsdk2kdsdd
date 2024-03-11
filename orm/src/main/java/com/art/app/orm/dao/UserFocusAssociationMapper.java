package com.art.app.orm.dao;

import com.art.app.orm.entity.UserFocusAssociation;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 用户关注表 Mapper 接口
 * </p>
 *
 * @author john
 * @since 2019-12-17
 */
public interface UserFocusAssociationMapper extends BaseMapper<UserFocusAssociation> {


    List<Integer> findFocusArtists(@Param("userId") Integer userId);
}
