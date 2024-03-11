package com.art.app.orm.service.impl;

import com.art.app.orm.entity.AccessLog;
import com.art.app.orm.dao.AccessLogMapper;
import com.art.app.orm.service.AccessLogService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author dragon123
 * @since 2020-03-28
 */
@Service
public class AccessLogServiceImpl extends ServiceImpl<AccessLogMapper, AccessLog> implements AccessLogService {

}
