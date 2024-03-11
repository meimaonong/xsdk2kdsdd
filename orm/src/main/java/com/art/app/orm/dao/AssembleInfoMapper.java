package com.art.app.orm.dao;

import com.art.app.orm.entity.AssembleInfo;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author john
 * @since 2020-06-06
 */
public interface AssembleInfoMapper extends BaseMapper<AssembleInfo> {

    int updateCountById(@Param("id") Integer id, @Param("addNumber") Integer addNumber);
}
