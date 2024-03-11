package com.art.app.orm.dao;

import com.art.app.orm.entity.UserPushMsg;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author john
 * @since 2020-05-30
 */
public interface UserPushMsgMapper extends BaseMapper<UserPushMsg> {

    String findLatestMsgByUserIdAndType(@Param("userId") Integer userId, @Param("type") Integer type);

}
