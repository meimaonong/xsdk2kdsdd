package com.art.app.orm.dao;

import com.art.app.orm.entity.Assemble;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author john
 * @since 2020-06-06
 */
public interface AssembleMapper extends BaseMapper<Assemble> {

    Assemble findByCondition(@Param("type") Integer type, @Param("userId") Integer userId, @Param("resId") String resId);
}
