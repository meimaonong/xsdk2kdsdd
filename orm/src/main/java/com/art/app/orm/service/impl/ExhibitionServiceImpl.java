package com.art.app.orm.service.impl;

import com.art.app.orm.entity.Exhibition;
import com.art.app.orm.dao.ExhibitionMapper;
import com.art.app.orm.service.ExhibitionService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 展览表 服务实现类
 * </p>
 *
 * @author john
 * @since 2020-05-30
 */
@Service
public class ExhibitionServiceImpl extends ServiceImpl<ExhibitionMapper, Exhibition> implements ExhibitionService {

}
