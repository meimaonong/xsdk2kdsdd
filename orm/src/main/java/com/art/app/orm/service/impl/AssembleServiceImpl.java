package com.art.app.orm.service.impl;

import com.art.app.orm.entity.Assemble;
import com.art.app.orm.dao.AssembleMapper;
import com.art.app.orm.service.AssembleService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author john
 * @since 2020-06-06
 */
@Service
public class AssembleServiceImpl extends ServiceImpl<AssembleMapper, Assemble> implements AssembleService {

    @Resource
    private AssembleMapper assembleMapper;

    @Override
    public Assemble findByCondition(int type, int userId, String resId) {
        return assembleMapper.findByCondition(type, userId, resId);
    }
}
