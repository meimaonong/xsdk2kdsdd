package com.art.app.orm.dao;

import com.art.app.orm.bean.MemberCurrentPoints;
import com.art.app.orm.entity.MemberPoint;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author dragon123
 * @since 2020-05-30
 */
public interface MemberPointMapper extends BaseMapper<MemberPoint> {

    List<MemberCurrentPoints> getUserPointsByType(@Param("type") Integer type);

}
