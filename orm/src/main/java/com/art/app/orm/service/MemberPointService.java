package com.art.app.orm.service;

import com.art.app.orm.entity.MemberPoint;
import com.baomidou.mybatisplus.service.IService;

import java.util.Map;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author dragon123
 * @since 2020-05-30
 */
public interface MemberPointService extends IService<MemberPoint> {

    Map<Integer, Integer> getUserPointsByType(int type);

}
