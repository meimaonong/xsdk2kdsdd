package com.art.app.orm.service;

import com.art.app.orm.entity.Assemble;
import com.baomidou.mybatisplus.service.IService;

import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author dragon123
 * @since 2020-06-06
 */
public interface AssembleService extends IService<Assemble> {

    Assemble findByCondition(int type, int userId, String resId);

}
