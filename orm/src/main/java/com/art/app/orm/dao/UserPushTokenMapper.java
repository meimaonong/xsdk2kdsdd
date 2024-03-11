package com.art.app.orm.dao;

import com.art.app.orm.entity.UserPushToken;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author john
 * @since 2020-05-27
 */
public interface UserPushTokenMapper extends BaseMapper<UserPushToken> {

    int replace(@Param("r") UserPushToken r);
}
