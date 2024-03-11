package com.art.app.orm.service;

import com.art.app.orm.entity.AssembleInfo;
import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author dragon123
 * @since 2020-06-06
 */
public interface AssembleInfoService extends IService<AssembleInfo> {

    int updateCountById(Integer id, Integer addNumber);
}
