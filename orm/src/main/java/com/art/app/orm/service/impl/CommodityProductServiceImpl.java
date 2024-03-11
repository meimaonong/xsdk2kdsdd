package com.art.app.orm.service.impl;

import com.art.app.orm.entity.CommodityProduct;
import com.art.app.orm.dao.CommodityProductMapper;
import com.art.app.orm.service.CommodityProductService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 精选关联产品表 服务实现类
 * </p>
 *
 * @author dragon123
 * @since 2019-12-17
 */
@Service
public class CommodityProductServiceImpl extends ServiceImpl<CommodityProductMapper, CommodityProduct> implements CommodityProductService {

}
