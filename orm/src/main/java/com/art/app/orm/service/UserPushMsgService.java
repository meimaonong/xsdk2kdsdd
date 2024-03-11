package com.art.app.orm.service;

import com.art.app.orm.entity.UserPushMsg;
import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author john
 * @since 2020-05-30
 */
public interface UserPushMsgService extends IService<UserPushMsg> {

    String findLatestMsgByUserIdAndType(int userId, int type);
}
