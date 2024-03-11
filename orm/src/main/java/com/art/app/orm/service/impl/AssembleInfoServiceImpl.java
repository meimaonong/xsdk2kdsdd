package com.art.app.orm.service.impl;

import com.art.app.orm.entity.AssembleInfo;
import com.art.app.orm.dao.AssembleInfoMapper;
import com.art.app.orm.service.AssembleInfoService;
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
public class AssembleInfoServiceImpl extends ServiceImpl<AssembleInfoMapper, AssembleInfo> implements AssembleInfoService {

    @Resource
    private AssembleInfoMapper assembleInfoMapper;

    @Override
    public int updateCountById(Integer id, Integer addNumber) {
        return assembleInfoMapper.updateCountById(id, addNumber);
    }
}
