package com.art.app.orm.service.impl;

import com.art.app.orm.entity.LikeRecord;
import com.art.app.orm.dao.LikeRecordMapper;
import com.art.app.orm.service.LikeRecordService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 点赞记录表 服务实现类
 * </p>
 *
 * @author john
 * @since 2019-12-17
 */
@Service
public class LikeRecordServiceImpl extends ServiceImpl<LikeRecordMapper, LikeRecord> implements LikeRecordService {

    @Resource
    private LikeRecordMapper likeRecordMapper;

    @Override
    public int replace(LikeRecord r) {
        return likeRecordMapper.replace(r);
    }
}
