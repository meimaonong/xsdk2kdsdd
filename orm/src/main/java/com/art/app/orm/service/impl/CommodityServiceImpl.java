package com.art.app.orm.service.impl;

import com.art.app.orm.entity.Commodity;
import com.art.app.orm.dao.CommodityMapper;
import com.art.app.orm.service.CommodityService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 精选（推荐商品）文章表 服务实现类
 * </p>
 *
 * @author dragon123
 * @since 2019-12-17
 */
@Service
public class CommodityServiceImpl extends ServiceImpl<CommodityMapper, Commodity> implements CommodityService {

}
