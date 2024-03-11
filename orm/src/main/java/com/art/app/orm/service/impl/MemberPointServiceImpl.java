package com.art.app.orm.service.impl;

import com.art.app.orm.bean.MemberCurrentPoints;
import com.art.app.orm.dao.MemberPointMapper;
import com.art.app.orm.entity.MemberPoint;
import com.art.app.orm.service.MemberPointService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author john
 * @since 2020-05-30
 */
@Service
public class MemberPointServiceImpl extends ServiceImpl<MemberPointMapper, MemberPoint> implements MemberPointService {

    @Resource
    private MemberPointMapper memberPointMapper;

    @Override
    public Map<Integer, Integer> getUserPointsByType(int type) {
        List<MemberCurrentPoints> memberCurrentPointsList = memberPointMapper.getUserPointsByType(type);
        if (Objects.isNull(memberCurrentPointsList) || memberCurrentPointsList.isEmpty()) {
            return Collections.emptyMap();
        }
        return memberCurrentPointsList.stream().collect(
                Collectors.toMap(MemberCurrentPoints::getUserId, MemberCurrentPoints::getPoints));
    }
}
