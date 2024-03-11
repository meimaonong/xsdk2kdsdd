package com.art.app.orm.service;

import com.art.app.orm.entity.UserBanRecord;
import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author dragon123
 * @since 2020-04-09
 */
public interface UserBanRecordService extends IService<UserBanRecord> {

    int replace(UserBanRecord r);

}
