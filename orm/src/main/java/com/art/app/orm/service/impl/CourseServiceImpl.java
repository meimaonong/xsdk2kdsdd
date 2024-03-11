package com.art.app.orm.service.impl;

import com.art.app.orm.entity.Course;
import com.art.app.orm.dao.CourseMapper;
import com.art.app.orm.service.CourseService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 课程表 服务实现类
 * </p>
 *
 * @author dragon123
 * @since 2019-12-17
 */
@Service
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements CourseService {

}
