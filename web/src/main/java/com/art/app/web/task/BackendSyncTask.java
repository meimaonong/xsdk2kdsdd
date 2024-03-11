package com.art.app.web.task;

import com.art.app.common.Constants;
import com.art.app.common.enums.ResourceTypeEnum;
import com.art.app.common.util.DatetimeUtils;
import com.art.app.mw.redis.RedisUtil;
import com.art.app.orm.entity.Article;
import com.art.app.orm.entity.Commodity;
import com.art.app.orm.entity.Course;
import com.art.app.orm.entity.Exhibition;
import com.art.app.orm.entity.SketchingActivity;
import com.art.app.orm.entity.TrainingClass;
import com.art.app.orm.entity.UserInfo;
import com.art.app.orm.entity.VideoCourse;
import com.art.app.orm.service.ArticleService;
import com.art.app.orm.service.CommodityService;
import com.art.app.orm.service.CourseService;
import com.art.app.orm.service.ExhibitionService;
import com.art.app.orm.service.MemberPointService;
import com.art.app.orm.service.SketchingActivityService;
import com.art.app.orm.service.TrainingClassService;
import com.art.app.orm.service.UserInfoService;
import com.art.app.orm.service.VideoCourseService;
import com.art.app.web.bean.MemberPointsRank;
import com.baomidou.mybatisplus.mapper.Condition;
import com.baomidou.mybatisplus.plugins.Page;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Component
public class BackendSyncTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(BackendSyncTask.class);

    @Autowired
    private ArticleService articleService;
    @Autowired
    private CommodityService commodityService;
    @Autowired
    private TrainingClassService trainingClassService;
    @Autowired
    private CourseService courseService;
    @Autowired
    private VideoCourseService videoCourseService;
    @Autowired
    private ExhibitionService exhibitionService;
    @Autowired
    private SketchingActivityService sketchingActivityService;
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private MemberPointService memberPointService;
    @Autowired
    private RedisUtil redisUtil;

    public static List<MemberPointsRank> memberPointsRankList = Lists.newArrayList();

    /**
     * top100
     */
    @Scheduled(fixedDelay = 5000)
    @SuppressWarnings("all")
    public void buildMemberShipPointRankList() {
        try {
            MDC.put(Constants.MDC_ID, UUID.randomUUID().toString());
            Page<UserInfo> userInfos = userInfoService.selectPage(new Page<>(1, 100), Condition.create().eq("del_flag", 0)
                    .orderDesc(Collections.singletonList("points")));
            if (CollectionUtils.isEmpty(userInfos.getRecords())) {
                return;
            }
            for (int i = 0; i < userInfos.getRecords().size(); i++) {
                memberPointsRankList.add(convertByUserInfo(userInfos.getRecords().get(i), i + 1));
            }
        } finally {
            MDC.remove(Constants.MDC_ID);
        }
    }

    /**
     * top100
     */
    @Scheduled(fixedDelay = 5000)
    @SuppressWarnings("all")
    @Transactional(rollbackFor = Exception.class)
    public void calculateMemberShipPoint() {
        try {
            MDC.put(Constants.MDC_ID, UUID.randomUUID().toString());

            Map<Integer, Integer> memberProducePoints = memberPointService.getUserPointsByType(0);
            Map<Integer, Integer> memberConsumePoints = memberPointService.getUserPointsByType(1);

            Map<Integer, Integer> userPoints = Maps.newHashMap();
            for (Map.Entry<Integer, Integer> entry : memberProducePoints.entrySet()) {
                if (Objects.isNull(memberConsumePoints.get(entry.getKey()))) {
                    userPoints.put(entry.getKey(), entry.getValue());
                } else {
                    userPoints.put(entry.getKey(), entry.getValue() - memberConsumePoints.get(entry.getKey()));
                }
            }

            if (MapUtils.isNotEmpty(userPoints)) {
                UserInfo userInfo = new UserInfo();
                userInfo.setUpdatedAt(DatetimeUtils.now());
                for (Map.Entry<Integer, Integer> entry : userPoints.entrySet()) {
                    userInfo.setPoints(entry.getValue());
                    userInfoService.update(userInfo, Condition.create().eq("del_flag", 0)
                            .and().eq("user_id", entry.getKey()));
                }
            }

        } finally {
            MDC.remove(Constants.MDC_ID);
        }
    }

    private MemberPointsRank convertByUserInfo(UserInfo userInfo, int rank) {
        MemberPointsRank memberPointsRank = new MemberPointsRank();
        memberPointsRank.setAvatar(userInfo.getAvatar());
        memberPointsRank.setNickName(userInfo.getNickName());
        memberPointsRank.setPoints(userInfo.getPoints());
        memberPointsRank.setRank(rank);
        return memberPointsRank;
    }

    @Scheduled(fixedDelay = 2000)
    @SuppressWarnings("all")
    public void syncResourceLike() {
        try {
            MDC.put(Constants.MDC_ID, UUID.randomUUID().toString());
            if (!redisUtil.lock(Constants.LIKE_MUTEX, String.valueOf(System.currentTimeMillis()), 5, TimeUnit.SECONDS)) {
                LOGGER.info("syncResourceLike break for mutex");
                return;
            }
            for (ResourceTypeEnum value : ResourceTypeEnum.values()) {
                Set<Object> resourceIds = redisUtil.sGet(Constants.RESOURCE_LIKE_SET_KEY_MAP.get(value.name()));
                if (CollectionUtils.isEmpty(resourceIds)) {
                    LOGGER.info("syncResourceLike break:{}", value.getName());
                    break;
                }
                Date now = DatetimeUtils.now();
                switch (value) {
                    case ARTICLE:
                        Article article = new Article();
                        for (Object resourceId : resourceIds) {
                            String sResourceId = String.valueOf(resourceId);
                            try {
                                article.setSnId(sResourceId);
                                article.setLikeNum(NumberUtils.toInt(String.valueOf(
                                        redisUtil.hget(Constants.RESOURCE_LIKE_KEY_MAP.get(value.name()), sResourceId))));
                                article.setUpdatedAt(now);
                                articleService.update(article,
                                        Condition.create().eq("sn_id", sResourceId).and().eq("del_flag", 0));
                                LOGGER.debug("syncResourceLike update article resId:{}, viewNum:{}", sResourceId, article.getLikeNum());
                            } catch (Exception e) {
                                LOGGER.error("syncResourceLike article:{}", sResourceId, e);
                            }
                        }
                        break;
                    case PRODUCT:
                        Commodity commodity = new Commodity();
                        for (Object resourceId : resourceIds) {
                            String sResourceId = String.valueOf(resourceId);
                            try {
                                commodity.setSnId(sResourceId);
                                commodity.setLikeNum(NumberUtils.toInt(String.valueOf(
                                        redisUtil.hget(Constants.RESOURCE_LIKE_KEY_MAP.get(value.name()), sResourceId))));
                                commodity.setUpdatedAt(now);
                                commodityService.update(commodity,
                                        Condition.create().eq("sn_id", sResourceId).and().eq("del_flag", 0));
                                LOGGER.debug("syncResourceLike update product resId:{}, viewNum:{}", sResourceId, commodity.getLikeNum());
                            } catch (Exception e) {
                                LOGGER.error("syncResourceLike product:{}", sResourceId, e);
                            }
                        }
                        break;
                    case TRAINING:
                        TrainingClass trainingClass = new TrainingClass();
                        for (Object resourceId : resourceIds) {
                            String sResourceId = String.valueOf(resourceId);
                            try {
                                trainingClass.setSnId(sResourceId);
                                trainingClass.setLikeNum(NumberUtils.toInt(String.valueOf(
                                        redisUtil.hget(Constants.RESOURCE_LIKE_KEY_MAP.get(value.name()), sResourceId))));
                                trainingClass.setUpdatedAt(now);
                                trainingClassService.update(trainingClass,
                                        Condition.create().eq("sn_id", sResourceId).and().eq("del_flag", 0));
                                LOGGER.debug("syncResourceLike update training resId:{}, viewNum:{}", sResourceId, trainingClass.getLikeNum());
                            } catch (Exception e) {
                                LOGGER.error("syncResourceLike training:{}", sResourceId, e);
                            }
                        }
                        break;
                    case COURSE:
                        Course course = new Course();
                        for (Object resourceId : resourceIds) {
                            String sResourceId = String.valueOf(resourceId);
                            try {
                                course.setSnId(sResourceId);
                                course.setLikeNum(NumberUtils.toInt(String.valueOf(
                                        redisUtil.hget(Constants.RESOURCE_LIKE_KEY_MAP.get(value.name()), sResourceId))));
                                course.setUpdatedAt(now);
                                courseService.update(course,
                                        Condition.create().eq("sn_id", sResourceId).and().eq("del_flag", 0));
                                LOGGER.debug("syncResourceLike update course resId:{}, viewNum:{}", sResourceId, course.getLikeNum());
                            } catch (Exception e) {
                                LOGGER.error("syncResourceLike course:{}", sResourceId, e);
                            }
                        }
                        break;
                    case VIDEO_COURSE:
                        VideoCourse videoCourse = new VideoCourse();
                        for (Object resourceId : resourceIds) {
                            String sResourceId = String.valueOf(resourceId);
                            try {
                                videoCourse.setSnId(sResourceId);
                                videoCourse.setLikeNum(NumberUtils.toInt(String.valueOf(
                                        redisUtil.hget(Constants.RESOURCE_LIKE_KEY_MAP.get(value.name()), sResourceId))));
                                videoCourse.setUpdatedAt(now);
                                videoCourseService.update(videoCourse,
                                        Condition.create().eq("sn_id", sResourceId).and().eq("del_flag", 0));
                                LOGGER.debug("syncResourceLike update videoCourse resId:{}, viewNum:{}", sResourceId, videoCourse.getLikeNum());
                            } catch (Exception e) {
                                LOGGER.error("syncResourceLike videoCourse:{}", sResourceId, e);
                            }
                        }
                        break;
                    case EXHIBITION:
                        Exhibition exhibition = new Exhibition();
                        for (Object resourceId : resourceIds) {
                            String sResourceId = String.valueOf(resourceId);
                            try {
                                exhibition.setSnId(sResourceId);
                                exhibition.setLikeNum(NumberUtils.toInt(String.valueOf(
                                        redisUtil.hget(Constants.RESOURCE_LIKE_KEY_MAP.get(value.name()), sResourceId))));
                                exhibition.setUpdatedAt(now);
                                exhibitionService.update(exhibition,
                                        Condition.create().eq("sn_id", sResourceId).and().eq("del_flag", 0));
                                LOGGER.debug("syncResourceLike update exhibition resId:{}, viewNum:{}", sResourceId, exhibition.getLikeNum());
                            } catch (Exception e) {
                                LOGGER.error("syncResourceLike exhibition:{}", sResourceId, e);
                            }
                        }
                        break;
                    case SKETCHING_ACTIVITY:
                        SketchingActivity sketchingActivity = new SketchingActivity();
                        for (Object resourceId : resourceIds) {
                            String sResourceId = String.valueOf(resourceId);
                            try {
                                sketchingActivity.setSnId(sResourceId);
                                sketchingActivity.setLikeNum(NumberUtils.toInt(String.valueOf(
                                        redisUtil.hget(Constants.RESOURCE_LIKE_KEY_MAP.get(value.name()), sResourceId))));
                                sketchingActivity.setUpdatedAt(now);
                                sketchingActivityService.update(sketchingActivity,
                                        Condition.create().eq("sn_id", sResourceId).and().eq("del_flag", 0));
                                LOGGER.debug("syncResourceLike update sketchingActivity resId:{}, viewNum:{}", sResourceId, sketchingActivity.getLikeNum());
                            } catch (Exception e) {
                                LOGGER.error("syncResourceLike sketchingActivity:{}", sResourceId, e);
                            }
                        }
                        break;
                    default:
                        break;
                }
            }
        } catch (Exception e) {
            LOGGER.error("syncResourceLike error", e);
        } finally {
            MDC.remove(Constants.MDC_ID);
        }
    }

    @Scheduled(fixedDelay = 2000)
    @SuppressWarnings("all")
    public void syncResourceView() {
        try {
            MDC.put(Constants.MDC_ID, UUID.randomUUID().toString());
            if (!redisUtil.lock(Constants.VIEW_MUTEX, String.valueOf(System.currentTimeMillis()), 5, TimeUnit.SECONDS)) {
                LOGGER.info("syncResourceView break for mutex");
                return;
            }
            for (ResourceTypeEnum value : ResourceTypeEnum.values()) {
                Set<Object> resourceIds = redisUtil.sGet(Constants.RESOURCE_VIEW_SET_KEY_MAP.get(value.name()));
                if (CollectionUtils.isEmpty(resourceIds)) {
                    LOGGER.info("syncResourceView break:{}", value.getName());
                    break;
                }
                Date now = DatetimeUtils.now();
                switch (value) {
                    case ARTICLE:
                        Article article = new Article();
                        for (Object resourceId : resourceIds) {
                            String sResourceId = String.valueOf(resourceId);
                            try {
                                article.setSnId(sResourceId);
                                article.setReadNum(NumberUtils.toInt(String.valueOf(
                                        redisUtil.hget(Constants.RESOURCE_VIEW_KEY_MAP.get(value.name()), sResourceId))));
                                article.setUpdatedAt(now);
                                articleService.update(article,
                                        Condition.create().eq("sn_id", sResourceId).and().eq("del_flag", 0));
                                LOGGER.debug("syncResourceView update article resId:{}, viewNum:{}", sResourceId, article.getReadNum());
                            } catch (Exception e) {
                                LOGGER.error("syncResourceView article resId:{}", sResourceId, e);
                            }
                        }
                        break;
                    case PRODUCT:
                        Commodity commodity = new Commodity();
                        for (Object resourceId : resourceIds) {
                            String sResourceId = String.valueOf(resourceId);
                            try {
                                commodity.setSnId(sResourceId);
                                commodity.setReadNum(NumberUtils.toInt(String.valueOf(
                                        redisUtil.hget(Constants.RESOURCE_VIEW_KEY_MAP.get(value.name()), sResourceId))));
                                commodity.setUpdatedAt(now);
                                commodityService.update(commodity,
                                        Condition.create().eq("sn_id", sResourceId).and().eq("del_flag", 0));
                                LOGGER.debug("syncResourceView update product resId:{}, viewNum:{}", sResourceId, commodity.getReadNum());
                            } catch (Exception e) {
                                LOGGER.error("syncResourceView product:{}", sResourceId, e);
                            }
                        }
                        break;
                    case TRAINING:
                        TrainingClass trainingClass = new TrainingClass();
                        for (Object resourceId : resourceIds) {
                            String sResourceId = String.valueOf(resourceId);
                            try {
                                trainingClass.setSnId(sResourceId);
                                trainingClass.setReadNum(NumberUtils.toInt(String.valueOf(
                                        redisUtil.hget(Constants.RESOURCE_VIEW_KEY_MAP.get(value.name()), sResourceId))));
                                trainingClass.setUpdatedAt(now);
                                trainingClassService.update(trainingClass,
                                        Condition.create().eq("sn_id", sResourceId).and().eq("del_flag", 0));
                                LOGGER.debug("syncResourceView update training resId:{}, viewNum:{}", sResourceId, trainingClass.getReadNum());
                            } catch (Exception e) {
                                LOGGER.error("syncResourceView training:{}", sResourceId, e);
                            }
                        }
                        break;
                    case COURSE:
                        Course course = new Course();
                        for (Object resourceId : resourceIds) {
                            String sResourceId = String.valueOf(resourceId);
                            try {
                                course.setSnId(sResourceId);
                                course.setReadNum(NumberUtils.toInt(String.valueOf(
                                        redisUtil.hget(Constants.RESOURCE_VIEW_KEY_MAP.get(value.name()), sResourceId))));
                                course.setUpdatedAt(now);
                                courseService.update(course,
                                        Condition.create().eq("sn_id", sResourceId).and().eq("del_flag", 0));
                                LOGGER.debug("syncResourceView update course resId:{}, viewNum:{}", sResourceId, course.getReadNum());
                            } catch (Exception e) {
                                LOGGER.error("syncResourceView course:{}", sResourceId, e);
                            }
                        }
                        break;
                    case VIDEO_COURSE:
                        VideoCourse videoCourse = new VideoCourse();
                        for (Object resourceId : resourceIds) {
                            String sResourceId = String.valueOf(resourceId);
                            try {
                                videoCourse.setSnId(sResourceId);
                                videoCourse.setReadNum(NumberUtils.toInt(String.valueOf(
                                        redisUtil.hget(Constants.RESOURCE_VIEW_KEY_MAP.get(value.name()), sResourceId))));
                                videoCourse.setUpdatedAt(now);
                                videoCourseService.update(videoCourse,
                                        Condition.create().eq("sn_id", sResourceId).and().eq("del_flag", 0));
                                LOGGER.debug("syncResourceView update videoCourse resId:{}, viewNum:{}", sResourceId, videoCourse.getReadNum());
                            } catch (Exception e) {
                                LOGGER.error("syncResourceView videoCourse:{}", sResourceId, e);
                            }
                        }
                        break;
                    case EXHIBITION:
                        Exhibition exhibition = new Exhibition();
                        for (Object resourceId : resourceIds) {
                            String sResourceId = String.valueOf(resourceId);
                            try {
                                exhibition.setSnId(sResourceId);
                                exhibition.setReadNum(NumberUtils.toInt(String.valueOf(
                                        redisUtil.hget(Constants.RESOURCE_VIEW_KEY_MAP.get(value.name()), sResourceId))));
                                exhibition.setUpdatedAt(now);
                                exhibitionService.update(exhibition,
                                        Condition.create().eq("sn_id", sResourceId).and().eq("del_flag", 0));
                                LOGGER.debug("syncResourceView update exhibition resId:{}, viewNum:{}", sResourceId, exhibition.getReadNum());
                            } catch (Exception e) {
                                LOGGER.error("syncResourceView exhibition:{}", sResourceId, e);
                            }
                        }
                        break;
                    case SKETCHING_ACTIVITY:
                        SketchingActivity sketchingActivity = new SketchingActivity();
                        for (Object resourceId : resourceIds) {
                            String sResourceId = String.valueOf(resourceId);
                            try {
                                sketchingActivity.setSnId(sResourceId);
                                sketchingActivity.setReadNum(NumberUtils.toInt(String.valueOf(
                                        redisUtil.hget(Constants.RESOURCE_VIEW_KEY_MAP.get(value.name()), sResourceId))));
                                sketchingActivity.setUpdatedAt(now);
                                sketchingActivityService.update(sketchingActivity,
                                        Condition.create().eq("sn_id", sResourceId).and().eq("del_flag", 0));
                                LOGGER.debug("syncResourceView update sketchingActivity resId:{}, viewNum:{}", sResourceId, sketchingActivity.getReadNum());
                            } catch (Exception e) {
                                LOGGER.error("syncResourceView sketchingActivity:{}", sResourceId, e);
                            }
                        }
                        break;
                    default:
                        break;
                }
            }
        } catch (Exception e) {
            LOGGER.error("syncResourceView error", e);
        } finally {
            MDC.remove(Constants.MDC_ID);
        }
    }
}
