package com.art.app.orm.service;

import com.art.app.orm.entity.LikeRecord;
import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 * 点赞记录表 服务类
 * </p>
 *
 * @author john
 * @since 2019-12-17
 */
public interface LikeRecordService extends IService<LikeRecord> {

    int replace(LikeRecord r);
}
