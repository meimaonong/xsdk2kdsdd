package com.art.app.orm.dao;

import com.art.app.orm.entity.UserBanRecord;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author john
 * @since 2020-04-09
 */
public interface UserBanRecordMapper extends BaseMapper<UserBanRecord> {

    int replace(@Param("r") UserBanRecord r);

}
