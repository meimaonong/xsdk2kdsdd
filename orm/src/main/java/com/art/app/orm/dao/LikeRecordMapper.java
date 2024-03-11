package com.art.app.orm.dao;

import com.art.app.orm.entity.LikeRecord;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 点赞记录表 Mapper 接口
 * </p>
 *
 * @author dragon123
 * @since 2019-12-17
 */
public interface LikeRecordMapper extends BaseMapper<LikeRecord> {

    int replace(@Param("r") LikeRecord r);
}
