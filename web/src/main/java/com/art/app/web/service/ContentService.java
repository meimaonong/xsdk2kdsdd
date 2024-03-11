package com.art.app.web.service;

import com.alibaba.fastjson.JSON;
import com.art.app.common.Constants;
import com.art.app.common.enums.ArticleStatusEnum;
import com.art.app.common.enums.ArticleTagEnum;
import com.art.app.common.enums.ComplainReasonEnum;
import com.art.app.common.enums.ContentTypeEnum;
import com.art.app.common.enums.DegreeEnum;
import com.art.app.common.enums.MemberActionTypeEnum;
import com.art.app.common.enums.MessageTypeEnum;
import com.art.app.common.enums.RelationTypeEnum;
import com.art.app.common.enums.ResourceTypeEnum;
import com.art.app.common.enums.ResponseCodeEnum;
import com.art.app.common.enums.SearchTypeEnum;
import com.art.app.common.util.DatetimeUtils;
import com.art.app.common.util.MathUtils;
import com.art.app.common.util.TokenUtils;
import com.art.app.mw.aliyun.OssManager;
import com.art.app.mw.push.AndroidNotification;
import com.art.app.mw.push.Audience;
import com.art.app.mw.push.IosAlert;
import com.art.app.mw.push.IosNotification;
import com.art.app.mw.push.Message;
import com.art.app.mw.push.Notification;
import com.art.app.mw.push.Options;
import com.art.app.mw.push.Platform;
import com.art.app.mw.push.PushClient;
import com.art.app.mw.push.PushPayload;
import com.art.app.mw.push.PushResult;
import com.art.app.mw.redis.RedisUtil;
import com.art.app.orm.entity.Article;
import com.art.app.orm.entity.ArticleTagAssociation;
import com.art.app.orm.entity.Assemble;
import com.art.app.orm.entity.AssembleInfo;
import com.art.app.orm.entity.Carousel;
import com.art.app.orm.entity.Commodity;
import com.art.app.orm.entity.CommodityProduct;
import com.art.app.orm.entity.ComplainRecord;
import com.art.app.orm.entity.Coupon;
import com.art.app.orm.entity.Course;
import com.art.app.orm.entity.Exhibition;
import com.art.app.orm.entity.LikeRecord;
import com.art.app.orm.entity.MemberPoint;
import com.art.app.orm.entity.OperationConfig;
import com.art.app.orm.entity.PushMsg;
import com.art.app.orm.entity.PushMsgType;
import com.art.app.orm.entity.ReadRecord;
import com.art.app.orm.entity.SketchingActivity;
import com.art.app.orm.entity.TrainingClass;
import com.art.app.orm.entity.User;
import com.art.app.orm.entity.UserAuth;
import com.art.app.orm.entity.UserBanRecord;
import com.art.app.orm.entity.UserCoupon;
import com.art.app.orm.entity.UserFocusAssociation;
import com.art.app.orm.entity.UserInfo;
import com.art.app.orm.entity.UserPushMsg;
import com.art.app.orm.entity.UserPushMsgJob;
import com.art.app.orm.entity.UserPushToken;
import com.art.app.orm.entity.UserRejectRecord;
import com.art.app.orm.entity.VersionControl;
import com.art.app.orm.entity.VideoCourse;
import com.art.app.orm.entity.XslOrderInfoClass;
import com.art.app.orm.entity.XslOrderInfoExhibition;
import com.art.app.orm.entity.XslOrderInfoSketching;
import com.art.app.orm.service.ArticleService;
import com.art.app.orm.service.ArticleTagAssociationService;
import com.art.app.orm.service.AssembleInfoService;
import com.art.app.orm.service.AssembleService;
import com.art.app.orm.service.CarouselService;
import com.art.app.orm.service.CommodityProductService;
import com.art.app.orm.service.CommodityService;
import com.art.app.orm.service.ComplainRecordService;
import com.art.app.orm.service.CouponService;
import com.art.app.orm.service.CourseService;
import com.art.app.orm.service.ExhibitionService;
import com.art.app.orm.service.LikeRecordService;
import com.art.app.orm.service.MemberPointService;
import com.art.app.orm.service.OperationConfigService;
import com.art.app.orm.service.PushMsgService;
import com.art.app.orm.service.PushMsgTypeService;
import com.art.app.orm.service.ReadRecordService;
import com.art.app.orm.service.SketchingActivityService;
import com.art.app.orm.service.TrainingClassService;
import com.art.app.orm.service.UserAuthService;
import com.art.app.orm.service.UserBanRecordService;
import com.art.app.orm.service.UserCouponService;
import com.art.app.orm.service.UserFocusAssociationService;
import com.art.app.orm.service.UserInfoService;
import com.art.app.orm.service.UserPushMsgJobService;
import com.art.app.orm.service.UserPushMsgService;
import com.art.app.orm.service.UserPushTokenService;
import com.art.app.orm.service.UserRejectRecordService;
import com.art.app.orm.service.UserService;
import com.art.app.orm.service.VersionControlService;
import com.art.app.orm.service.VideoCourseService;
import com.art.app.orm.service.XslOrderInfoClassService;
import com.art.app.orm.service.XslOrderInfoExhibitionService;
import com.art.app.orm.service.XslOrderInfoMemberService;
import com.art.app.orm.service.XslOrderInfoSketchingService;
import com.art.app.payment.utils.JackJsonUtil;
import com.art.app.web.bean.ArticleDetail;
import com.art.app.web.bean.ArticleResource;
import com.art.app.web.bean.ArtistResource;
import com.art.app.web.bean.AssembleBasicInfo;
import com.art.app.web.bean.AssembleMember;
import com.art.app.web.bean.BanAuthorResource;
import com.art.app.web.bean.BizException;
import com.art.app.web.bean.CarouselResource;
import com.art.app.web.bean.CommodityDetail;
import com.art.app.web.bean.CourseDetail;
import com.art.app.web.bean.ExhibitionDetail;
import com.art.app.web.bean.PersonalStatistics;
import com.art.app.web.bean.ProductResource;
import com.art.app.web.bean.RelationDetail;
import com.art.app.web.bean.SketchDetail;
import com.art.app.web.bean.TrainingDetail;
import com.art.app.web.bean.UserBasicInfo;
import com.art.app.web.bean.VideoDetail;
import com.art.app.web.bean.request.PushRequest;
import com.art.app.web.bean.request.RequestVo;
import com.art.app.web.bean.response.ArticleResult;
import com.art.app.web.bean.response.ArtistResult;
import com.art.app.web.bean.response.AssembleDetail;
import com.art.app.web.bean.response.AssembleResult;
import com.art.app.web.bean.response.BanAuthorResult;
import com.art.app.web.bean.response.CommodityResult;
import com.art.app.web.bean.response.ComplainReason;
import com.art.app.web.bean.response.CourseResult;
import com.art.app.web.bean.response.FocusResult;
import com.art.app.web.bean.response.HomePageConfigResult;
import com.art.app.web.bean.response.HomePageResult;
import com.art.app.web.bean.response.MemberCertResult;
import com.art.app.web.bean.response.MemberHomeResult;
import com.art.app.web.bean.response.MessageCategoryResult;
import com.art.app.web.bean.response.MessageHomeResult;
import com.art.app.web.bean.response.PersonalBriefResult;
import com.art.app.web.bean.response.PointsContributionResult;
import com.art.app.web.bean.response.PointsHistoryResult;
import com.art.app.web.bean.response.PointsHomeResult;
import com.art.app.web.bean.response.RecommendResult;
import com.art.app.web.bean.response.RelationResult;
import com.art.app.web.bean.response.SearchListResult;
import com.art.app.web.bean.response.SearchResult;
import com.art.app.web.bean.response.SketchResult;
import com.art.app.web.bean.response.TrainingResult;
import com.art.app.web.bean.response.UserCouponsResult;
import com.art.app.web.bean.response.VideoInfo;
import com.art.app.web.bean.response.VideoResult;
import com.art.app.web.task.BackendSyncTask;
import com.baomidou.mybatisplus.mapper.Condition;
import com.baomidou.mybatisplus.plugins.Page;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.art.app.common.Constants.PLATFORMS;
import static com.art.app.common.enums.ResourceTypeEnum.ARTICLE;
import static com.art.app.common.enums.ResourceTypeEnum.COURSE;
import static com.art.app.common.enums.ResourceTypeEnum.EXHIBITION;
import static com.art.app.common.enums.ResourceTypeEnum.PRODUCT;
import static com.art.app.common.enums.ResourceTypeEnum.SKETCHING_ACTIVITY;
import static com.art.app.common.enums.ResourceTypeEnum.TRAINING;
import static com.art.app.common.enums.ResourceTypeEnum.VIDEO_COURSE;

@Service
public class ContentService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ContentService.class);

    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private LikeRecordService likeRecordService;
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private CarouselService carouselService;
    @Autowired
    private ArticleService articleService;
    @Autowired
    private OperationConfigService operationConfigService;
    @Autowired
    private ArticleTagAssociationService articleTagAssociationService;
    @Autowired
    private UserFocusAssociationService focusAssociationService;
    @Autowired
    private TrainingClassService trainingClassService;
    @Autowired
    private CourseService courseService;
    @Autowired
    private CommodityService commodityService;
    @Autowired
    private CommodityProductService productService;
    @Autowired
    private VersionControlService versionControlService;
    @Autowired
    private ReadRecordService readRecordService;
    @Autowired
    private ComplainRecordService complainRecordService;
    @Autowired
    private UserBanRecordService userBanRecordService;
    @Autowired
    private UserRejectRecordService userRejectRecordService;
    @Autowired
    private MemberPointService memberPointService;
    @Autowired
    private UserAuthService userAuthService;
    @Autowired
    private UserPushMsgService userPushMsgService;
    @Autowired
    private PushMsgService pushMsgService;
    @Autowired
    private PushClient pushClient;
    @Autowired
    private ExhibitionService exhibitionService;
    @Autowired
    private VideoCourseService videoCourseService;
    @Autowired
    private SketchingActivityService sketchingActivityService;
    @Autowired
    private UserPushTokenService userPushTokenService;
    @Autowired
    private AssembleService assembleService;
    @Autowired
    private AssembleInfoService assembleInfoService;
    @Autowired
    private UserService userService;
    @Autowired
    private XslOrderInfoClassService orderInfoClassService;
    @Autowired
    private XslOrderInfoExhibitionService orderInfoExhibitionService;
    @Autowired
    private XslOrderInfoSketchingService orderInfoSketchingService;
    @Autowired
    private PushMsgTypeService pushMsgTypeService;
    @Autowired
    private UserPushMsgJobService userPushMsgJobService;
    @Autowired
    private OssManager ossManager;
    @Autowired
    private CacheHolder cacheHolder;
    @Autowired
    private XslOrderInfoMemberService orderInfoMemberService;
    @Autowired
    private UserCouponService userCouponService;
    @Autowired
    private CouponService couponService;


    @Value("${detail.host}")
    private String detailHost = "https://www.xs.com";
    @Value("${cdn.host}")
    private String cdnHost;

    private List<ArticleResource> unifiedArticleResources = Lists.newArrayList();
    private Map<String, List<ArticleResource>> personalRecommendContent = Maps.newConcurrentMap();
    private Map<ArticleTagEnum, List<ArticleResource>> tagArticleMap = Maps.newConcurrentMap();
    private Map<String, Map<ArticleTagEnum, List<ArticleResource>>> personalTagArticleMap = Maps.newConcurrentMap();
    private Map<String, List<ArticleResource>> focusArticleMap = Maps.newConcurrentMap();

    private static final List<HomePageConfigResult> DEFAULT_HOMEPAGE_CONFIG
            = Lists.newArrayList(
            new HomePageConfigResult(ContentTypeEnum.FOCUS),
            new HomePageConfigResult(ContentTypeEnum.RECOMMEND),
            new HomePageConfigResult(ContentTypeEnum.WORK),
            new HomePageConfigResult(ContentTypeEnum.ACTIVITY),
            new HomePageConfigResult(ContentTypeEnum.ARTIST));

    private static final List<HomePageConfigResult> DEFAULT_HOMEPAGE_CONFIG_V1
            = Lists.newArrayList(
            new HomePageConfigResult(ContentTypeEnum.RECOMMEND),
            new HomePageConfigResult(ContentTypeEnum.VIDEO),
            new HomePageConfigResult(ContentTypeEnum.WORK),
            new HomePageConfigResult(ContentTypeEnum.ACTIVITY),
            new HomePageConfigResult(ContentTypeEnum.ARTIST));

    @SuppressWarnings("all")
    public void repair() {
        List<Article> articles = articleService.selectList(Condition.create().gt("id", 0));
        for (Article article : articles) {
            if (StringUtils.isBlank(article.getThumbUrl()) || "null".equalsIgnoreCase(article.getThumbUrl())) {
                article.setThumbUrl(extractImgUrl(article.getContent()));
                articleService.update(article, Condition.create().eq("id", article.getId()));
            }
        }
    }

    @SuppressWarnings("all")
    public void pushMsg(Map<String, Object> params) {
        int msgId = NumberUtils.toInt(String.valueOf(params.get("msgId")));
        PushMsg pushMsg = pushMsgService.selectOne(Condition.create().eq("id", msgId)
                .and().eq("del_flag", 0));
        if (Objects.isNull(pushMsg)) {
            return;
        }
        Audience audience = Audience.all();
        boolean pushAll = false;
        List<String> pushTarget = JSON.parseArray(String.valueOf(params.get("target")), String.class);
        if (CollectionUtils.isEmpty(pushTarget)) {
            pushAll = true;
        } else {
            audience = Audience.registrationId(pushTarget);
        }
        Platform platform = Platform.all();
        switch (NumberUtils.toInt(String.valueOf(params.get("platform")))) {
            case 1:
                platform = Platform.android();
                break;
            case 2:
                platform = Platform.ios();
                break;
            default:
                break;
        }
        PushPayload payload = PushPayload.newBuilder()
                .setPlatform(platform)
                .setAudience(audience)
                .setNotification(Notification.alert(pushMsg.getTitle()))
                .setMessage(Message.content(pushMsg.getContent()))
                .build();
        UserPushMsgJob userPushMsgJob = new UserPushMsgJob();
        try {
            PushResult result = pushClient.sendPush(payload);
            userPushMsgJob.setCreatedAt(DatetimeUtils.now());
            userPushMsgJob.setDelFlag(0);
            userPushMsgJob.setMsgId((long) msgId);
            userPushMsgJob.setUsers(JSON.toJSONString(pushTarget));
            userPushMsgJob.setStatus(1);
            if (!result.isResultOK()) {
                userPushMsgJob.setStatus(0);
                throw new BizException(ResponseCodeEnum.SYSTEM_ERROR);
            }
        } catch (Exception e) {
            LOGGER.error("pushMsg:{}", params, e);
        } finally {
            userPushMsgJobService.insert(userPushMsgJob);
        }
    }

    @SuppressWarnings("all")
    public AssembleResult assembleList(Map<String, Object> params) {
        AssembleResult assembleResult = new AssembleResult();
        List<AssembleInfo> assembleInfos = assembleInfoService.selectList(Condition.create().eq("del_flag", 0)
                .and().eq("type", NumberUtils.toInt(String.valueOf(params.get("type"))))
                .and().eq("sn_id", String.valueOf(params.get("resourceId"))));
        if (CollectionUtils.isEmpty(assembleInfos)) {
            throw new BizException(ResponseCodeEnum.PARAM_ERROR);
        }
        int pageIndex = NumberUtils.toInt(String.valueOf(params.get("pageIndex")));
        int pageSize = NumberUtils.toInt(String.valueOf(params.get("pageSize")));
        checkPageCondition(pageIndex, pageSize);
        assembleResult.setTitle(assembleInfos.get(0).getTitle());
        assembleResult.setApplyStopTime(assembleInfos.get(0).getApplyStopTime());
        assembleResult.setHasMore(assembleInfos.size() > (pageIndex * pageSize));
        assembleResult.setContents(convertByAssembleInfo(assembleInfos.subList((pageIndex - 1) * pageSize,
                assembleInfos.size() < pageIndex * pageSize ? assembleInfos.size() : pageIndex * pageSize)));
        return assembleResult;
    }

    @SuppressWarnings("all")
    public AssembleDetail assembleDetail(Map<String, Object> params) {
        AssembleDetail assembleResult = new AssembleDetail();
        AssembleInfo assembleInfo = assembleInfoService.selectOne(Condition.create().eq("del_flag", 0)
                .and().eq("id", NumberUtils.toInt(String.valueOf(params.get("assembleGroupId")))));
        if (Objects.isNull(assembleInfo)) {
            throw new BizException(ResponseCodeEnum.PARAM_ERROR);
        }
        assembleResult.setTitle(assembleInfo.getTitle());
        assembleResult.setApplyStopTime(assembleInfo.getApplyStopTime());
        assembleResult.setAssembleCount(assembleInfo.getCount());
        List<Assemble> assembles = assembleService.selectList(Condition.create().eq("del_flag", 0)
                .and().eq("assemble_id", assembleInfo.getId()));
        assembleResult.setContents(convertByAssemble(assembles));
        return assembleResult;
    }

    @SuppressWarnings("all")
    private List<AssembleMember> convertByAssemble(List<Assemble> assembles) {
        if (CollectionUtils.isEmpty(assembles)) {
            return Collections.emptyList();
        }
        List<AssembleMember> assembleMembers = Lists.newArrayList();
        for (Assemble assemble : assembles) {
            AssembleMember assembleMember = new AssembleMember();
            UserInfo userInfo = getUserById(assemble.getUserId());
            if (Objects.nonNull(userInfo)) {
                assembleMembers.add(assembleMember);
                assembleMember.setAvatar(userInfo.getAvatar());
                assembleMember.setNickName(userInfo.getNickName());
                XslOrderInfoClass xslOrderInfoClass = orderInfoClassService.selectOne(Condition.create().eq("del_flag", 0)
                        .and().eq("assemble_id", assemble.getAssembleId())
                        .and().eq("user_id", userInfo.getUserId())
                        .and().eq("payment_status", 3).last("limit 1"));
                if (Objects.nonNull(xslOrderInfoClass)) {
                    assembleMember.setNickName(xslOrderInfoClass.getName());
                }
                assembleMember.setUserId(userInfo.getUserId());
                assembleMember.setSponsor(assemble.getSponsor());
            }
        }
        return assembleMembers;
    }

    private List<AssembleBasicInfo> convertByAssembleInfo(List<AssembleInfo> assembleInfos) {
        if (CollectionUtils.isEmpty(assembleInfos)) {
            return Collections.emptyList();
        }
        return assembleInfos.stream().map(this::convertByAssembleInfo).collect(Collectors.toList());
    }

    @SuppressWarnings("all")
    private AssembleBasicInfo convertByAssembleInfo(AssembleInfo assembleInfo) {
        AssembleBasicInfo assembleBasicInfo = new AssembleBasicInfo();
        assembleBasicInfo.setAssembleCount(assembleInfo.getCount());
        UserInfo userInfo = getUserById(assembleInfo.getUserId());
        if (Objects.isNull(userInfo)) {
            throw new BizException(ResponseCodeEnum.PARAM_ERROR);
        }
        assembleBasicInfo.setNickName(userInfo.getNickName());
        assembleBasicInfo.setAvatar(userInfo.getAvatar());
        XslOrderInfoClass xslOrderInfoClass = orderInfoClassService.selectOne(Condition.create().eq("del_flag", 0)
                .and().eq("assemble_id", assembleInfo.getId())
                .and().eq("user_id", userInfo.getUserId())
                .and().eq("payment_status", 3).last("limit 1"));
        if (Objects.nonNull(xslOrderInfoClass)) {
            assembleBasicInfo.setNickName(xslOrderInfoClass.getName());
        }
        assembleBasicInfo.setUserId(userInfo.getUserId());
        assembleBasicInfo.setAssembleGroupId(assembleInfo.getId());
        return assembleBasicInfo;
    }

    public String getDetailUrl(ResourceTypeEnum resourceType, String resourceId) {
        String detailUrl = detailHost;
        String resourceUrl = "/article/detail?resourceId=";
        switch (resourceType) {
            case ARTICLE:
                resourceUrl = "/article/detail?resourceId=";
                break;
            case PRODUCT:
                resourceUrl = "/select/detail?resourceId=";
                break;
            case TRAINING:
                resourceUrl = "/training/detail?resourceId=";
                break;
            case COURSE:
                resourceUrl = "/course/detail?resourceId=";
                break;
            case EXHIBITION:
                resourceUrl = "/exhibition/detail?resourceId=";
                break;
            case VIDEO_COURSE:
                resourceUrl = "/video/detail?resourceId=";
                break;
            case SKETCHING_ACTIVITY:
                resourceUrl = "/sketch/detail?resourceId=";
                break;
            default:
                break;
        }
        try {
            Object configDetail = redisUtil.get("redis_detail_host");
            if (Objects.nonNull(configDetail)) {
                detailUrl = String.valueOf(configDetail);
            }
        } catch (Exception e) {
            throw new BizException(ResponseCodeEnum.PARAM_ERROR);
        }
        return detailUrl + resourceUrl + resourceId;
    }

    @SuppressWarnings("all")
    public List<CarouselResource> carousel() {
        List<CarouselResource> carouselResources = Collections.emptyList();
        List<Carousel> carousels = carouselService.selectList(Condition.create().eq("del_flag", 0));
        if (CollectionUtils.isNotEmpty(carousels)) {
            carouselResources = carousels.stream().map(this::convertByCarousel).collect(Collectors.toList());
        }
        return carouselResources;
    }

    public List<HomePageConfigResult> getHomePageConfig(RequestVo.BaseParams baseParams) {
        List<HomePageConfigResult> homePageConfigs = DEFAULT_HOMEPAGE_CONFIG;
        if (Objects.isNull(baseParams) || StringUtils.isBlank(baseParams.getAppVersion())
                || baseParams.getAppVersion().compareTo("1.4.3") < 0) {
            homePageConfigs = DEFAULT_HOMEPAGE_CONFIG_V1;
        }
        try {
            Object configObj = redisUtil.get(Constants.HOMEPAGE_CONFIG);
            if (Objects.nonNull(configObj)) {
                String config = String.valueOf(configObj);
                String[] parts = config.split(",");
                Set<HomePageConfigResult> hpConfig = Sets.newLinkedHashSet();
                for (String part : parts) {
                    hpConfig.add(new HomePageConfigResult(ContentTypeEnum.ofType(NumberUtils.toInt(part))));
                }
                homePageConfigs = Lists.newArrayList(hpConfig);
            }
        } catch (Exception e) {
            LOGGER.error("getHomePageConfig", e);
        }
        return homePageConfigs;
    }

    public void checkPageCondition(int pageIndex, int pageSize) {
        if (pageIndex <= 0 || pageSize < 1) {
            throw new BizException(ResponseCodeEnum.PARAM_ERROR);
        }
    }

    /**
     * homepage
     *
     * @param params
     * @return
     */
    @SuppressWarnings("all")
    public HomePageResult homePageList(Map<String, Object> params, String token) {
        HomePageResult result = new HomePageResult();
        ContentTypeEnum contentTypeEnum = ContentTypeEnum.ofType(NumberUtils.toInt(String.valueOf(params.get("type"))));
        int pageIndex = 0;
        int pageSize = 0;
        if (Objects.nonNull(params.get("pageIndex"))) {
            pageIndex = NumberUtils.toInt(String.valueOf(params.get("pageIndex")));
        }
        if (Objects.nonNull(params.get("pageSize"))) {
            pageSize = NumberUtils.toInt(String.valueOf(params.get("pageSize")));
        }
        int userId = TokenUtils.getUserIdByToken(token);
        switch (contentTypeEnum) {
            case RECOMMEND:
                checkPageCondition(pageIndex, pageSize);
                RecommendResult recommendResult = new RecommendResult();
                recommendResult.setAd(JackJsonUtil.toBean(cacheHolder.getGeneralKeyValue(Constants.AS_DESC),
                        new com.fasterxml.jackson.core.type.TypeReference<Map<String, Object>>() {
                        }));
//                recommendResult.getAd().put("detail",
//                        getDetailUrl(ResourceTypeEnum.ofType(NumberUtils.toInt(
//                                recommendResult.getAd().get("type").toString())), recommendResult.getAd().get("resource_id").toString()));
                result = recommendResult;
                List<ArticleResource> personalArticles = getPersonalRecommend(userId);
                if (CollectionUtils.isNotEmpty(personalArticles) && personalArticles.size() >= ((pageIndex - 1) * pageSize)) {
                    recommendResult.setContents(personalArticles.subList((pageIndex - 1) * pageSize,
                            personalArticles.size() < pageIndex * pageSize ? personalArticles.size() : pageIndex * pageSize));
                } else {
                    result.setHasMore(false);
                }
                break;
            case ARTIST:
                ArtistResult artistResult = new ArtistResult();
                result = artistResult;
                List<UserInfo> artists = userInfoService.selectList(
                        Condition.create().eq("is_artist", 1).and().eq("del_flag", 0));
                if (CollectionUtils.isNotEmpty(artists)) {
                    artistResult.setArtists(artists.stream().map(u -> convertByUserInfo(u, userId)).collect(Collectors.toList()));
                }
                artistResult.getHotArtists().addAll(findHotArtist(artists));
                result.setHasMore(false);
                break;
            case VIDEO:
            case WORK:
            case ACTIVITY:
                RecommendResult tagResult = new RecommendResult();
                result = tagResult;
                checkPageCondition(pageIndex, pageSize);
                Map<ArticleTagEnum, List<ArticleResource>> personalTagArticles = getPersonalTagArticles(userId);
                List<ArticleResource> targetResources = personalTagArticles.get(ArticleTagEnum.fromContentTypeEnum(contentTypeEnum));
                if (CollectionUtils.isNotEmpty(targetResources) && targetResources.size() >= ((pageIndex - 1) * pageSize)) {
                    tagResult.setContents(targetResources.subList((pageIndex - 1) * pageSize,
                            targetResources.size() < pageIndex * pageSize ? targetResources.size() : pageIndex * pageSize));
                } else {
                    result.setHasMore(false);
                }
                break;
            case FOCUS:
                checkPageCondition(pageIndex, pageSize);
                List<Integer> artistIds = focusAssociationService.findFocusArtists(userId);
                FocusResult focusArticlesResult = new FocusResult();
                result = focusArticlesResult;
                if (CollectionUtils.isNotEmpty(artistIds)) {
                    // show the articles of these artists
                    List<ArticleResource> focusArticleResouces = focusArticleMap.get(String.valueOf(userId));
                    if (CollectionUtils.isNotEmpty(focusArticleResouces) && focusArticleResouces.size() >= ((pageIndex - 1) * pageSize)) {
                        focusArticlesResult.setArticles(focusArticleResouces.subList((pageIndex - 1) * pageSize,
                                focusArticleResouces.size() < pageIndex * pageSize ? focusArticleResouces.size() : pageIndex * pageSize));
                    } else {
                        result.setHasMore(false);
                    }
                } else {
                    // show the recommend artists
                    List<UserInfo> allArtists = userInfoService.selectList(
                            Condition.create().eq("is_artist", 1).and().eq("del_flag", 0));
                    List<ArtistResource> tmpArtists = findHotArtist(allArtists);
                    if (((pageIndex - 1) * pageSize) > tmpArtists.size()) {
                        result.setHasMore(false);
                    } else {
                        focusArticlesResult.getArtist().addAll(
                                tmpArtists.subList(((pageIndex - 1) * pageSize),
                                        (pageIndex * pageSize) >= tmpArtists.size() ? tmpArtists.size() : (pageIndex * pageSize)));
                    }
                }
                break;
            default:
                break;
        }
        return result;
    }

    @SuppressWarnings("all")
    public List<ArtistResource> findHotArtist(List<UserInfo> artists) {
        OperationConfig operationConfig = operationConfigService.selectOne(
                Condition.create().eq("ref", ContentTypeEnum.ARTIST.getType())
                        .and().eq("type", ResourceTypeEnum.ARTIST.getType())
                        .and().eq("del_flag", 0));
        if (CollectionUtils.isNotEmpty(artists) &&
                Objects.nonNull(operationConfig) && StringUtils.isNotBlank(operationConfig.getResourceIds())) {
            List<Integer> hotArtistIds = Arrays.stream(operationConfig.getResourceIds().split(","))
                    .map(v -> NumberUtils.toInt(v)).collect(Collectors.toList());
            Map<Integer, UserInfo> userInfoMap = artists.stream().
                    collect(Collectors.toMap(UserInfo::getUserId, Function.identity(), (v1, v2) -> v2));
            if (CollectionUtils.isNotEmpty(hotArtistIds)) {
                List<ArtistResource> hotArtists = Lists.newArrayList();
                for (Integer hotArtistId : hotArtistIds) {
                    UserInfo hotArtist = userInfoMap.get(hotArtistId);
                    if (Objects.isNull(hotArtist)) {
                        continue;
                    }
                    hotArtists.add(convertByUserInfo(hotArtist, hotArtistId));
                }
                return hotArtists;
            }
        }
        return Collections.emptyList();
    }

    /**
     * selectPage
     *
     * @param params
     * @return
     */
    @SuppressWarnings("all")
    public HomePageResult selectList(Map<String, Object> params, String token) {
        ContentTypeEnum contentTypeEnum = ContentTypeEnum.ofType(NumberUtils.toInt(String.valueOf(params.get("type"))));
        int pageIndex = NumberUtils.toInt(String.valueOf(params.get("pageIndex")));
        int pageSize = NumberUtils.toInt(String.valueOf(params.get("pageSize")));
        CommodityResult result = new CommodityResult();
        int userId = TokenUtils.getUserIdByToken(token);
        switch (contentTypeEnum) {
            case BOOK:
            case PAINT:
                int type = contentTypeEnum.getResourceType();
                int total = commodityService.selectCount(
                        Condition.create().eq("del_flag", 0).and().eq("type", type));
                Page<Commodity> commodityPage = commodityService.selectPage(
                        new Page<>(pageIndex, pageSize),
                        Condition.create().eq("del_flag", 0).and().eq("type", type)
                                .orderDesc(Collections.singletonList("created_at")));
                if (CollectionUtils.isNotEmpty(commodityPage.getRecords())) {
                    List<CommodityResult.CommodityInfo> commodityInfos = commodityPage.getRecords()
                            .stream().map(c -> convertCommodityInfoByCommodity(c, userId)).collect(Collectors.toList());
                    result.setContents(commodityInfos);
                    result.setHasMore(total > (pageIndex * pageSize));
                }
                return result;
            case BASE:
                checkPageCondition(pageIndex, pageSize);
                List<ArticleResource> targetResources = tagArticleMap.get(ArticleTagEnum.LIVE_BASE);
                if (CollectionUtils.isNotEmpty(targetResources) && targetResources.size() >= ((pageIndex - 1) * pageSize)) {
                    result.setContents(targetResources.subList((pageIndex - 1) * pageSize,
                            targetResources.size() < pageIndex * pageSize ? targetResources.size() : pageIndex * pageSize)
                            .stream().map(c -> convertCommodityInfoByArticleResource(c, userId)).collect(Collectors.toList()));
                } else {
                    result.setHasMore(false);
                }
                return result;
            case EXHIBITION:
                checkPageCondition(pageIndex, pageSize);
                int totalExhibition = exhibitionService.selectCount(
                        Condition.create().eq("del_flag", 0));
                Page<Exhibition> exhibitionPage = exhibitionService.selectPage(
                        new Page<>(pageIndex, pageSize),
                        Condition.create().eq("del_flag", 0).orderDesc(Collections.singletonList("created_at")));
                if (CollectionUtils.isNotEmpty(exhibitionPage.getRecords())) {
                    List<CommodityResult.CommodityInfo> commodityInfos = exhibitionPage.getRecords()
                            .stream().map(c -> convertByExhibition(c, userId)).collect(Collectors.toList());
                    result.setContents(commodityInfos);
                    result.setHasMore(totalExhibition > (pageIndex * pageSize));
                } else {
                    result.setHasMore(false);
                    result.setContents(Collections.emptyList());
                }
                return result;
            default:
                break;
        }
        return new HomePageResult();
    }

    public CommodityResult.CommodityInfo convertByExhibition(Exhibition exhibition, int userId) {
        CommodityResult.CommodityInfo commodityInfo = new CommodityResult.CommodityInfo();
        UserInfo userInfo = getUserById(exhibition.getUserId());
        if (Objects.isNull(userInfo)) {
            return commodityInfo;
        }
        commodityInfo.setAuthor(userInfo.getNickName());
        if (userInfo.getIsArtist() == 1 && StringUtils.isNotBlank(userInfo.getUserName())) {
            commodityInfo.setAuthor(userInfo.getUserName());
        }
        commodityInfo.setTitle(exhibition.getTitle());
        commodityInfo.setUserId(exhibition.getUserId());
        commodityInfo.setAvatar(userInfo.getAvatar());
        commodityInfo.setCreatedAt(exhibition.getCreatedAt());
        commodityInfo.setImgUrl(exhibition.getThumbUrl());
        commodityInfo.setResourceId(exhibition.getSnId());
        commodityInfo.setViewNum(exhibition.getReadNum());
        commodityInfo.setLikeNum(exhibition.getLikeNum());
        commodityInfo.setLike(likeOrNot(userId, EXHIBITION.getType(), exhibition.getSnId()));
        commodityInfo.setDetailUrl(getDetailUrl(EXHIBITION, exhibition.getSnId()));
        commodityInfo.setDescription(exhibition.getDescription());
        return commodityInfo;
    }

    /**
     * selectDetail
     *
     * @param params
     * @return
     */
    @SuppressWarnings("all")
    public CommodityDetail selectDetail(Map<String, Object> params, String token, int source) {
        CommodityDetail result = null;
        int userId = TokenUtils.getUserIdByToken(token);
        ResourceTypeEnum resourceTypeEnum = PRODUCT;
        String resId = null;
        int author = 0;
        Commodity commodity = commodityService.selectOne(Condition.create().eq("del_flag", 0)
                .and().eq("sn_id", String.valueOf(params.get("resourceId"))));
        List<CommodityProduct> products = productService.selectList(
                Condition.create().eq("del_flag", 0).and().eq("sn_id", String.valueOf(params.get("resourceId"))));
        result = convertCommodityDetailByCommodity(commodity, products);
        resId = commodity.getSnId();
        author = commodity.getUserId();
        result.setLike(likeOrNot(userId, resourceTypeEnum.getType(), resId));
        result.setFocus(focusById(userId, author));

        if (source == 0) {
            processResourceView(resId, resourceTypeEnum.name(),
                    Constants.RESOURCE_VIEW_KEY_MAP.get(resourceTypeEnum.name()),
                    Constants.RESOURCE_VIEW_SET_KEY_MAP.get(resourceTypeEnum.name()), userId);
        }
        return result;
    }

    /**
     * homepage
     *
     * @param params
     * @return
     */
    @SuppressWarnings("all")
    public RecommendResult draftList(String token) {
        RecommendResult result = new RecommendResult();
        int userId = TokenUtils.getUserIdByToken(token);
        List<Article> drafts = articleService.selectList(
                Condition.create().eq("user_id", userId)
                        .and().eq("is_draft", 1)
                        .and().eq("del_flag", 0)
                        .orderBy("created_at", false));
        if (CollectionUtils.isNotEmpty(drafts)) {
            List<ArticleResource> draftResources = drafts.stream().map(this::convertByArticle).collect(Collectors.toList());
            result.setContents(draftResources);
        }
        return result;
    }

    /**
     * institute
     *
     * @param params
     * @return
     */
    @SuppressWarnings("all")
    public VideoResult videoList(Map<String, Object> params) {
        VideoResult result = new VideoResult();
        int pageIndex = NumberUtils.toInt(String.valueOf(params.get("pageIndex")));
        int pageSize = NumberUtils.toInt(String.valueOf(params.get("pageSize")));
        int total = videoCourseService.selectCount(Condition.create().eq("del_flag", 0));
        Page<VideoCourse> videoCoursePage = videoCourseService.selectPage(
                new Page<>(pageIndex, pageSize),
                Condition.create().eq("del_flag", 0).orderDesc(Collections.singletonList("created_at")));
        if (CollectionUtils.isNotEmpty(videoCoursePage.getRecords())) {
            List<VideoInfo> videoInfos = videoCoursePage.getRecords()
                    .stream().map(this::convertByVideo).collect(Collectors.toList());
            result.setContents(videoInfos);
            result.setHasMore(total > (pageIndex * pageSize));
        }
        return result;
    }

    /**
     * institute
     *
     * @param params
     * @return
     */
    @SuppressWarnings("all")
    public VideoDetail videoDetail(Map<String, Object> params, String token, int source) {
        int visitor = TokenUtils.getUserIdByToken(token);
        VideoDetail result = new VideoDetail();
        VideoCourse course = videoCourseService.selectOne(
                Condition.create().eq("del_flag", 0).and()
                        .eq("sn_id", String.valueOf(params.get("resourceId"))));
        if (Objects.nonNull(course)) {
            UserInfo userInfo = getUserById(course.getUserId());
            if (Objects.isNull(userInfo)) {
                return null;
            }
            result.setContent(course.getContent());
            result.setCreatedAt(course.getCreatedAt());
            result.setImgUrl(course.getThumbUrl());
            result.setResourceId(course.getSnId());
            result.setTitle(course.getTitle());
            Optional.ofNullable(course.getReadNum()).ifPresent(result::setViewNum);
            result.setAuthor(userInfo.getNickName());
            if (userInfo.getIsArtist() == 1 && StringUtils.isNotBlank(userInfo.getUserName())) {
                result.setAuthor(userInfo.getUserName());
            }
            result.setVideoUrl(course.getVideoUrl());
            result.setUserId(userInfo.getUserId());
            result.setAvatar(userInfo.getAvatar());
            result.setDescription(course.getDescription());
            Optional.ofNullable(course.getLikeNum()).ifPresent(result::setLikeNum);
            result.setLike(likeOrNot(visitor, VIDEO_COURSE.getType(), course.getSnId()));
            if (source == 0) {
                processResourceView(course.getSnId(), VIDEO_COURSE.name(),
                        Constants.RESOURCE_VIEW_KEY_MAP.get(VIDEO_COURSE.name()),
                        Constants.RESOURCE_VIEW_SET_KEY_MAP.get(VIDEO_COURSE.name()), visitor);
            }
        }
        return result;
    }

    /**
     * institute
     *
     * @param params
     * @return
     */
    @SuppressWarnings("all")
    public SketchResult sketchList(Map<String, Object> params) {
        SketchResult result = new SketchResult();
        int pageIndex = NumberUtils.toInt(String.valueOf(params.get("pageIndex")));
        int pageSize = NumberUtils.toInt(String.valueOf(params.get("pageSize")));
        int total = sketchingActivityService.selectCount(Condition.create().eq("del_flag", 0));
        Page<SketchingActivity> sketchingActivityPage = sketchingActivityService.selectPage(
                new Page<>(pageIndex, pageSize),
                Condition.create().eq("del_flag", 0).orderDesc(Collections.singletonList("created_at")));
        if (CollectionUtils.isNotEmpty(sketchingActivityPage.getRecords())) {
            List<SketchResult.SketchInfo> sketchInfos = sketchingActivityPage.getRecords()
                    .stream().map(this::convertByCourse).collect(Collectors.toList());
            result.setContents(sketchInfos);
            result.setHasMore(total > (pageIndex * pageSize));
        }
        return result;
    }

    /**
     * institute
     *
     * @param params
     * @return
     */
    @SuppressWarnings("all")
    public SketchDetail sketchDetail(Map<String, Object> params, String token, int source) {
        int userId = TokenUtils.getUserIdByToken(token);
        int visitor = NumberUtils.toInt(String.valueOf(params.get("userId")));
        SketchDetail result = new SketchDetail();
        SketchingActivity sketchingActivity = sketchingActivityService.selectOne(
                Condition.create().eq("del_flag", 0).and()
                        .eq("sn_id", String.valueOf(params.get("resourceId"))));
        if (Objects.nonNull(sketchingActivity)) {
            UserInfo userInfo = getUserById(sketchingActivity.getUserId());
            if (Objects.isNull(userInfo)) {
                return null;
            }
            result.setContent(sketchingActivity.getContent());
            result.setCreatedAt(sketchingActivity.getCreatedAt());
            result.setImgUrl(sketchingActivity.getThumbUrl());
            result.setResourceId(sketchingActivity.getSnId());
            result.setTitle(sketchingActivity.getTitle());
            Optional.ofNullable(sketchingActivity.getReadNum()).ifPresent(result::setViewNum);
            result.setAuthor(userInfo.getNickName());
            if (userInfo.getIsArtist() == 1 && StringUtils.isNotBlank(userInfo.getUserName())) {
                result.setAuthor(userInfo.getUserName());
            }
            result.setUserId(userInfo.getUserId());
            result.setAvatar(userInfo.getAvatar());
            result.setDescription(sketchingActivity.getDescription());
            Optional.ofNullable(sketchingActivity.getLikeNum()).ifPresent(result::setLikeNum);
            result.setLike(likeOrNot(visitor, SKETCHING_ACTIVITY.getType(), sketchingActivity.getSnId()));

            result.setIsGroup(sketchingActivity.getIsGroup());
            result.setIsSignUp(sketchingActivity.getIsSignUp());

            Optional.ofNullable(sketchingActivity.getLikeNum()).ifPresent(result::setLikeNum);
            Optional.ofNullable(sketchingActivity.getApplyStopTime()).ifPresent(result::setApplyStopTime);
            Optional.ofNullable(sketchingActivity.getClassStartTime()).ifPresent(result::setClassStartTime);
            Optional.ofNullable(sketchingActivity.getClassStopTime()).ifPresent(result::setClassStopTime);

            if (result.getIsGroup() == 0) {
                XslOrderInfoSketching xslOrderInfoSketching = orderInfoSketchingService.selectOne(Condition.create().eq("del_flag", 0)
                        .and().eq("sketching_id", sketchingActivity.getSnId())
                        .and().eq("assemble_id", 0)
                        .and().eq("user_id", userId)
                        .and().eq("payment_status", 3).last("limit 1"));
                if (Objects.nonNull(xslOrderInfoSketching)) {
                    result.setPurchaseStatus(1);
                }
            } else {
                List<AssembleInfo> assembleInfos = assembleInfoService.selectList(Condition.create().eq("del_flag", 0)
                        .and().eq("type", SKETCHING_ACTIVITY.getType()).and().eq("sn_id", sketchingActivity.getSnId()));
                if (CollectionUtils.isEmpty(assembleInfos)) {
                    // 当前此资源无拼团
                    result.setAssembleGroupId(0);
                    result.setJoinAssemble(0);
                    result.setAssembleTotalCount(0);
                    result.setAssembleCount(0);
                } else {
                    result.setAssembleTotalCount(assembleInfos.stream().mapToInt(AssembleInfo::getCount).sum());
                    // 传入的userId 对应的拼团
                    Assemble visitorAssemble = assembleService.findByCondition(SKETCHING_ACTIVITY.getType(), visitor, sketchingActivity.getSnId());
                    // 用户自己参与 的拼团
                    Assemble userAssemble = assembleService.findByCondition(SKETCHING_ACTIVITY.getType(), userId, sketchingActivity.getSnId());
                    if (Objects.nonNull(userAssemble)) {
                        result.setJoinAssemble(1);
                        result.setAssembleGroupId(userAssemble.getAssembleId());
                        result.setAssembleCount(assembleService.selectCount(Condition.create().eq("del_flag", 0)
                                .and().eq("assemble_id", userAssemble.getAssembleId())));
                        result.setSponsorInfo(findSponsorByUserId(userAssemble.getUserId()));
                    } else {
                        result.setJoinAssemble(0);
                        if (Objects.isNull(visitorAssemble)) {
                            // 选举一个当前人数最少的团返回，如果没有返回 0
                            assembleInfos.sort(new Comparator<AssembleInfo>() {
                                @Override
                                public int compare(AssembleInfo o1, AssembleInfo o2) {
                                    return Integer.valueOf(o1.getCount()).compareTo(Integer.valueOf(o2.getCount()));
                                }
                            });
                            result.setAssembleCount(assembleInfos.get(0).getCount());
                            result.setAssembleGroupId(assembleInfos.get(0).getId());
                            result.setSponsorInfo(findSponsorByUserId(assembleInfos.get(0).getUserId()));
                        } else {
                            int assembleCount = assembleService.selectCount(Condition.create().eq("del_flag", 0)
                                    .and().eq("assemble_Id", visitorAssemble.getAssembleId()));
                            result.setAssembleCount(assembleCount);
                            result.setAssembleGroupId(visitorAssemble.getAssembleId());
                            result.setSponsorInfo(findSponsorByUserId(visitorAssemble.getUserId()));
                        }
                    }
                }
            }

            if (source == 0) {
                processResourceView(sketchingActivity.getSnId(), SKETCHING_ACTIVITY.name(),
                        Constants.RESOURCE_VIEW_KEY_MAP.get(SKETCHING_ACTIVITY.name()),
                        Constants.RESOURCE_VIEW_SET_KEY_MAP.get(SKETCHING_ACTIVITY.name()), visitor);
            }
        }
        return result;
    }

    /**
     * institute
     *
     * @param params
     * @return
     */
    @SuppressWarnings("all")
    public CourseResult courseList(Map<String, Object> params) {
        CourseResult result = new CourseResult();
        int pageIndex = NumberUtils.toInt(String.valueOf(params.get("pageIndex")));
        int pageSize = NumberUtils.toInt(String.valueOf(params.get("pageSize")));
        int total = courseService.selectCount(Condition.create().eq("del_flag", 0));
        Page<Course> coursePage = courseService.selectPage(
                new Page<>(pageIndex, pageSize),
                Condition.create().eq("del_flag", 0).orderDesc(Collections.singletonList("created_at")));
        if (CollectionUtils.isNotEmpty(coursePage.getRecords())) {
            List<CourseResult.CourseInfo> courseInfos = coursePage.getRecords()
                    .stream().map(this::convertByCourse).collect(Collectors.toList());
            result.setContents(courseInfos);
            result.setHasMore(total > (pageIndex * pageSize));
        }
        return result;
    }

    @SuppressWarnings("all")
    private UserBasicInfo findSponsorByUserId(int userId) {
        UserBasicInfo userBasicInfo = new UserBasicInfo();
        userBasicInfo.setUserId(userId);
        UserInfo userInfo = getUserById(userId);
        User user = userService.selectOne(Condition.create().eq("del_flag", 0).and().eq("id", userId));
        if (Objects.isNull(userInfo) || Objects.isNull(user)) {
            throw new BizException(ResponseCodeEnum.PARAM_ERROR);
        }
        userBasicInfo.setNickName(userInfo.getNickName());
        userBasicInfo.setAvatar(userInfo.getAvatar());
        userBasicInfo.setPhone(user.getMobile());
        return userBasicInfo;
    }

    /**
     * 计算一个拼团资源的总人数Tra
     *
     * @param type
     * @param resId
     * @return
     */
    @SuppressWarnings("all")
    private int findAssembleTotalCount(int type, String resId) {
        int count = assembleService.selectCount(Condition.create().eq("del_flag", 0)
                .and().eq("type", type).and().eq("sn_id", resId));
        return count;
    }

    /**
     * institute
     *
     * @param params
     * @return
     */
    @SuppressWarnings("all")
    public CourseDetail courseDetail(Map<String, Object> params, String token, int source) {
        int visitor = TokenUtils.getUserIdByToken(token);
        CourseDetail result = new CourseDetail();
        Course course = courseService.selectOne(
                Condition.create().eq("del_flag", 0).and()
                        .eq("sn_id", String.valueOf(params.get("resourceId"))));
        if (Objects.nonNull(course)) {
            UserInfo userInfo = getUserById(course.getUserId());
            if (Objects.isNull(userInfo)) {
                return null;
            }
            result.setContent(course.getContent());
            result.setCreatedAt(course.getCreatedAt());
            result.setImgUrl(course.getThumbUrl());
            result.setResourceId(course.getSnId());
            result.setTitle(course.getTitle());
            Optional.ofNullable(course.getReadNum()).ifPresent(result::setViewNum);
            result.setAuthor(userInfo.getNickName());
            if (userInfo.getIsArtist() == 1 && StringUtils.isNotBlank(userInfo.getUserName())) {
                result.setAuthor(userInfo.getUserName());
            }
            result.setUserId(userInfo.getUserId());
            result.setAvatar(userInfo.getAvatar());
            result.setDescription(course.getDescription());
            Optional.ofNullable(course.getLikeNum()).ifPresent(result::setLikeNum);
            result.setLike(likeOrNot(visitor, COURSE.getType(), course.getSnId()));
            if (source == 0) {
                processResourceView(course.getSnId(), COURSE.name(),
                        Constants.RESOURCE_VIEW_KEY_MAP.get(COURSE.name()),
                        Constants.RESOURCE_VIEW_SET_KEY_MAP.get(COURSE.name()), visitor);
            }
        }
        return result;
    }

    /**
     * institute
     *
     * @param params
     * @return
     */
    @SuppressWarnings("all")
    public TrainingResult trainingList(Map<String, Object> params) {
        TrainingResult result = new TrainingResult();
        int pageIndex = NumberUtils.toInt(String.valueOf(params.get("pageIndex")));
        int pageSize = NumberUtils.toInt(String.valueOf(params.get("pageSize")));
        int total = trainingClassService.selectCount(
                Condition.create().eq("del_flag", 0));
        Page<TrainingClass> trainingClasses = trainingClassService.selectPage(
                new Page<>(pageIndex, pageSize),
                Condition.create().eq("del_flag", 0).orderDesc(Collections.singletonList("created_at")));
        if (CollectionUtils.isNotEmpty(trainingClasses.getRecords())) {
            List<TrainingResult.Training> trainings = trainingClasses.getRecords()
                    .stream().map(this::convertByTrainingClass).collect(Collectors.toList());
            result.setContents(trainings);
            result.setHasMore(total > (pageIndex * pageSize));
        }
        return result;
    }

    /**
     * institute
     *
     * @param params
     * @return
     */
    @SuppressWarnings("all")
    public TrainingDetail trainingDetail(Map<String, Object> params, String token, int source) {
        int userId = TokenUtils.getUserIdByToken(token);
        int visitor = NumberUtils.toInt(String.valueOf(params.get("userId")));
        TrainingDetail result = new TrainingDetail();
        TrainingClass trainingClass = trainingClassService.selectOne(
                Condition.create().eq("del_flag", 0).and()
                        .eq("sn_id", String.valueOf(params.get("resourceId"))));
        if (Objects.nonNull(trainingClass)) {
            UserInfo userInfo = getUserById(trainingClass.getUserId());
            if (Objects.isNull(userInfo)) {
                return null;
            }
            result.setContent(trainingClass.getContent());
            result.setCreatedAt(trainingClass.getCreatedAt());
            result.setImgUrl(trainingClass.getThumbUrl());
            result.setResourceId(trainingClass.getSnId());
            result.setStatus(trainingClass.getStatus());
            result.setTitle(trainingClass.getTitle());
            result.setUrl(trainingClass.getUrl());
            Optional.ofNullable(trainingClass.getReadNum()).ifPresent(result::setViewNum);
            result.setAuthor(userInfo.getNickName());
            if (userInfo.getIsArtist() == 1 && StringUtils.isNotBlank(userInfo.getUserName())) {
                result.setAuthor(userInfo.getUserName());
            }
            result.setUserId(userInfo.getUserId());
            result.setAvatar(userInfo.getAvatar());
            result.setDescription(trainingClass.getDescription());
            result.setRule(trainingClass.getRule());
            Optional.ofNullable(trainingClass.getLikeNum()).ifPresent(result::setLikeNum);
            Optional.ofNullable(trainingClass.getApplyStopTime()).ifPresent(result::setApplyStopTime);
            Optional.ofNullable(trainingClass.getClassStartTime()).ifPresent(result::setClassStartTime);
            Optional.ofNullable(trainingClass.getClassStopTime()).ifPresent(result::setClassStopTime);
            result.setIsGroup(trainingClass.getIsGroup());
            result.setIsSignUp(trainingClass.getIsSignUp());
            result.setLike(likeOrNot(userId, TRAINING.getType(), trainingClass.getSnId()));
            if (result.getIsGroup() == 0) {
                XslOrderInfoClass xslOrderInfoClass = orderInfoClassService.selectOne(Condition.create().eq("del_flag", 0)
                        .and().eq("class_id", trainingClass.getSnId())
                        .and().eq("assemble_id", 0)
                        .and().eq("user_id", userId)
                        .and().eq("payment_status", 3).last("limit 1"));
                if (Objects.nonNull(xslOrderInfoClass)) {
                    result.setPurchaseStatus(1);
                }
            } else {
                List<AssembleInfo> assembleInfos = assembleInfoService.selectList(Condition.create().eq("del_flag", 0)
                        .and().eq("type", TRAINING.getType()).and().eq("sn_id", trainingClass.getSnId()));
                if (CollectionUtils.isEmpty(assembleInfos)) {
                    // 当前此资源无拼团
                    result.setAssembleGroupId(0);
                    result.setJoinAssemble(0);
                    result.setAssembleTotalCount(0);
                    result.setAssembleCount(0);
                } else {
                    result.setAssembleTotalCount(assembleInfos.stream().mapToInt(AssembleInfo::getCount).sum());
                    // 传入的userId 对应的拼团
                    Assemble visitorAssemble = assembleService.findByCondition(TRAINING.getType(), visitor, trainingClass.getSnId());
                    // 用户自己参与 的拼团
                    Assemble userAssemble = assembleService.findByCondition(TRAINING.getType(), userId, trainingClass.getSnId());
                    if (Objects.nonNull(userAssemble)) {
                        result.setJoinAssemble(1);
                        result.setAssembleGroupId(userAssemble.getAssembleId());
                        result.setAssembleCount(assembleService.selectCount(Condition.create().eq("del_flag", 0)
                                .and().eq("assemble_id", userAssemble.getAssembleId())));
                        result.setSponsorInfo(findSponsorByUserId(userAssemble.getUserId()));
                    } else {
                        result.setJoinAssemble(0);
                        if (Objects.isNull(visitorAssemble)) {
                            // 选举一个当前人数最少的团返回，如果没有返回 0
                            assembleInfos.sort(new Comparator<AssembleInfo>() {
                                @Override
                                public int compare(AssembleInfo o1, AssembleInfo o2) {
                                    return Integer.valueOf(o1.getCount()).compareTo(Integer.valueOf(o2.getCount()));
                                }
                            });
                            result.setAssembleCount(assembleInfos.get(0).getCount());
                            result.setAssembleGroupId(assembleInfos.get(0).getId());
                            result.setSponsorInfo(findSponsorByUserId(assembleInfos.get(0).getUserId()));
                        } else {
                            int assembleCount = assembleService.selectCount(Condition.create().eq("del_flag", 0)
                                    .and().eq("assemble_Id", visitorAssemble.getAssembleId()));
                            result.setAssembleCount(assembleCount);
                            result.setAssembleGroupId(visitorAssemble.getAssembleId());
                            result.setSponsorInfo(findSponsorByUserId(visitorAssemble.getUserId()));
                        }
                    }
                }
            }
            if (source == 0) {
                processResourceView(trainingClass.getSnId(), TRAINING.name(),
                        Constants.RESOURCE_VIEW_KEY_MAP.get(TRAINING.name()),
                        Constants.RESOURCE_VIEW_SET_KEY_MAP.get(TRAINING.name()), userId);
            }
        }
        return result;
    }

    /**
     * institute
     *
     * @param params
     * @return
     */
    @SuppressWarnings("all")
    public ExhibitionDetail exhibitionDetail(Map<String, Object> params, String token, int source) {
        int userId = TokenUtils.getUserIdByToken(token);
        int visitor = NumberUtils.toInt(String.valueOf(params.get("userId")));
        ExhibitionDetail result = new ExhibitionDetail();
        Exhibition exhibition = exhibitionService.selectOne(
                Condition.create().eq("del_flag", 0).and()
                        .eq("sn_id", String.valueOf(params.get("resourceId"))));
        if (Objects.nonNull(exhibition)) {
            UserInfo userInfo = getUserById(exhibition.getUserId());
            if (Objects.isNull(userInfo)) {
                return null;
            }
            result.setContent(exhibition.getContent());
            result.setCreatedAt(exhibition.getCreatedAt());
            result.setImgUrl(exhibition.getThumbUrl());
            result.setResourceId(exhibition.getSnId());
            result.setStatus(exhibition.getStatus());
            result.setTitle(exhibition.getTitle());
            result.setUrl(exhibition.getUrl());
            Optional.ofNullable(exhibition.getReadNum()).ifPresent(result::setViewNum);
            result.setAuthor(userInfo.getNickName());
            if (userInfo.getIsArtist() == 1 && StringUtils.isNotBlank(userInfo.getUserName())) {
                result.setAuthor(userInfo.getUserName());
            }
            result.setUserId(userInfo.getUserId());
            result.setAvatar(userInfo.getAvatar());
            result.setDescription(exhibition.getDescription());
            result.setRule(exhibition.getRule());
            Optional.ofNullable(exhibition.getLikeNum()).ifPresent(result::setLikeNum);
            Optional.ofNullable(exhibition.getApplyStopTime()).ifPresent(result::setApplyStopTime);
            Optional.ofNullable(exhibition.getClassStartTime()).ifPresent(result::setClassStartTime);
            Optional.ofNullable(exhibition.getClassStopTime()).ifPresent(result::setClassStopTime);
            result.setIsGroup(exhibition.getIsGroup());
            result.setIsSignUp(exhibition.getIsSignUp());
            result.setLike(likeOrNot(userId, EXHIBITION.getType(), exhibition.getSnId()));

            if (result.getIsGroup() == 0) {
                XslOrderInfoExhibition xslOrderInfoExhibition = orderInfoExhibitionService.selectOne(Condition.create().eq("del_flag", 0)
                        .and().eq("exhibition_id", exhibition.getSnId())
                        .and().eq("assemble_id", 0)
                        .and().eq("user_id", userId)
                        .and().eq("payment_status", 3).last("limit 1"));
                if (Objects.nonNull(xslOrderInfoExhibition)) {
                    result.setPurchaseStatus(1);
                }
            } else {
                List<AssembleInfo> assembleInfos = assembleInfoService.selectList(Condition.create().eq("del_flag", 0)
                        .and().eq("type", EXHIBITION.getType()).and().eq("sn_id", exhibition.getSnId()));
                if (CollectionUtils.isEmpty(assembleInfos)) {
                    // 当前此资源无拼团
                    result.setAssembleGroupId(0);
                    result.setJoinAssemble(0);
                    result.setAssembleTotalCount(0);
                    result.setAssembleCount(0);
                } else {
                    result.setAssembleTotalCount(assembleInfos.stream().mapToInt(AssembleInfo::getCount).sum());
                    // 传入的userId 对应的拼团
                    Assemble visitorAssemble = assembleService.findByCondition(EXHIBITION.getType(), visitor, exhibition.getSnId());
                    // 用户自己参与 的拼团
                    Assemble userAssemble = assembleService.findByCondition(EXHIBITION.getType(), userId, exhibition.getSnId());
                    if (Objects.nonNull(userAssemble)) {
                        result.setJoinAssemble(1);
                        result.setAssembleGroupId(userAssemble.getAssembleId());
                        result.setAssembleCount(assembleService.selectCount(Condition.create().eq("del_flag", 0)
                                .and().eq("assemble_id", userAssemble.getAssembleId())));
                        result.setSponsorInfo(findSponsorByUserId(userAssemble.getUserId()));
                    } else {
                        result.setJoinAssemble(0);
                        if (Objects.isNull(visitorAssemble)) {
                            // 选举一个当前人数最少的团返回，如果没有返回 0
                            assembleInfos.sort(new Comparator<AssembleInfo>() {
                                @Override
                                public int compare(AssembleInfo o1, AssembleInfo o2) {
                                    return Integer.valueOf(o1.getCount()).compareTo(Integer.valueOf(o2.getCount()));
                                }
                            });
                            result.setAssembleCount(assembleInfos.get(0).getCount());
                            result.setAssembleGroupId(assembleInfos.get(0).getId());
                            result.setSponsorInfo(findSponsorByUserId(assembleInfos.get(0).getUserId()));
                        } else {
                            int assembleCount = assembleService.selectCount(Condition.create().eq("del_flag", 0)
                                    .and().eq("assemble_Id", visitorAssemble.getAssembleId()));
                            result.setAssembleCount(assembleCount);
                            result.setAssembleGroupId(visitorAssemble.getAssembleId());
                            result.setSponsorInfo(findSponsorByUserId(visitorAssemble.getUserId()));
                        }
                    }
                }
            }

            if (source == 0) {
                processResourceView(exhibition.getSnId(), EXHIBITION.name(),
                        Constants.RESOURCE_VIEW_KEY_MAP.get(EXHIBITION.name()),
                        Constants.RESOURCE_VIEW_SET_KEY_MAP.get(EXHIBITION.name()), userId);
            }
        }

        return result;
    }

    @SuppressWarnings("all")
    public List<String> searchHot() {
        OperationConfig searchConfig = operationConfigService.selectOne(
                Condition.create().eq("ref", ContentTypeEnum.SEARCH.getType())
                        .and().eq("del_flag", 0));
        if (Objects.nonNull(searchConfig)) {
            return Lists.newArrayList(searchConfig.getResourceIds().split(","));
        }
        return Collections.emptyList();
    }

    /**
     * home page search
     *
     * @param params
     * @return
     */
    @SuppressWarnings("all")
    public List<SearchResult> homePageSearch(Map<String, Object> params) {
        List<SearchResult> searchResults = Lists.newArrayList();
        String keywords = String.valueOf(params.get("keywords"));
        Integer articleNum = articleService.findNonVideoTagArticleCount(keywords);
        if (Objects.nonNull(articleNum) && articleNum > 0) {
            SearchResult articleSearch = new SearchResult();
            articleSearch.setName(SearchTypeEnum.ARTICLE.getName());
            articleSearch.setType(SearchTypeEnum.ARTICLE.getType());
            searchResults.add(articleSearch);
        }
        int artistNum = userInfoService.selectCount(
                Condition.create().eq("del_flag", 0).andNew()
                        .like("nick_name", keywords).or().like("user_name", keywords));
        if (artistNum > 0) {
            SearchResult artistSearch = new SearchResult();
            artistSearch.setName(SearchTypeEnum.USER.getName());
            artistSearch.setType(SearchTypeEnum.USER.getType());
            searchResults.add(artistSearch);
        }
        Integer videoCount = articleService.findVideoTagArticleCount(keywords);
        if (Objects.nonNull(videoCount) && videoCount > 0) {
            SearchResult commoditySearch = new SearchResult();
            commoditySearch.setName(SearchTypeEnum.VIDEO.getName());
            commoditySearch.setType(SearchTypeEnum.VIDEO.getType());
            searchResults.add(commoditySearch);
        }
        return searchResults;
    }

    /**
     * home page search
     *
     * @param params
     * @return
     */
    public SearchListResult searchList(Map<String, Object> params, String token) {
        SearchListResult searchResult;
        int pageIndex = NumberUtils.toInt(String.valueOf(params.get("pageIndex")));
        int pageSize = NumberUtils.toInt(String.valueOf(params.get("pageSize")));
        int userId = TokenUtils.getUserIdByToken(token);
        switch (SearchTypeEnum.ofType(NumberUtils.toInt(String.valueOf(params.get("type"))))) {
            case ARTICLE:
                searchResult =
                        buildSearchResult(userId, SearchTypeEnum.ARTICLE.getType(), pageIndex, pageSize, String.valueOf(params.get("keywords")));
                break;
            case USER:
                searchResult =
                        buildSearchResult(userId, SearchTypeEnum.USER.getType(), pageIndex, pageSize, String.valueOf(params.get("keywords")));
                break;
            case VIDEO:
                searchResult =
                        buildSearchResult(userId, SearchTypeEnum.VIDEO.getType(), pageIndex, pageSize, String.valueOf(params.get("keywords")));
                break;
            default:
                searchResult = new SearchListResult();
                break;
        }
        return searchResult;
    }

    /**
     * resource detail
     *
     * @param params
     * @return
     */
    @SuppressWarnings("all")
    public ArticleResult getArticleDetail(Map<String, Object> params, String token, int source) {
        ArticleResult resourceResult = new ArticleResult();
        String articleResId = String.valueOf(params.get("resourceId"));
        Article article = articleService.selectOne(Condition.create().eq("sn_id", articleResId).and().eq("del_flag", 0));
        if (Objects.isNull(article)) {
            throw new BizException(ResponseCodeEnum.PARAM_ERROR);
        }
        UserInfo author = getUserById(article.getUserId());
        if (Objects.isNull(author)) {
            return resourceResult;
        }
        int userId = TokenUtils.getUserIdByToken(token);
        if (userId > 0 && userId != article.getUserId() && article.getStatus() != ArticleStatusEnum.ONLINE.getStatus()) {
            return null;
        }
        resourceResult.setAuthor(author.getNickName());
        if (author.getIsArtist() == 1 && StringUtils.isNotBlank(author.getUserName())) {
            resourceResult.setAuthor(author.getUserName());
        }
        resourceResult.setTitle(article.getTitle());
        resourceResult.setAvatar(author.getAvatar());
        resourceResult.setUserId(author.getUserId());
        resourceResult.setFrom(article.getFrom());
        resourceResult.setCreatedAt(article.getCreatedAt());
        resourceResult.setResourceId(articleResId);
        Optional.ofNullable(article.getReadNum()).ifPresent(resourceResult::setViewNum);
        Optional.ofNullable(article.getLikeNum()).ifPresent(resourceResult::setLikeNum);
        resourceResult.setType(article.getType());
        resourceResult.setImgUrl(article.getThumbUrl());
        resourceResult.setContent(article.getContent());
        resourceResult.setStatus(article.getStatus());
        resourceResult.setDescription(article.getDescription());
        resourceResult.setVideoUrl(article.getVideoUrl());
        if (article.getFrom() == 0) {
            try {
                resourceResult.setContent(JSON.parseArray(article.getContent(), ArticleDetail.class));
            } catch (Exception e) {
                LOGGER.error("convert content error", e);
            }
        }
        resourceResult.setLike(likeOrNot(userId, ARTICLE.getType(), article.getSnId()));
        resourceResult.setFocus(focusById(userId, article.getUserId()));
        if (source == 0) {
            processResourceView(articleResId, ResourceTypeEnum.ARTICLE.name(),
                    Constants.RESOURCE_VIEW_KEY_MAP.get(ResourceTypeEnum.ARTICLE.name()),
                    Constants.RESOURCE_VIEW_SET_KEY_MAP.get(ResourceTypeEnum.ARTICLE.name()), userId);
        }
        List<ArticleResource> personalArticles = getPersonalRecommend(userId);
        if (CollectionUtils.isNotEmpty(personalArticles)) {
            Random random = new Random();
            Set<ArticleResource> suggests = Sets.newHashSet();
            for (int i = 0; i < 10; i++) {
                ArticleResource randArticle = personalArticles.get(random.nextInt(personalArticles.size()));
                if (randArticle.getResourceId().equalsIgnoreCase(article.getSnId())) {
                    continue;
                }
                if (suggests.contains(randArticle)) {
                    continue;
                }
                suggests.add(randArticle);
                if (suggests.size() >= 5) {
                    break;
                }
            }
            if (CollectionUtils.isNotEmpty(suggests)) {
                for (ArticleResource suggestion : suggests) {
                    suggestion.setLike(likeOrNot(userId, ARTICLE.getType(), article.getSnId()));
                    suggestion.setFocus(focusById(userId, article.getUserId()));
                }
                resourceResult.getSuggestions().addAll(suggests);
            }
        }
        return resourceResult;
    }

    @SuppressWarnings("all")
    public void likeResource(Map<String, Object> params, String token) {
        Date now = DatetimeUtils.now();
        String resourceId = String.valueOf(params.get("resourceId"));
        int type = NumberUtils.toInt(String.valueOf(params.get("type")));
        int action = NumberUtils.toInt(String.valueOf(params.get("action")));
        int userId = TokenUtils.getUserIdByToken(token);
        if (userId == 0) {
            throw new BizException(ResponseCodeEnum.INVALID_TOKEN);
        }
        LikeRecord likeRecord = new LikeRecord();
        likeRecord.setResourceId(resourceId);
        likeRecord.setType(type);
        likeRecord.setUserId(userId);
        if (Constants.RESOURCE_LIKE == action) {
            likeRecord.setCreatedAt(now);
            likeRecord.setDelFlag(0);
        } else if (Constants.RESOURCE_DISLIKE == action) {
            likeRecord.setUpdatedAt(now);
            likeRecord.setDelFlag(1);
        } else {
            throw new BizException(ResponseCodeEnum.PARAM_ERROR);
        }
        likeRecordService.replace(likeRecord);
        String resourceTypeName = ResourceTypeEnum.ofType(type).name();
        processResourceLikeAction(resourceId, resourceTypeName, action,
                Constants.RESOURCE_LIKE_KEY_MAP.get(resourceTypeName),
                Constants.RESOURCE_LIKE_SET_KEY_MAP.get(resourceTypeName));
    }

    @SuppressWarnings("all")
    public String publish(Map<String, Object> bizParams, String token, int from) {
        String resourceId;
        int userId = TokenUtils.getUserIdByToken(token);
        Article article = new Article();
        Date now = DatetimeUtils.now();
        boolean addAction = false;
        if (Objects.isNull(bizParams.get("resourceId"))
                || StringUtils.isBlank(String.valueOf(bizParams.get("resourceId")))) {
            resourceId = UUID.randomUUID().toString().replace("-", "").substring(0, 32);
            article.setSnId(resourceId);
            addAction = true;
        } else {
            article.setSnId(String.valueOf(bizParams.get("resourceId")));
            resourceId = article.getSnId();
            Article articleDb = articleService.selectOne(Condition.create().eq("sn_id", resourceId));
            if (articleDb.getIsDraft() == 0 && !articleDb.getStatus().equals(ArticleStatusEnum.OFFLINE.getStatus())) {
                throw new BizException(ResponseCodeEnum.FORBIDDEN_MODIFY_FILE);
            }
        }
        String content = JSON.toJSONString((bizParams.get("blocks")));
        if (StringUtils.isNotBlank(cdnHost)) {
            content = content.replaceAll(ossManager.getOssHost(), cdnHost);
        }
        article.setContent(content);
        article.setIsDraft("true".equalsIgnoreCase(String.valueOf(bizParams.get("draft"))) ? 1 : 0);
        article.setFrom(from);
        article.setUserId(userId);
        article.setTitle(String.valueOf(bizParams.get("title")));
        article.setMode(NumberUtils.toInt(String.valueOf(bizParams.get("mode"))));
        article.setType(NumberUtils.toInt(String.valueOf(bizParams.get("type"))));
        if (Objects.nonNull(bizParams.get("thumbUrl"))) {
            article.setThumbUrl(String.valueOf(bizParams.get("thumbUrl")));
        }
        if (Objects.nonNull(bizParams.get("videoUrl"))) {
            article.setVideoUrl(String.valueOf(bizParams.get("videoUrl")));
        }
        if (Objects.nonNull(bizParams.get("description"))) {
            article.setDescription(String.valueOf(bizParams.get("description")));
        }
        if (Objects.nonNull(bizParams.get("keywords"))) {
            article.setKeywords(String.valueOf(bizParams.get("keywords")));
        }
        if (StringUtils.isBlank(article.getThumbUrl())) {
            article.setThumbUrl(extractImgUrl(content));
        }
        boolean draftFull = checkDraft(userId);
        if (addAction) {
            if (article.getIsDraft() == Constants.DRAFT && draftFull) {
                return Constants.DRAFT_FULL;
            }
            article.setDelFlag(0);
            article.setReadNum(0);
            article.setLikeNum(0);
            article.setCreatedAt(now);
            articleService.insert(article);
        } else {
            article.setStatus(ArticleStatusEnum.DEFAULT.getStatus());
            article.setUpdatedAt(now);
            articleService.update(article, Condition.create().eq("sn_id", resourceId));
        }
        return resourceId;
    }

    @SuppressWarnings("all")
    public void deleteArticle(Map<String, Object> bizParams, String token) {
        Article articleDb = articleService.selectOne(
                Condition.create().eq("sn_id", String.valueOf(bizParams.get("resourceId"))));
        if (articleDb.getIsDraft() == 0 && (articleDb.getStatus().equals(ArticleStatusEnum.AUDITING.getStatus())
                || articleDb.getStatus().equals(ArticleStatusEnum.DEFAULT.getStatus()))) {
            throw new BizException(ResponseCodeEnum.FORBIDDEN_REMOVE_FILE);
        }
        int userId = TokenUtils.getUserIdByToken(token);
        Article article = new Article();
        Date now = DatetimeUtils.now();
        article.setUpdatedAt(now);
        article.setDelFlag(1);
        articleService.update(article,
                Condition.create().eq("sn_id", String.valueOf(bizParams.get("resourceId"))));
    }

    @Scheduled(initialDelay = 1 * 1000, fixedDelay = 10 * 1000)
    @SuppressWarnings("all")
    public void buildRecommendPeriodly() {
        long start = System.currentTimeMillis();
        List<ArticleResource> backUpUnifiedArticleResources = Lists.newArrayList();
        OperationConfig recommendOperationConfig = operationConfigService.selectOne(
                Condition.create().eq("ref", ContentTypeEnum.RECOMMEND.getType())
                        .and().eq("type", ARTICLE.getType())
                        .and().eq("del_flag", 0));
        if (Objects.nonNull(recommendOperationConfig) && StringUtils.isNotBlank(recommendOperationConfig.getResourceIds())) {
            List<String> hotRecommendResourceIds = Lists.newArrayList(recommendOperationConfig.getResourceIds().split(","));
            if (CollectionUtils.isNotEmpty(hotRecommendResourceIds)) {
                List<Article> hotArticles = articleService.selectList(
                        Condition.create().in("id", hotRecommendResourceIds).and().eq("del_flag", 0));
                if (CollectionUtils.isNotEmpty(hotArticles)) {
                    backUpUnifiedArticleResources.addAll(hotArticles.stream().map(this::convertByArticle).collect(Collectors.toList()));
                }
            }
        }
        List<Article> recommendArticles = articleService.selectList(
                Condition.create().eq("is_recommend", 1)
                        .and().eq("status", ArticleStatusEnum.ONLINE.getStatus())
                        .and().eq("del_flag", 0)
                        .orderDesc(Lists.newArrayList("rank", "created_at")));
        if (CollectionUtils.isNotEmpty(recommendArticles)) {
            for (Article recommendArticle : recommendArticles) {
                ArticleResource recommendArticleResource = convertByArticle(recommendArticle);
                if (!backUpUnifiedArticleResources.contains(recommendArticleResource)) {
                    backUpUnifiedArticleResources.add(recommendArticleResource);
                }
            }
        }
        unifiedArticleResources.clear();
        unifiedArticleResources = backUpUnifiedArticleResources;
        // build personal recommend
        Set<Integer> customUsers = findUserNeedCustomContent();
        if (CollectionUtils.isNotEmpty(customUsers)) {
            for (Integer userId : customUsers) {
                processPersonalRecommends(userId, getPersonalBanResources(userId), getPersonalBanAuthors(userId));
            }
        }
        LOGGER.info("buildRecommendPeriodly cost:{}", System.currentTimeMillis() - start);
    }

    @Scheduled(initialDelay = 2 * 1000, fixedDelay = 15 * 1000)
    @SuppressWarnings("all")
    public void buildTagArticlePeriodly() {
        long start = System.currentTimeMillis();
        for (ArticleTagEnum value : ArticleTagEnum.values()) {
            List<Article> articles = getArticleByTags(value.getType());
            if (CollectionUtils.isEmpty(articles)) {
                tagArticleMap.put(value, Collections.emptyList());
                continue;
            }
            List<ArticleResource> articleResources = articles.stream()
                    .map(this::convertByArticle).collect(Collectors.toList());
            tagArticleMap.put(value, articleResources);
        }
        // build personal tag articles
        Set<Integer> customUsers = findUserNeedCustomContent();
        if (CollectionUtils.isNotEmpty(customUsers)) {
            for (Integer userId : customUsers) {
                processPersonalTagArticles(userId, getPersonalBanResources(userId), getPersonalBanAuthors(userId));
            }
        }
        LOGGER.info("buildTagArticlePeriodly cost:{}", System.currentTimeMillis() - start);
    }

    @Scheduled(initialDelay = 3 * 1000, fixedDelay = 10 * 1000)
    @SuppressWarnings("all")
    public void buildFocusPeriodly() {
        long start = System.currentTimeMillis();
        List<UserFocusAssociation> focusAssociations =
                focusAssociationService.selectList(Condition.create().eq("del_flag", 0));
        if (CollectionUtils.isEmpty(focusAssociations)) {
            return;
        }
        List<Integer> userIds = focusAssociations.stream()
                .map(UserFocusAssociation::getUserId).collect(Collectors.toList());
        for (Integer userId : userIds) {
            buildFocusByUserId(userId);
        }
        LOGGER.info("buildFocusPeriodly cost:{}", System.currentTimeMillis() - start);
    }

    @Scheduled(initialDelay = 3 * 1000, fixedDelay = 10 * 1000)
    @SuppressWarnings("all")
    public void buildPersonalStatistic() {
        long start = System.currentTimeMillis();
        List<UserInfo> users = userInfoService.selectList(Condition.create().eq("del_flag", 0));
        if (CollectionUtils.isEmpty(users)) {
            return;
        }
        for (UserInfo user : users) {
            PersonalStatistics personalStatistics = getPersonalStatistic(user.getUserId());
            UserInfo updateUserInfo = new UserInfo();
            updateUserInfo.setUpdatedAt(DatetimeUtils.now());
            updateUserInfo.setFansNum(personalStatistics.getFanNum());
            updateUserInfo.setFocusNum(personalStatistics.getFocusNum());
            updateUserInfo.setViewNum(personalStatistics.getViewNum());
            userInfoService.update(updateUserInfo, Condition.create().eq("user_id", user.getUserId()));
        }
        LOGGER.info("buildPersonalStatistic cost:{}", System.currentTimeMillis() - start);
    }

    @SuppressWarnings("all")
    @Async
    public void buildFocusByUserId(int userId) {
        List<Integer> focusArtistIds = focusAssociationService.findFocusArtists(userId);
        if (CollectionUtils.isEmpty(focusArtistIds)) {
            return;
        }
        List<Article> focusArticles = articleService.findTagArticles(focusArtistIds);
        // filter those articles which the user doesnot want to see
        focusArticles = filterArticles(focusArticles, userId);
        focusArticleMap.put(String.valueOf(userId), focusArticles.stream()
                .map(f -> convertByArticle(f, userId)).collect(Collectors.toList()));
    }

    @SuppressWarnings("all")
    public PersonalBriefResult personalPage(Map<String, Object> bizParam, String token) {
        int userId = 0;
        int loginUserId = TokenUtils.getUserIdByToken(token);
        if (MapUtils.isNotEmpty(bizParam) && Objects.nonNull(bizParam.get("userId"))) {
            userId = NumberUtils.toInt(String.valueOf(bizParam.get("userId")));
        } else {
            userId = loginUserId;
        }
        if (userId == 0) {
            throw new BizException(ResponseCodeEnum.PARAM_ERROR);
        }
        UserInfo userInfo = getUserById(userId);
        if (Objects.isNull(userInfo)) {
            throw new BizException(ResponseCodeEnum.PARAM_ERROR);
        }
        PersonalBriefResult result = new PersonalBriefResult();
        result.setNickName(userInfo.getNickName());
        result.setAvatar(userInfo.getAvatar());
        result.setIntroduction(userInfo.getIntro());
        result.setMotto(userInfo.getMotto());
        if (StringUtils.isBlank(result.getMotto())) {
            result.setMotto("画和远方，有你有我！");
        }
        result.setBgImgUrl(userInfo.getSpaceImgUrl());
        result.setDegree(DegreeEnum.ofType(userInfo.getDegree()).getName());
        result.setLevel(DegreeEnum.ofType(userInfo.getDegree()).getType());
        if (Objects.nonNull(userInfo.getPoints())) {
            result.setPoints(userInfo.getPoints());
        }
        PersonalStatistics statistics = getPersonalStatistic(userId);
        result.setFanNum(statistics.getFanNum());
        result.setFocusNum(statistics.getFocusNum());
        result.setViewNum(statistics.getViewNum());
        result.setMsgUnReadNum(getUnReadMsgByUserId(userId));
        result.setCouponUnUsedNum(getUnUsedCouponByUserId(userId));
        int draftNum = articleService.selectCount(
                Condition.create().eq("user_id", userId)
                        .and().eq("is_draft", 1)
                        .and().eq("del_flag", 0));
        result.setDraftNum(draftNum);
        if (userId != loginUserId) {
            result.setFocus(focusById(loginUserId, userId));
        }
        if (userInfo.getIsArtist() == 1 && StringUtils.isNotBlank(userInfo.getUserName())) {
            result.setNickName(userInfo.getUserName());
            result.setArtist(true);
        }
        return result;
    }

    @SuppressWarnings("all")
    public RecommendResult personalPageArticles(Map<String, Object> bizParam, String token) {
        int pageIndex = 0;
        int pageSize = 0;
        if (Objects.isNull(bizParam.get("pageIndex"))
                || Objects.isNull(bizParam.get("pageSize"))
                || Objects.isNull(bizParam.get("type"))) {
            throw new BizException(ResponseCodeEnum.PARAM_ERROR);
        }
        pageIndex = NumberUtils.toInt(String.valueOf(bizParam.get("pageIndex")));
        pageSize = NumberUtils.toInt(String.valueOf(bizParam.get("pageSize")));
        int userId = 0;
        int loginUserId = TokenUtils.getUserIdByToken(token);
        if (MapUtils.isNotEmpty(bizParam) && Objects.nonNull(bizParam.get("userId"))) {
            userId = NumberUtils.toInt(String.valueOf(bizParam.get("userId")));
        } else {
            userId = loginUserId;
        }
        if (userId == 0) {
            throw new BizException(ResponseCodeEnum.PARAM_ERROR);
        }
        List<Integer> statuses = ArticleStatusEnum.personalViewStatus;
        if (userId != loginUserId) {
            statuses = Collections.singletonList(ArticleStatusEnum.ONLINE.getStatus());
        }
        RecommendResult recommendResult = new RecommendResult();
        int total = articleService.selectCount(
                Condition.create().eq("user_id", userId)
                        .and().eq("type", NumberUtils.toInt(String.valueOf(bizParam.get("type"))))
                        .and().eq("del_flag", 0)
                        .and().eq("is_draft", 0)
                        .and().in("status", statuses));

        Page<Article> articles = articleService.selectPage(new Page<>(pageIndex, pageSize),
                Condition.create().eq("user_id", userId)
                        .and().eq("type", NumberUtils.toInt(String.valueOf(bizParam.get("type"))))
                        .and().eq("del_flag", 0)
                        .and().in("status", statuses)
                        .and().eq("is_draft", 0)
                        .orderBy("created_at", false));
        recommendResult.setHasMore(false);
        if (CollectionUtils.isEmpty(articles.getRecords())) {
            return recommendResult;
        }
        recommendResult.setHasMore(total > (pageIndex * pageSize));
        List<ArticleResource> articleResources = articles.getRecords().stream()
                .map(this::convertByArticle).collect(Collectors.toList());
        for (ArticleResource articleResource : articleResources) {
            if (ArticleStatusEnum.OFFLINE.getStatus() == articleResource.getStatus()
                    && StringUtils.isBlank(articleResource.getFailReason())) {
                articleResource.setFailReason(Constants.AUDIT_FAIL_REASONN);
            }
        }
        recommendResult.setContents(articleResources);
        return recommendResult;
    }

    @SuppressWarnings("all")
    public RelationResult personalRelation(Map<String, Object> bizParam, String token) {
        RelationResult relationResult = null;
        int userId = TokenUtils.getUserIdByToken(token);
        int pageOwner = 0;
        if (Objects.nonNull(bizParam.get("userId"))) {
            pageOwner = NumberUtils.toInt(String.valueOf(bizParam.get("userId")));
        }
        if (pageOwner == 0) {
            pageOwner = userId;
        }
        int pageIndex = NumberUtils.toInt(String.valueOf(bizParam.get("pageIndex")));
        int pageSize = NumberUtils.toInt(String.valueOf(bizParam.get("pageSize")));
        switch (RelationTypeEnum.ofType(NumberUtils.toInt(String.valueOf(bizParam.get("type"))))) {
            case FOCUS:
                relationResult = getRelationByType(userId, pageOwner, pageIndex, pageSize, RelationTypeEnum.FOCUS);
                break;
            case FAN:
                relationResult = getRelationByType(userId, pageOwner, pageIndex, pageSize, RelationTypeEnum.FAN);
                break;
            default:
                relationResult = new RelationResult();
                break;
        }
        return relationResult;
    }

    @SuppressWarnings("all")
    public Map<String, Object> updateCheck(Map<String, Object> bizParams) {
        VersionControl versionControl = versionControlService.selectOne(
                Condition.create().eq("del_flag", 0)
                        .and().eq("os", NumberUtils.toInt(String.valueOf(bizParams.get("os")))));
        if (Objects.isNull(versionControl)) {
            return null;
        } else if (MathUtils.validVersionUpgrade(
                String.valueOf(bizParams.get("versionName")), versionControl.getUpdateVersionName())) {
            Map<String, Object> updateCheck = Maps.newHashMap();
            updateCheck.put("versionName", versionControl.getUpdateVersionName());
            updateCheck.put("changeLog", versionControl.getUpdateLog());
            updateCheck.put("downloadUrl", versionControl.getUpdateVersionUrl());
            updateCheck.put("forceUpdate", versionControl.getForceUpdate());
            return updateCheck;
        } else {
            return null;
        }
    }

    public List<ComplainReason> complainReason() {
        return ComplainReason.complains;
    }

    public void complain(Map<String, Object> bizParams, String token) {
        int userId = TokenUtils.getUserIdByToken(token);
        ComplainRecord complainRecord = new ComplainRecord();
        complainRecord.setUserId(userId);
        complainRecord.setCreatedAt(DatetimeUtils.now());
        complainRecord.setDelFlag(0);
        complainRecord.setType(NumberUtils.toInt(String.valueOf(bizParams.get("type"))));
        complainRecord.setRejectReason(NumberUtils.toInt(String.valueOf(bizParams.get("reason"))));
        complainRecord.setRejectMessage(ComplainReasonEnum.valueOfReason(complainRecord.getRejectReason()).getDesc());
        complainRecord.setResId(String.valueOf(bizParams.get("resourceId")));
        complainRecordService.insert(complainRecord);
    }

    public void banResource(Map<String, Object> bizParams, String token) {
        int userId = TokenUtils.getUserIdByToken(token);
        UserRejectRecord userRejectRecord = new UserRejectRecord();
        userRejectRecord.setUserId(userId);
        userRejectRecord.setCreatedAt(DatetimeUtils.now());
        userRejectRecord.setDelFlag(0);
        userRejectRecord.setResId(String.valueOf(bizParams.get("resourceId")));
        userRejectRecord.setType(NumberUtils.toInt(String.valueOf(bizParams.get("type"))));
        userRejectRecordService.insert(userRejectRecord);
        processPersonalContent(userId, Collections.singleton(userRejectRecord.getResId()));
    }

    public void banAuthor(Map<String, Object> bizParams, String token) {
        int userId = TokenUtils.getUserIdByToken(token);
        UserBanRecord userBanRecord = new UserBanRecord();
        userBanRecord.setAuthorId(NumberUtils.toInt(String.valueOf(bizParams.get("authorId"))));
        userBanRecord.setCreatedAt(DatetimeUtils.now());
        int action = NumberUtils.toInt(String.valueOf(bizParams.get("action")));
        userBanRecord.setDelFlag(0);
        if (action == 2) {
            userBanRecord.setDelFlag(1);
        }
        userBanRecord.setUserId(userId);
        userBanRecordService.replace(userBanRecord);
        processPersonalContent(userId, Collections.emptySet());
    }

    @SuppressWarnings("all")
    public BanAuthorResult blacklist(Map<String, Object> params, String token) {
        int userId = TokenUtils.getUserIdByToken(token);
        int pageIndex = 0;
        int pageSize = 0;
        if (Objects.nonNull(params.get("pageIndex"))) {
            pageIndex = NumberUtils.toInt(String.valueOf(params.get("pageIndex")));
        }
        if (Objects.nonNull(params.get("pageSize"))) {
            pageSize = NumberUtils.toInt(String.valueOf(params.get("pageSize")));
        }
        checkPageCondition(pageIndex, pageSize);
        BanAuthorResult banAuthorResult = new BanAuthorResult();
        int totalBanAuthors = userBanRecordService
                .selectCount(Condition.create().eq("user_id", userId).and().eq("del_flag", 0));
        if (totalBanAuthors == 0) {
            return banAuthorResult;
        }
        banAuthorResult.setHasMore(totalBanAuthors > (pageIndex * pageSize));
        Page<UserBanRecord> userBanRecordPage = userBanRecordService.selectPage(new Page<>(pageIndex, pageSize)
                , Condition.create().eq("user_id", userId).and().eq("del_flag", 0));
        if (CollectionUtils.isNotEmpty(userBanRecordPage.getRecords())) {
            List<Integer> userIds = userBanRecordPage.getRecords()
                    .stream().map(UserBanRecord::getAuthorId).collect(Collectors.toList());
            for (Integer id : userIds) {
                UserInfo userInfo = getUserById(id);
                if (Objects.isNull(userInfo)) {
                    continue;
                }
                banAuthorResult.getContents().add(convertByUser(userInfo));
            }
        }
        return banAuthorResult;
    }

    @SuppressWarnings("all")
    public PointsHomeResult pointsHome(Map<String, Object> params, String token) {
        int userId = TokenUtils.getUserIdByToken(token);
        int pageIndex = 0;
        int pageSize = 0;
        if (Objects.nonNull(params.get("pageIndex"))) {
            pageIndex = NumberUtils.toInt(String.valueOf(params.get("pageIndex")));
        }
        if (Objects.nonNull(params.get("pageSize"))) {
            pageSize = NumberUtils.toInt(String.valueOf(params.get("pageSize")));
        }
        checkPageCondition(pageIndex, pageSize);
        PointsHomeResult pointsHomeResult = new PointsHomeResult();
        pointsHomeResult.setPoints(getUserById(userId).getPoints());
        pointsHomeResult.setRank(getMemberPointRank(userId, pointsHomeResult.getPoints()));
        Map<String, Object> contributionDesc = JackJsonUtil.toBean(cacheHolder.getGeneralKeyValue(Constants.CONTRIBUTION_DESC),
                new com.fasterxml.jackson.core.type.TypeReference<Map<String, Object>>() {
                });

        pointsHomeResult.setRule(String.valueOf(contributionDesc.get("rule")));
        pointsHomeResult.setUsageRule(String.valueOf(contributionDesc.get("usage")));
        PointsHomeResult.IncentiveBean incentiveBean = new PointsHomeResult.IncentiveBean();
        incentiveBean.setDesc(String.valueOf(contributionDesc.get("incentive_desc")));
        incentiveBean.setName(String.valueOf(contributionDesc.get("incentive_item")));
        incentiveBean.setImgUrl(String.valueOf(contributionDesc.get("thumb_url")));
        pointsHomeResult.setIncentive(Lists.newArrayList(incentiveBean));
        pointsHomeResult.setHasMore((pageIndex * pageSize) < BackendSyncTask.memberPointsRankList.size());
        if (((pageIndex - 1) * pageSize) < BackendSyncTask.memberPointsRankList.size()) {
            pointsHomeResult.setRankList(BackendSyncTask.memberPointsRankList
                    .subList((pageIndex - 1) * pageSize,
                            (pageIndex * pageSize) > BackendSyncTask.memberPointsRankList.size()
                                    ? BackendSyncTask.memberPointsRankList.size() : (pageIndex * pageSize)));
        }
        return pointsHomeResult;
    }

    @SuppressWarnings("all")
    public PointsContributionResult pointsContribution(Map<String, Object> params) {
        int pageIndex = 0;
        int pageSize = 0;
        if (Objects.nonNull(params.get("pageIndex"))) {
            pageIndex = NumberUtils.toInt(String.valueOf(params.get("pageIndex")));
        }
        if (Objects.nonNull(params.get("pageSize"))) {
            pageSize = NumberUtils.toInt(String.valueOf(params.get("pageSize")));
        }
        checkPageCondition(pageIndex, pageSize);
        PointsContributionResult pointsHomeResult = new PointsContributionResult();
        pointsHomeResult.setHasMore((pageIndex * pageSize) < BackendSyncTask.memberPointsRankList.size());
        if (((pageIndex - 1) * pageSize) < BackendSyncTask.memberPointsRankList.size()) {
            pointsHomeResult.setRankList(BackendSyncTask.memberPointsRankList
                    .subList((pageIndex - 1) * pageSize,
                            (pageIndex * pageSize) > BackendSyncTask.memberPointsRankList.size()
                                    ? BackendSyncTask.memberPointsRankList.size() : (pageIndex * pageSize)));
        }
        return pointsHomeResult;
    }

    @SuppressWarnings("all")
    public PointsHistoryResult pointsHistory(Map<String, Object> params, String token) {
        int userId = TokenUtils.getUserIdByToken(token);
        int pageIndex = 0;
        int pageSize = 0;
        if (Objects.nonNull(params.get("pageIndex"))) {
            pageIndex = NumberUtils.toInt(String.valueOf(params.get("pageIndex")));
        }
        if (Objects.nonNull(params.get("pageSize"))) {
            pageSize = NumberUtils.toInt(String.valueOf(params.get("pageSize")));
        }
        checkPageCondition(pageIndex, pageSize);
        PointsHistoryResult pointsHistoryResult = new PointsHistoryResult();
        int total = memberPointService.selectCount(
                Condition.create().eq("del_flag", 0).and().eq("user_id", userId));
        Page<MemberPoint> memberPointPage = memberPointService.selectPage(
                new Page<>(pageIndex, pageSize),
                Condition.create().eq("del_flag", 0).and().eq("user_id", userId)
                        .orderDesc(Collections.singletonList("created_at")));
        if (CollectionUtils.isNotEmpty(memberPointPage.getRecords())) {
            List<PointsHistoryResult.PointsHistoryBean> pointsHistoryBeans = memberPointPage.getRecords()
                    .stream().map(this::convertByMemberPoints).collect(Collectors.toList());
            pointsHistoryResult.setContents(pointsHistoryBeans);
            pointsHistoryResult.setHasMore(total > (pageIndex * pageSize));
        }
        return pointsHistoryResult;
    }

    @SuppressWarnings("all")
    public MemberCertResult memberCertificateResult(String token) {
        MemberCertResult certResult = new MemberCertResult();
        int userId = TokenUtils.getUserIdByToken(token);
        UserAuth userAuth = userAuthService.selectOne(Condition.create()
                .eq("user_id", userId).and().eq("del_flag", 0));
        if (Objects.isNull(userAuth)) {
            LOGGER.error("未查询到有提交认证记录:{}", userId);
            throw new BizException(ResponseCodeEnum.PARAM_ERROR);
        }
        BeanUtils.copyProperties(userAuth, certResult);
        return certResult;
    }

    @SuppressWarnings("all")
    public MemberHomeResult memberHome(String token) {
        int userId = TokenUtils.getUserIdByToken(token);
        MemberHomeResult memberHomeResult = new MemberHomeResult();
        UserInfo userInfo = getUserById(userId);
        if (Objects.isNull(userInfo)) {
            throw new BizException(ResponseCodeEnum.PARAM_ERROR);
        }
//        XslOrderInfoMember orderInfoMember = orderInfoMemberService.selectOne(Condition.create().eq("del_flag", 0)
//                .and().eq("user_id", userId).and().eq("order_status", 2)
//                .orderDesc(Collections.singletonList("created_at")).last("limit 1"));
        memberHomeResult.setAvatar(userInfo.getAvatar());
        memberHomeResult.setNickName(userInfo.getNickName());
        memberHomeResult.setStatus(userInfo.getDegree() > 0 ? 1 : 0);
        memberHomeResult.setLevel(userInfo.getDegree());
        memberHomeResult.setRule(cacheHolder.getGeneralKeyValue("member_rule_desc"));
        memberHomeResult.setMemberBenefits(cacheHolder.getMemberDescriptionByVersion("1"));
        memberHomeResult.setVideoCourses(Lists.newArrayList());

        memberHomeResult.setValidDateEnd(userInfo.getExpireTime());
        List<VideoCourse> videoCourses = videoCourseService.selectList(
                Condition.create().eq("del_flag", 0).orderDesc(Collections.singletonList("created_at"))
                        .last("limit 2"));
        if (CollectionUtils.isNotEmpty(videoCourses)) {
            memberHomeResult.setVideoCourses(videoCourses.stream().map(this::convertByVideo).collect(Collectors.toList()));
        }

        return memberHomeResult;
    }


    @SuppressWarnings("all")
    public List<MessageHomeResult> messageHome(String token) {
        int userId = TokenUtils.getUserIdByToken(token);
        List<MessageHomeResult> messageHomeResults = Lists.newArrayList();
        for (MessageTypeEnum value : MessageTypeEnum.values()) {
            MessageHomeResult messageHomeResult = new MessageHomeResult();
            messageHomeResult.setHasUnRead(false);
            MessageHomeResult.ContentBean contentBean = new MessageHomeResult.ContentBean();
            contentBean.setType(value.getType());
            messageHomeResult.setContent(contentBean);
            PushMsgType msgType = pushMsgTypeService.selectOne(Condition.create().eq("del_flag", 0)
                    .and().eq("id", value.getType()));
            if (Objects.nonNull(msgType)) {
                messageHomeResults.add(messageHomeResult);
                contentBean.setIconUrl(msgType.getIconUrl());
                contentBean.setTitle(msgType.getName());
                messageHomeResult.setHasUnRead(userPushMsgService.selectCount(Condition.create().eq("user_id", userId)
                        .and().eq("type", value.getType())
                        .and().eq("del_flag", 0)
                        .and().eq("status", 0)) > 0);
                contentBean.setDesc(userPushMsgService.findLatestMsgByUserIdAndType(userId, value.getType()));
            }
        }
        return messageHomeResults;
    }

    @SuppressWarnings("all")
    private int getUnReadMsgByUserId(int userId) {
        return userPushMsgService.selectCount(Condition.create().eq("user_id", userId)
                .and().eq("del_flag", 0)
                .and().eq("status", 0));
    }

    @SuppressWarnings("all")
    private int getUnUsedCouponByUserId(int userId) {
        return userCouponService.selectCount(Condition.create().eq("user_id", userId)
                .and().eq("del_flag", 0).and().eq("status", 0));
    }

    @SuppressWarnings("all")
    public UserCouponsResult coupons(Map<String, Object> params, String token) {
        int userId = TokenUtils.getUserIdByToken(token);
        int pageIndex = 0;
        int pageSize = 0;
        int type = NumberUtils.toInt(String.valueOf(params.get("type")));
        if (Objects.nonNull(params.get("pageIndex"))) {
            pageIndex = NumberUtils.toInt(String.valueOf(params.get("pageIndex")));
        }
        if (Objects.nonNull(params.get("pageSize"))) {
            pageSize = NumberUtils.toInt(String.valueOf(params.get("pageSize")));
        }
        checkPageCondition(pageIndex, pageSize);
        UserCouponsResult couponsResult = new UserCouponsResult();
        couponsResult.setDesc(cacheHolder.getGeneralKeyValue(Constants.COUPON_DESC));
        int total = userCouponService.selectCount(Condition.create().eq("user_id", userId)
                .and().eq("del_flag", 0).and().eq("type", type));

        Page<UserCoupon> userCouponPage = userCouponService.selectPage(
                new Page<>(pageIndex, pageSize),
                Condition.create().eq("del_flag", 0)
                        .and().eq("user_id", userId)
                        .and().eq("type", type)
                        .orderDesc(Collections.singletonList("created_at")));
        if (CollectionUtils.isNotEmpty(userCouponPage.getRecords())) {
            List<UserCouponsResult.CouponDetail> couponDetails = convertByCouponDetail(userCouponPage.getRecords());
            couponsResult.setContents(couponDetails);
            couponsResult.setHasMore(total > (pageIndex * pageSize));
        }
        return couponsResult;
    }

    @SuppressWarnings("all")
    private List<UserCouponsResult.CouponDetail> convertByCouponDetail(List<UserCoupon> userCoupons) {
        List<Integer> couponIds = userCoupons.stream().map(UserCoupon::getCouponId).collect(Collectors.toList());
        List<Coupon> coupons = couponService.selectList(
                Condition.create().eq("del_flag", 0).and().in("id", couponIds));
        Map<Integer, Integer> couponIdMap = userCoupons.stream().collect(Collectors.toMap(UserCoupon::getCouponId, UserCoupon::getStatus));
        if (CollectionUtils.isNotEmpty(coupons)) {
            List<UserCouponsResult.CouponDetail> result = Lists.newArrayList();
            for (Coupon coupon : coupons) {
                UserCouponsResult.CouponDetail couponDetail = new UserCouponsResult.CouponDetail();
                couponDetail.setCouponId(coupon.getId());
                couponDetail.setType(coupon.getType());
                couponDetail.setAmount(coupon.getAmount().doubleValue());
                couponDetail.setDetail(coupon.getDetail());
                couponDetail.setValidStarted(coupon.getValidStartAt());
                couponDetail.setValidEnded(coupon.getValidEndAt());
                if (Objects.nonNull(couponIdMap.get(coupon.getId()))) {
                    couponDetail.setStatus(couponIdMap.get(coupon.getId()));
                }
                result.add(couponDetail);
            }
            return result;
        }
        return Collections.emptyList();
    }

    @SuppressWarnings("all")
    public MessageCategoryResult messageCategory(Map<String, Object> params, String token) {
        int userId = TokenUtils.getUserIdByToken(token);
        int pageIndex = 0;
        int pageSize = 0;
        int type = NumberUtils.toInt(String.valueOf(params.get("type")));
        if (Objects.nonNull(params.get("pageIndex"))) {
            pageIndex = NumberUtils.toInt(String.valueOf(params.get("pageIndex")));
        }
        if (Objects.nonNull(params.get("pageSize"))) {
            pageSize = NumberUtils.toInt(String.valueOf(params.get("pageSize")));
        }
        checkPageCondition(pageIndex, pageSize);
        MessageCategoryResult messageCategoryResult = new MessageCategoryResult();
        int total = userPushMsgService.selectCount(Condition.create().eq("user_id", userId)
                .and().eq("del_flag", 0).and().eq("type", type));
        int unRead = userPushMsgService.selectCount(Condition.create().eq("user_id", userId)
                .and().eq("type", type)
                .and().eq("del_flag", 0)
                .and().eq("status", 0));
        messageCategoryResult.setHasUnRead(unRead > 0);
        Page<UserPushMsg> userPushMsgPage = userPushMsgService.selectPage(
                new Page<>(pageIndex, pageSize),
                Condition.create().eq("del_flag", 0)
                        .and().eq("user_id", userId)
                        .and().eq("type", type)
                        .orderDesc(Collections.singletonList("created_at")));
        List<Long> msgIds = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(userPushMsgPage.getRecords())) {
            List<MessageCategoryResult.ContentsBean> pushMsgs = userPushMsgPage.getRecords()
                    .stream().map(this::convertByUserPushMsg).collect(Collectors.toList());
            messageCategoryResult.setContents(pushMsgs);
            messageCategoryResult.setHasMore(total > (pageIndex * pageSize));
            msgIds = pushMsgs.stream().map(p -> NumberUtils.toLong(p.getMsgId())).collect(Collectors.toList());
        }
        Map<String, Object> cleanMsgMap = Maps.newHashMap();
        cleanMsgMap.put("msgIds", JSON.toJSONString(msgIds));
        messageRead(token, cleanMsgMap);
        return messageCategoryResult;
    }

    @SuppressWarnings("all")
    private MessageCategoryResult.ContentsBean convertByUserPushMsg(UserPushMsg pushMsg) {
        PushMsg msg = pushMsgService.selectOne(Condition.create()
                .eq("id", pushMsg.getMsgId()).and().eq("del_flag", 0));
        if (Objects.isNull(msg)) {
            return null;
        }
        MessageCategoryResult.ContentsBean contentsBean = new MessageCategoryResult.ContentsBean();
        contentsBean.setMsgId(pushMsg.getMsgId().toString());
        contentsBean.setStatus(pushMsg.getStatus());
        contentsBean.setCreatedAt(pushMsg.getCreatedAt());
        contentsBean.setImgUrl(msg.getImgUrl());
        contentsBean.setTitle(msg.getTitle());
        contentsBean.setContent(JackJsonUtil.toBean(msg.getContent(), new TypeReference<Map<String, Object>>() {
        }));
        contentsBean.setDesc(msg.getDescription());
        contentsBean.setDetailUrl(detailHost + "/msg?msgId=" + contentsBean.getMsgId());
        return contentsBean;
    }

    @SuppressWarnings("all")
    public void messageClean(String token) {
        int userId = TokenUtils.getUserIdByToken(token);
        UserPushMsg userPushMsg = new UserPushMsg();
        userPushMsg.setStatus(1);
        userPushMsg.setUpdatedAt(DatetimeUtils.now());
        userPushMsgService.update(userPushMsg, Condition.create().eq("user_id", userId).and().eq("del_flag", 0));
    }

    @SuppressWarnings("all")
    @Async
    public void messageRead(String token, Map<String, Object> params) {
        int userId = TokenUtils.getUserIdByToken(token);
        if (Objects.isNull(params.get("msgIds"))) {
            return;
        }
        List<Long> msgIds = JSON.parseArray(String.valueOf(params.get("msgIds")), Long.class);
        UserPushMsg userPushMsg = new UserPushMsg();
        userPushMsg.setStatus(1);
        userPushMsg.setUpdatedAt(DatetimeUtils.now());
        if (CollectionUtils.isEmpty(msgIds)) {
            // set status to read for all message of user
            userPushMsgService.update(userPushMsg, Condition.create().eq("user_id", userId).and().eq("del_flag", 0));
        } else {
            userPushMsgService.update(userPushMsg, Condition.create().eq("user_id", userId)
                    .and().eq("del_flag", 0).and().in("msg_id", msgIds));
        }
    }

    /**
     * 根据参数执行发送消息的任务
     *
     * @param params
     */
    @SuppressWarnings("all")
    public void messagePush(Map<String, Object> params) {
        if (Objects.isNull(params.get("msgIds")) || Objects.isNull(params.get("platform"))) {
            LOGGER.warn("empty msgIds");
            throw new BizException(ResponseCodeEnum.PARAM_ERROR);
        }
        PushRequest pushRequest = JSON.parseObject(JSON.toJSONString(params), PushRequest.class);
        List<Long> msgIds = pushRequest.getMsgIds();
        List<String> targets = pushRequest.getTarget();
        int platform = NumberUtils.toInt(String.valueOf(params.get("platform")));
        if (CollectionUtils.isEmpty(msgIds) || !PLATFORMS.contains(platform)) {
            LOGGER.warn("error msgIds or platform:{}", params);
            throw new BizException(ResponseCodeEnum.PARAM_ERROR);
        }
        msgIds.parallelStream().forEach(m -> {
            PushMsg pushMsg = pushMsgService.selectOne(Condition.create().eq("id", m).and().eq("del_flag", 0));
            if (Objects.nonNull(pushMsg)) {
                try {
                    doPushMsgJob(pushMsg, platform, targets);
                } catch (Exception e) {
                    LOGGER.error("push msg error:{}, {}, {}", m, platform, targets, e);
                }
            }
        });
    }

    @Transactional(rollbackFor = Exception.class)
    @SuppressWarnings("all")
    public void doPushMsgJob(PushMsg pushMsg, int platform, List<String> targets) {
        Platform platformValue = Platform.android_ios();
        Notification notification = Notification.newBuilder().setAlert(pushMsg.getTitle()).build();
        Map<String, String> extras = JackJsonUtil.toBean(pushMsg.getContent(), new TypeReference<Map<String, String>>() {
        });
        switch (platform) {
            case 1:
                platformValue = Platform.android();
                notification = Notification.android(pushMsg.getDescription(), pushMsg.getTitle(), extras);
                break;
            case 2:
                platformValue = Platform.ios();
                IosAlert iosAlert = IosAlert.newBuilder().setTitleAndBody(pushMsg.getTitle(), pushMsg.getDescription(), "").build();
                notification = Notification.ios(iosAlert, extras);
                break;
            default:
                notification = Notification.newBuilder()
                        .addPlatformNotification(IosNotification.alert(pushMsg.getTitle()))
                        .addPlatformNotification(AndroidNotification.alert(pushMsg.getTitle()))
                        .build();
                break;
        }
        Audience audience = Audience.all();
        if (CollectionUtils.isNotEmpty(targets)) {
            audience = Audience.registrationId(targets);
        }

        PushPayload payload = PushPayload.newBuilder()
                .setPlatform(platformValue)
                .setAudience(audience)
                .setNotification(notification)
                .setOptions(Options.newBuilder()
                        .setApnsProduction(true)
                        .build())
                .build();
        try {
            pushClient.sendPush(payload);
        } catch (Exception e) {
            LOGGER.error("call channel push error for :{}, {}, {}", pushMsg, platform, targets, e);
            return;
        }
        if (CollectionUtils.isEmpty(targets)) {
            int lastUserId = 0;
            do {
                List<User> users = userService.selectList(Condition.create().eq("del_flag", 0)
                        .and().gt("id", lastUserId).last("limit 500"));
                if (CollectionUtils.isEmpty(users)) {
                    break;
                }
                lastUserId = users.get(users.size() - 1).getId();
                UserPushMsg userPushMsg = new UserPushMsg();
                userPushMsg.setStatus(0);
                userPushMsg.setDelFlag(0);
                userPushMsg.setMsgId(pushMsg.getId());
                userPushMsg.setCreatedAt(DatetimeUtils.now());
                userPushMsg.setType(pushMsg.getType());

                for (User user : users) {
                    userPushMsg.setUserId(user.getId());
                    userPushMsgService.insert(userPushMsg);
                }
            } while (true);
        } else {
            List<UserPushToken> pushTokens = userPushTokenService.selectList(
                    Condition.create().eq("del_flag", 0).and().in("token", targets));
            if (CollectionUtils.isNotEmpty(pushTokens)) {
                Set<Integer> userIds = pushTokens.stream().map(UserPushToken::getUserId).collect(Collectors.toSet());
                UserPushMsg userPushMsg = new UserPushMsg();
                userPushMsg.setStatus(0);
                userPushMsg.setDelFlag(0);
                userPushMsg.setMsgId(pushMsg.getId());
                userPushMsg.setCreatedAt(DatetimeUtils.now());
                userPushMsg.setType(pushMsg.getType());

                for (Integer userId : userIds) {
                    userPushMsg.setUserId(userId);
                    userPushMsgService.insert(userPushMsg);
                }
            }
        }
    }

    @SuppressWarnings("all")
    public void memberCertificate(Map<String, Object> params, String token) {
        int userId = TokenUtils.getUserIdByToken(token);
        UserAuth userAuth = userAuthService.selectOne(Condition.create()
                .eq("user_id", userId).and().eq("del_flag", 0));
        if (Objects.nonNull(userAuth)) {
            if (userAuth.getStatus() == 1) {
                return;
            } else {
                UserAuth apply = new UserAuth();
                apply.setStatus(0);
                apply.setDelFlag(0);
                apply.setUpdatedAt(DatetimeUtils.now());
                apply.setType(NumberUtils.toInt(String.valueOf(params.get("type"))));
                apply.setNumber(String.valueOf(params.get("number")));
                apply.setName(String.valueOf(params.get("name")));
                apply.setGender(NumberUtils.toInt(String.valueOf(params.get("gender"))));
                apply.setFrontImage(String.valueOf(params.get("frontImage")));
                apply.setBackImage(String.valueOf(params.get("backImage")));
                userAuthService.update(apply, Condition.create().eq("user_id", userId)
                        .and().eq("del_flag", 0));
            }
        } else {
            UserAuth apply = new UserAuth();
            apply.setUserId(userId);
            apply.setCreatedAt(DatetimeUtils.now());
            apply.setStatus(0);
            apply.setDelFlag(0);
            apply.setType(NumberUtils.toInt(String.valueOf(params.get("type"))));
            apply.setNumber(String.valueOf(params.get("number")));
            apply.setGender(NumberUtils.toInt(String.valueOf(params.get("gender"))));
            apply.setName(String.valueOf(params.get("name")));
            apply.setFrontImage(String.valueOf(params.get("frontImage")));
            apply.setBackImage(String.valueOf(params.get("backImage")));
            userAuthService.insert(apply);
        }
    }

    private PointsHistoryResult.PointsHistoryBean convertByMemberPoints(MemberPoint memberPoint) {
        PointsHistoryResult.PointsHistoryBean pointsHistoryBean = new PointsHistoryResult.PointsHistoryBean();
        pointsHistoryBean.setCreatedAt(memberPoint.getCreatedAt());
        if (Objects.nonNull(MemberActionTypeEnum.ofType(memberPoint.getSource()))) {
            pointsHistoryBean.setDesc(MemberActionTypeEnum.ofType(memberPoint.getSource()).getDesc());
        } else {
            throw new BizException(ResponseCodeEnum.SYSTEM_ERROR);
        }
        pointsHistoryBean.setPointsLeft(memberPoint.getTotal());
        pointsHistoryBean.setType(memberPoint.getType());
        pointsHistoryBean.setPoint(memberPoint.getPoint());
        return pointsHistoryBean;
    }

    @SuppressWarnings("all")
    private int getMemberPointRank(int userId, int points) {
        int total = userInfoService.selectCount(Condition.create().eq("del_flag", 0));
        int below = userInfoService.selectCount(
                Condition.create().eq("del_flag", 0)
                        .and().eq("user_id", userId)
                        .and().gt("points", points));
        return total - below;
    }

    private BanAuthorResource convertByUser(UserInfo userInfo) {
        BanAuthorResource banAuthorResource = new BanAuthorResource();
        banAuthorResource.setAvatar(userInfo.getAvatar());
        banAuthorResource.setNickName(userInfo.getNickName());
        if (userInfo.getIsArtist() == 1 && StringUtils.isNotBlank(userInfo.getUserName())) {
            banAuthorResource.setNickName(userInfo.getUserName());
        }
        banAuthorResource.setUserId(userInfo.getUserId());
        banAuthorResource.setIntroduction(userInfo.getIntro());
        return banAuthorResource;
    }

    @SuppressWarnings("all")
    private boolean likeOrNot(int userId, int type, String resourceId) {
        int likeRecord = likeRecordService.selectCount(
                Condition.create().eq("user_id", userId)
                        .and().eq("del_flag", 0).and().eq("resource_id", resourceId)
                        .and().eq("type", type));
        return likeRecord > 0;
    }

    private CommodityDetail convertCommodityDetailByCommodity(Commodity commodity, List<CommodityProduct> products) {
        CommodityDetail resource = new CommodityDetail();
        UserInfo userInfo = getUserById(commodity.getUserId());
        if (Objects.isNull(userInfo)) {
            return resource;
        }
        resource.setAuthor(userInfo.getNickName());
        if (userInfo.getIsArtist() == 1 && StringUtils.isNotBlank(userInfo.getUserName())) {
            resource.setAuthor(userInfo.getUserName());
        }
        resource.setContent(commodity.getContent());
        resource.setAvatar(userInfo.getAvatar());
        resource.setUserId(userInfo.getUserId());
        Optional.ofNullable(commodity.getReadNum()).ifPresent(resource::setViewNum);
        Optional.ofNullable(commodity.getLikeNum()).ifPresent(resource::setLikeNum);
        resource.setTitle(commodity.getTitle());
        resource.setImgUrl(commodity.getThumbUrl());
        resource.setResourceId(commodity.getSnId());
        resource.setCreatedAt(commodity.getCreatedAt());
        resource.setDescription(commodity.getDescription());
        if (CollectionUtils.isNotEmpty(products)) {
            resource.setSuggestProducts(products.stream().map(this::convertByProduct).collect(Collectors.toList()));
        }
        return resource;
    }

    private ProductResource convertByProduct(CommodityProduct product) {
        ProductResource resource = new ProductResource();
        resource.setImgUrl(product.getThumbUrl());
        resource.setPrice(String.valueOf(product.getPrice()));
        resource.setResourceId(product.getSnId());
        resource.setTitle(product.getTitle());
        return resource;
    }

    @SuppressWarnings("all")
    private RelationResult getRelationByType(int userId, int pageOwner, int pageIndex, int pageSize, RelationTypeEnum relationTypeEnum) {
        RelationResult relationResult = new RelationResult();
        relationResult.setType(relationTypeEnum.getType());
        relationResult.setName(relationTypeEnum.getName());
        List<Integer> relationUserIds = null;
        switch (relationTypeEnum) {
            case FOCUS:
                Page<UserFocusAssociation> focusAssociationPage =
                        focusAssociationService.selectPage(new Page<>(pageIndex, pageSize)
                                , Condition.create().eq("user_id", pageOwner).and().eq("del_flag", 0));
                if (CollectionUtils.isNotEmpty(focusAssociationPage.getRecords())) {
                    relationUserIds = focusAssociationPage.getRecords().
                            stream().map(UserFocusAssociation::getFocusId).collect(Collectors.toList());
                    relationResult.setContents(relationUserIds
                            .stream().map(id -> convertByFocusRelation(id, focusById(userId, id))).collect(Collectors.toList()));
                    int totalFocus = focusAssociationService
                            .selectCount(Condition.create().eq("user_id", pageOwner).and().eq("del_flag", 0));
                    relationResult.setHasMore(totalFocus > (pageIndex * pageSize));
                }
                break;
            case FAN:
                Page<UserFocusAssociation> fanAssociationPage = focusAssociationService
                        .selectPage(new Page<>(pageIndex, pageSize)
                                , Condition.create().eq("focus_id", pageOwner).and().eq("del_flag", 0));
                if (CollectionUtils.isNotEmpty(fanAssociationPage.getRecords())) {
                    relationUserIds = fanAssociationPage.getRecords()
                            .stream().map(UserFocusAssociation::getUserId).collect(Collectors.toList());
                    relationResult.setContents(relationUserIds
                            .stream().map(id -> convertByFocusRelation(id, focusById(userId, id))).collect(Collectors.toList()));
                    int totalFan = focusAssociationService
                            .selectCount(Condition.create().eq("focus_id", pageOwner).and().eq("del_flag", 0));
                    relationResult.setHasMore(totalFan > (pageIndex * pageSize));
                }
                break;
            default:
                break;
        }
        return relationResult;
    }

    @SuppressWarnings("all")
    private boolean focusById(int userId, int focusId) {
        if (userId == 0) {
            return false;
        }
        UserFocusAssociation focusAssociation = focusAssociationService
                .selectOne(Condition.create().eq("user_id", userId)
                        .and().eq("focus_id", focusId)
                        .and().eq("del_flag", 0));
        return Objects.nonNull(focusAssociation);
    }

    private RelationDetail convertByFocusRelation(Integer userId, boolean focusFlag) {
        RelationDetail relationDetail = new RelationDetail();
        relationDetail.setHasFocus(focusFlag);
        relationDetail.setUserId(userId);
        UserInfo userInfo = getUserById(userId);
        relationDetail.setAvatar(userInfo.getAvatar());
        relationDetail.setNickName(userInfo.getNickName());
        if (userInfo.getIsArtist() == 1 && StringUtils.isNotBlank(userInfo.getUserName())) {
            relationDetail.setNickName(userInfo.getUserName());
        }
        Optional.ofNullable(articleService.findViewCountByAuthorId(userId)).ifPresent(relationDetail::setViewNum);
        return relationDetail;
    }

    @SuppressWarnings("all")
    private boolean checkDraft(int userId) {
        List<Article> drafts = articleService.selectList(
                Condition.create().eq("user_id", userId)
                        .and().eq("is_draft", 1)
                        .and().eq("del_flag", 0));
        if (CollectionUtils.isNotEmpty(drafts) && drafts.size() >= Constants.DRAFT_LIMIT) {
            return true;
        }
        return false;
    }

    @SuppressWarnings("all")
    private SearchListResult buildSearchResult(int userId, int type, int pageIndex, int pageSize, String keywords) {
        SearchListResult searchResult = new SearchListResult();
        switch (SearchTypeEnum.ofType(type)) {
            case ARTICLE:
                Integer nonVideoCount = articleService.findNonVideoTagArticleCount(keywords);
                if (Objects.nonNull(nonVideoCount)) {
                    List<Article> nonVideos = articleService.findNonTagArticlesByKeywords(
                            keywords, (pageIndex - 1) * pageSize, pageSize);
                    if (CollectionUtils.isNotEmpty(nonVideos)) {
                        List<ArticleResource> videoResources = nonVideos
                                .stream().map(a -> convertByArticle(a, userId)).collect(Collectors.toList());
                        searchResult.setContents(videoResources);
                    }
                    searchResult.setHasMore(nonVideoCount > (pageIndex * pageSize));
                }
                break;
            case USER:
                int artistNum = userInfoService.selectCount(
                        Condition.create().eq("del_flag", 0).andNew()
                                .like("nick_name", keywords).or().like("user_name", keywords));
                Page<UserInfo> pageUserInfos = userInfoService.selectPage(new Page<>(pageIndex, pageSize),
                        Condition.create().eq("del_flag", 0).andNew()
                                .like("nick_name", keywords).or().like("user_name", keywords));
                if (Objects.nonNull(pageUserInfos) && CollectionUtils.isNotEmpty(pageUserInfos.getRecords())) {
                    List<ArtistResource> artistResources = pageUserInfos.getRecords()
                            .stream().map(u -> convertByUserInfo(u, userId)).collect(Collectors.toList());
                    searchResult.setContents(artistResources);
                }
                searchResult.setHasMore(artistNum > (pageIndex * pageSize));
                break;
            case VIDEO:
                Integer videoCount = articleService.findVideoTagArticleCount(keywords);
                if (Objects.nonNull(videoCount)) {
                    List<Article> videos = articleService.findVideoTagArticlesByKeywords(
                            keywords, (pageIndex - 1) * pageSize, pageSize);
                    if (CollectionUtils.isNotEmpty(videos)) {
                        List<ArticleResource> videoResources = videos
                                .stream().map(v -> convertByArticle(v, userId)).collect(Collectors.toList());
                        searchResult.setContents(videoResources);
                    }
                    searchResult.setHasMore(videoCount > (pageIndex * pageSize));
                }
                break;
            default:
                break;
        }
        return searchResult;
    }

    private CommodityDetail convertCommodityByArticle(Article article) {
        CommodityDetail commodityDetail = new CommodityDetail();
        UserInfo userInfo = getUserById(article.getUserId());
        if (Objects.isNull(userInfo)) {
            return commodityDetail;
        }
        commodityDetail.setAuthor(userInfo.getNickName());
        commodityDetail.setUserId(userInfo.getUserId());
        commodityDetail.setAvatar(userInfo.getAvatar());
        commodityDetail.setContent(article.getContent());
        commodityDetail.setCreatedAt(article.getCreatedAt());
        commodityDetail.setImgUrl(article.getThumbUrl());
        commodityDetail.setResourceId(article.getSnId());
        Optional.ofNullable(article.getReadNum()).ifPresent(commodityDetail::setViewNum);
        Optional.ofNullable(article.getLikeNum()).ifPresent(commodityDetail::setLikeNum);
        commodityDetail.setTitle(article.getTitle());
        return commodityDetail;
    }

    @SuppressWarnings("all")
    private List<Article> getArticleByTags(int tag) {
        List<ArticleTagAssociation> articleTagAssociations =
                articleTagAssociationService.selectList(
                        Condition.create().eq("tag_id", tag).and().eq("del_flag", 0));
        if (CollectionUtils.isNotEmpty(articleTagAssociations)) {
            return articleService.selectList(Condition.create()
                    .in("id", articleTagAssociations.stream()
                            .map(ArticleTagAssociation::getArticleId).collect(Collectors.toList()))
                    .and().eq("del_flag", 0).and()
                    .eq("status", ArticleStatusEnum.ONLINE.getStatus())
                    .orderDesc(Lists.newArrayList("created_at")));
        }
        return Collections.emptyList();
    }

    @SuppressWarnings("all")
    private PersonalStatistics getPersonalStatistic(int userId) {
        PersonalStatistics statistics = new PersonalStatistics();
        List<UserFocusAssociation> focusedAssociations = focusAssociationService.selectList(
                Condition.create().eq("user_id", userId).and().eq("del_flag", 0));
        List<UserFocusAssociation> focusAssociations = focusAssociationService.selectList(
                Condition.create().eq("focus_id", userId).and().eq("del_flag", 0));
        statistics.setFocusNum(focusedAssociations.size());
        statistics.setFanNum(focusAssociations.size());
        Optional.ofNullable(articleService.findViewCountByAuthorId(userId)).ifPresent(v -> statistics.setViewNum(v));
        return statistics;
    }

    private ArtistResource convertByUserInfo(UserInfo userInfo, int userId) {
        ArtistResource artistResource = new ArtistResource();
        artistResource.setFanNum(userInfo.getViewNum());
        artistResource.setLetter(userInfo.getLetter());
        artistResource.setNickName(userInfo.getNickName());
        artistResource.setUserId(userInfo.getUserId());
        artistResource.setAvatar(userInfo.getAvatar());
        if (userInfo.getIsArtist() == 1 && StringUtils.isNotBlank(userInfo.getUserName())) {
            artistResource.setNickName(userInfo.getUserName());
        }
        artistResource.setHasFocus(focusById(userId, userInfo.getUserId()));
        return artistResource;
    }

    private CarouselResource convertByCarousel(Carousel carousel) {
        CarouselResource carouselResource = new CarouselResource();
        carouselResource.setImgUrl(carousel.getThumbUrl());
        carouselResource.setTitle(carousel.getTitle());
        carouselResource.setType(carousel.getType());
        carouselResource.setResourceId(carousel.getResourceId());
        UserInfo userInfo = findAuthorByResource(carousel.getResourceId(), carousel.getType());
        if (Objects.nonNull(userInfo)) {
            carouselResource.setAuthor(userInfo.getNickName());
        }
        carouselResource.setDetailUrl(
                getDetailUrl(ResourceTypeEnum.ofType(carouselResource.getType()), carousel.getResourceId()));
        return carouselResource;
    }

    @SuppressWarnings("all")
    private CommodityResult.CommodityInfo convertCommodityInfoByArticleResource(ArticleResource articleResource, int userId) {
        CommodityResult.CommodityInfo commodityInfo = new CommodityResult.CommodityInfo();
        UserInfo userInfo = getUserById(articleResource.getUserId());
        if (Objects.isNull(userInfo)) {
            return commodityInfo;
        }
        commodityInfo.setAuthor(userInfo.getNickName());
        if (userInfo.getIsArtist() == 1 && StringUtils.isNotBlank(userInfo.getUserName())) {
            commodityInfo.setAuthor(userInfo.getUserName());
        }
        commodityInfo.setTitle(articleResource.getTitle());
        commodityInfo.setUserId(articleResource.getUserId());
        commodityInfo.setAvatar(userInfo.getAvatar());
        commodityInfo.setCreatedAt(articleResource.getCreatedAt());
        commodityInfo.setImgUrl(articleResource.getImgUrl());
        commodityInfo.setResourceId(articleResource.getResourceId());
        commodityInfo.setViewNum(articleResource.getViewNum());
        commodityInfo.setLikeNum(articleResource.getLikeNum());
        commodityInfo.setLike(likeOrNot(userId, ARTICLE.getType(), articleResource.getResourceId()));
        commodityInfo.setDetailUrl(getDetailUrl(ARTICLE, articleResource.getResourceId()));
        commodityInfo.setDescription(articleResource.getDescription());
        return commodityInfo;
    }

    @SuppressWarnings("all")
    private CommodityResult.CommodityInfo convertCommodityInfoByCommodity(Commodity commodity, int userId) {
        CommodityResult.CommodityInfo commodityInfo = new CommodityResult.CommodityInfo();
        UserInfo userInfo = getUserById(commodity.getUserId());
        if (Objects.isNull(userInfo)) {
            return commodityInfo;
        }
        commodityInfo.setAuthor(userInfo.getNickName());
        if (userInfo.getIsArtist() == 1 && StringUtils.isNotBlank(userInfo.getUserName())) {
            commodityInfo.setAuthor(userInfo.getUserName());
        }
        commodityInfo.setTitle(commodity.getTitle());
        commodityInfo.setUserId(commodity.getUserId());
        commodityInfo.setAvatar(userInfo.getAvatar());
        commodityInfo.setCreatedAt(commodity.getCreatedAt());
        commodityInfo.setImgUrl(commodity.getThumbUrl());
        commodityInfo.setResourceId(commodity.getSnId());
        Optional.ofNullable(commodity.getReadNum()).ifPresent(commodityInfo::setViewNum);
        Optional.ofNullable(commodity.getLikeNum()).ifPresent(commodityInfo::setLikeNum);
        commodityInfo.setLike(likeOrNot(userId, PRODUCT.getType(), commodity.getSnId()));
        commodityInfo.setFocus(focusById(userId, commodity.getUserId()));
        commodityInfo.setDetailUrl(getDetailUrl(PRODUCT, commodity.getSnId()));
        commodityInfo.setDescription(commodity.getDescription());
        return commodityInfo;
    }

    private TrainingResult.Training convertByTrainingClass(TrainingClass trainingClass) {
        UserInfo userInfo = getUserById(trainingClass.getUserId());
        if (Objects.isNull(userInfo)) {
            return null;
        }
        TrainingResult.Training training = new TrainingResult.Training();
        training.setCreatedAt(trainingClass.getCreatedAt());
        training.setImgUrl(trainingClass.getThumbUrl());
        training.setResourceId(trainingClass.getSnId());
        training.setTitle(trainingClass.getTitle());
        training.setStatus(trainingClass.getStatus());
        training.setDescription(trainingClass.getDescription());
        Optional.ofNullable(trainingClass.getReadNum()).ifPresent(training::setViewNum);
        training.setDetailUrl(getDetailUrl(TRAINING, training.getResourceId()));
        training.setAuthor(userInfo.getNickName());
        if (userInfo.getIsArtist() == 1 && StringUtils.isNotBlank(userInfo.getUserName())) {
            training.setAuthor(userInfo.getUserName());
        }
        training.setUserId(userInfo.getUserId());
        training.setAvatar(userInfo.getAvatar());
        return training;
    }

    private CourseResult.CourseInfo convertByCourse(Course course) {
        UserInfo userInfo = getUserById(course.getUserId());
        if (Objects.isNull(userInfo)) {
            return null;
        }
        CourseResult.CourseInfo courseInfo = new CourseResult.CourseInfo();
        courseInfo.setCreatedAt(course.getCreatedAt());
        courseInfo.setImgUrl(course.getThumbUrl());
        courseInfo.setResourceId(course.getSnId());
        courseInfo.setTitle(course.getTitle());
        courseInfo.setAuthor(userInfo.getNickName());
        if (userInfo.getIsArtist() == 1 && StringUtils.isNotBlank(userInfo.getUserName())) {
            courseInfo.setAuthor(userInfo.getUserName());
        }
        courseInfo.setUserId(userInfo.getUserId());
        courseInfo.setAvatar(userInfo.getAvatar());
        Optional.ofNullable(course.getReadNum()).ifPresent(courseInfo::setViewNum);
        Optional.ofNullable(course.getType()).ifPresent(courseInfo::setType);
        courseInfo.setDetailUrl(getDetailUrl(COURSE, course.getSnId()));
        courseInfo.setDescription(course.getDescription());
        return courseInfo;
    }

    private SketchResult.SketchInfo convertByCourse(SketchingActivity sketchingActivity) {
        UserInfo userInfo = getUserById(sketchingActivity.getUserId());
        if (Objects.isNull(userInfo)) {
            return null;
        }
        SketchResult.SketchInfo courseInfo = new SketchResult.SketchInfo();
        courseInfo.setCreatedAt(sketchingActivity.getCreatedAt());
        courseInfo.setImgUrl(sketchingActivity.getThumbUrl());
        courseInfo.setResourceId(sketchingActivity.getSnId());
        courseInfo.setTitle(sketchingActivity.getTitle());
        courseInfo.setAuthor(userInfo.getNickName());
        if (userInfo.getIsArtist() == 1 && StringUtils.isNotBlank(userInfo.getUserName())) {
            courseInfo.setAuthor(userInfo.getUserName());
        }
        courseInfo.setUserId(userInfo.getUserId());
        courseInfo.setAvatar(userInfo.getAvatar());
        Optional.ofNullable(sketchingActivity.getReadNum()).ifPresent(courseInfo::setViewNum);
        courseInfo.setDetailUrl(getDetailUrl(SKETCHING_ACTIVITY, sketchingActivity.getSnId()));
        courseInfo.setDescription(sketchingActivity.getDescription());
        return courseInfo;
    }

    private VideoInfo convertByVideo(VideoCourse course) {
        UserInfo userInfo = getUserById(course.getUserId());
        if (Objects.isNull(userInfo)) {
            return null;
        }
        VideoInfo courseInfo = new VideoInfo();
        courseInfo.setCreatedAt(course.getCreatedAt());
        courseInfo.setImgUrl(course.getThumbUrl());
        courseInfo.setResourceId(course.getSnId());
        courseInfo.setTitle(course.getTitle());
        courseInfo.setAuthor(userInfo.getNickName());
        if (userInfo.getIsArtist() == 1 && StringUtils.isNotBlank(userInfo.getUserName())) {
            courseInfo.setAuthor(userInfo.getUserName());
        }
        courseInfo.setUserId(userInfo.getUserId());
        courseInfo.setAvatar(userInfo.getAvatar());
        Optional.ofNullable(course.getReadNum()).ifPresent(courseInfo::setViewNum);
        courseInfo.setDetailUrl(getDetailUrl(VIDEO_COURSE, course.getSnId()));
        courseInfo.setVideoUrl(course.getVideoUrl());
        courseInfo.setDescription(course.getDescription());
        return courseInfo;
    }

    private ArticleResource convertByArticle(Article article) {
        return convertByArticleCore(article);
    }

    private ArticleResource convertByArticle(Article article, int userId) {
        ArticleResource articleResource = convertByArticleCore(article);
        articleResource.setLike(likeOrNot(userId, ARTICLE.getType(), article.getSnId()));
        articleResource.setFocus(focusById(userId, article.getUserId()));
        return articleResource;
    }

    @SuppressWarnings("all")
    private ArticleResource convertByArticleCore(Article article) {
        UserInfo userInfo = findAuthorByResource(article.getSnId(), ARTICLE.getType());
        if (Objects.isNull(userInfo)) {
            throw new BizException(ResponseCodeEnum.SYSTEM_ERROR);
        }
        ArticleResource articleResource = new ArticleResource();
        articleResource.setVideoUrl(article.getVideoUrl());
        articleResource.setResourceId(article.getSnId());
        articleResource.setCreatedAt(article.getCreatedAt());
        articleResource.setTitle(article.getTitle());
        Optional.ofNullable(article.getReadNum()).ifPresent(articleResource::setViewNum);
        Optional.ofNullable(article.getLikeNum()).ifPresent(articleResource::setLikeNum);
        articleResource.setType(article.getType());
        articleResource.setAuthor(userInfo.getNickName());
        if (userInfo.getIsArtist() == 1 && StringUtils.isNotBlank(userInfo.getUserName())) {
            articleResource.setAuthor(userInfo.getUserName());
        }
        articleResource.setUserId(userInfo.getUserId());
        articleResource.setAvatar(userInfo.getAvatar());
        articleResource.setStatus(article.getStatus());
        articleResource.setDescription(article.getDescription());
        articleResource.setFrom(article.getFrom());
        articleResource.setDetailUrl(getDetailUrl(ARTICLE, article.getSnId()));
        articleResource.setFailReason(article.getRejectMessage());

        if (StringUtils.isNotBlank(article.getThumbUrl()) && !"null".equalsIgnoreCase(article.getThumbUrl())) {
            articleResource.setImgUrl(article.getThumbUrl());
        } else {
            articleResource.setImgUrl(extractImgUrl(article.getContent()));
        }
        return articleResource;
    }

    private String extractImgUrl(String content) {
        try {
            if (StringUtils.isNotBlank(content)) {
                List<ArticleDetail> blocks = JSON.parseArray(content, ArticleDetail.class);
                if (CollectionUtils.isNotEmpty(blocks)) {
                    Optional<ArticleDetail> firstImgOpt = blocks.stream()
                            .filter(a -> StringUtils.isNotBlank(a.getImgUrl())).findFirst();
                    if (firstImgOpt.isPresent()) {
                        return firstImgOpt.get().getImgUrl();
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.debug("extractImgUrl", e);
        }
        return null;
    }

    @SuppressWarnings("all")
    public UserInfo getUserById(int userId) {
        return userInfoService.selectOne(Condition.create().eq("user_id", userId).and().eq("del_flag", 0));
    }

    @SuppressWarnings("all")
    private UserInfo findAuthorByResource(String resourceId, int type) {
        ResourceTypeEnum resourceTypeEnum = ResourceTypeEnum.ofType(type);
        int userId = 0;
        switch (resourceTypeEnum) {
            case ARTICLE:
                Article article = articleService.selectOne(
                        Condition.create().eq("sn_id", resourceId).and().eq("del_flag", 0));
                if (Objects.nonNull(article)) {
                    userId = article.getUserId();
                }
                break;
            case TRAINING:
                TrainingClass trainingClass = trainingClassService.selectOne(
                        Condition.create().eq("sn_id", resourceId).and().eq("del_flag", 0));
                if (Objects.nonNull(trainingClass)) {
                    userId = trainingClass.getUserId();
                }
                break;
            case COURSE:
                Course course = courseService.selectOne(
                        Condition.create().eq("sn_id", resourceId).and().eq("del_flag", 0));
                if (Objects.nonNull(course)) {
                    userId = course.getUserId();
                }
                break;
            case PRODUCT:
                Commodity commodity = commodityService.selectOne(
                        Condition.create().eq("sn_id", resourceId).and().eq("del_flag", 0));
                if (Objects.nonNull(commodity)) {
                    userId = commodity.getUserId();
                }
                break;
            default:
                break;
        }
        if (userId > 0) {
            return getUserById(userId);
        }
        return null;
    }

    /**
     * 缓存资源的点赞结果，由定时任务去更新
     *
     * @param resourceId
     * @param action
     * @param resHashMapKey
     */
    private void processResourceLikeAction(String resourceId, String resTypeName, int action,
                                           String resHashMapKey, String resSetKey) {
        if (StringUtils.isBlank(resHashMapKey)) {
            LOGGER.warn("processResourceLikeAction find not exist key");
            throw new BizException(ResponseCodeEnum.PARAM_ERROR);
        }
        try {
            redisUtil.sSet(resSetKey, resourceId);
            if (Constants.RESOURCE_LIKE == action) {
                redisUtil.hincr(resHashMapKey, resourceId, 1);
            } else if (Constants.RESOURCE_DISLIKE == action) {
                redisUtil.hincr(resHashMapKey, resourceId, -1);
            }
        } catch (Exception e) {
            LOGGER.error("processResourceLikeAction error for resId:{}, type:{}, action:{}",
                    resourceId, resTypeName, action, e);
        }
    }

    /**
     * 缓存资源的阅读结果，由定时任务去更新
     *
     * @param resourceId
     * @param resTypeName
     * @param resHashMapKey
     * @param resSetKey
     */
    @Async
    public void processResourceView(String resourceId, String resTypeName, String resHashMapKey,
                                    String resSetKey, int userId) {
        try {
            redisUtil.sSet(resSetKey, resourceId);
            redisUtil.hincr(resHashMapKey, resourceId, 1);
            ReadRecord readRecord = new ReadRecord();
            readRecord.setCreatedAt(new Date());
            readRecord.setDelFlag(0);
            readRecord.setResourceId(resourceId);
            readRecord.setUserId(userId);
            readRecord.setType(ResourceTypeEnum.valueOf(resTypeName).getType());
            readRecordService.insert(readRecord);
        } catch (Exception e) {
            LOGGER.error("processResourceView error for resId:{}, type:{}", resourceId, resTypeName, e);
        }
    }

    @Async
    public void processPersonalContent(int userId, Set<String> resourceIds) {
        Set<String> banAuthors = getPersonalBanAuthors(userId);
        processPersonalTagArticles(userId, resourceIds, banAuthors);
        processPersonalRecommends(userId, resourceIds, banAuthors);
    }

    private void processPersonalRecommends(int userId, Set<String> resourceIds, Set<String> authors) {
        if (userId == 0) {
            return;
        }
        if (CollectionUtils.isEmpty(resourceIds) && CollectionUtils.isEmpty(authors)) {
            return;
        }
        personalRecommendContent.put(String.valueOf(userId), shadowCopy(unifiedArticleResources));
        if (CollectionUtils.isNotEmpty(resourceIds)) {
            personalRecommendContent.put(String.valueOf(userId),
                    personalRecommendContent.get(String.valueOf(userId))
                            .stream().filter(a -> !resourceIds.contains(a.getResourceId())).collect(Collectors.toList()));
        }
        if (CollectionUtils.isNotEmpty(authors)) {
            personalRecommendContent.put(String.valueOf(userId),
                    personalRecommendContent.get(String.valueOf(userId))
                            .stream().filter(a -> !authors.contains(String.valueOf(a.getUserId()))).collect(Collectors.toList()));
        }
    }

    private List<ArticleResource> shadowCopy(List<ArticleResource> articleResources) {
        return Lists.newArrayList(articleResources);
    }

    private void processPersonalTagArticles(int userId, Set<String> resourceIds, Set<String> authors) {
        if (userId == 0) {
            return;
        }
        if (CollectionUtils.isEmpty(resourceIds) && CollectionUtils.isEmpty(authors)) {
            return;
        }
        Map<ArticleTagEnum, List<ArticleResource>> personalMap = Maps.newConcurrentMap();
        for (Map.Entry<ArticleTagEnum, List<ArticleResource>> entry : tagArticleMap.entrySet()) {
            personalMap.put(entry.getKey(), shadowCopy(entry.getValue()));
        }
        personalTagArticleMap.put(String.valueOf(userId), personalMap);
        if (CollectionUtils.isNotEmpty(resourceIds)) {
            for (Map.Entry<ArticleTagEnum, List<ArticleResource>> entry : personalTagArticleMap.get(String.valueOf(userId)).entrySet()) {
                entry.setValue(entry.getValue().stream().filter(a -> !resourceIds.contains(a.getResourceId())).collect(Collectors.toList()));
            }
        }
        if (CollectionUtils.isNotEmpty(authors)) {
            for (Map.Entry<ArticleTagEnum, List<ArticleResource>> entry : personalTagArticleMap.get(String.valueOf(userId)).entrySet()) {
                entry.setValue(entry.getValue().stream().filter(a -> !authors.contains(String.valueOf(a.getUserId()))).collect(Collectors.toList()));
            }
        }
    }

    private List<Article> filterArticles(List<Article> articles, int userId) {
        if (userId == 0) {
            return articles;
        }
        List<Article> filterArticles = articles;
        Set<String> personalBanResources = getPersonalBanResources(userId);
        Set<String> personalBanAuthors = getPersonalBanAuthors(userId);
        if (CollectionUtils.isNotEmpty(personalBanAuthors)) {
            filterArticles = articles.stream()
                    .filter(a -> !personalBanAuthors.contains(String.valueOf(a.getUserId()))).collect(Collectors.toList());
        }
        if (CollectionUtils.isNotEmpty(personalBanResources)) {
            filterArticles = filterArticles.stream()
                    .filter(a -> !personalBanResources.contains(a.getSnId())).collect(Collectors.toList());
        }
        return filterArticles;
    }

    @SuppressWarnings("all")
    private Set<String> getPersonalBanResources(int userId) {
        if (userId == 0) {
            return Collections.emptySet();
        }
        List<UserRejectRecord> userRejectRecords = userRejectRecordService.selectList(
                Condition.create().eq("user_id", userId).and().eq("del_flag", 0));
        if (CollectionUtils.isNotEmpty(userRejectRecords)) {
            return userRejectRecords.stream().map(UserRejectRecord::getResId).collect(Collectors.toSet());
        }
        return Collections.emptySet();
    }

    @SuppressWarnings("all")
    private Set<String> getPersonalBanAuthors(int userId) {
        if (userId == 0) {
            return Collections.emptySet();
        }
        List<UserBanRecord> userBanRecords = userBanRecordService.selectList(
                Condition.create().eq("user_id", userId).and().eq("del_flag", 0));
        if (CollectionUtils.isNotEmpty(userBanRecords)) {
            return userBanRecords.stream().map(u -> String.valueOf(u.getAuthorId())).collect(Collectors.toSet());
        }
        return Collections.emptySet();
    }

    private List<ArticleResource> getPersonalRecommend(int userId) {
        if (userId == 0) {
            return unifiedArticleResources;
        }
        if (CollectionUtils.isEmpty(personalRecommendContent.get(String.valueOf(userId)))) {
            return unifiedArticleResources;
        }
        return personalRecommendContent.get(String.valueOf(userId));
    }

    private Map<ArticleTagEnum, List<ArticleResource>> getPersonalTagArticles(int userId) {
        if (userId == 0) {
            return tagArticleMap;
        }
        if (MapUtils.isEmpty(personalTagArticleMap.get(String.valueOf(userId)))) {
            return tagArticleMap;
        }
        return personalTagArticleMap.get(String.valueOf(userId));
    }

    @SuppressWarnings("all")
    private Set<Integer> findUserNeedCustomContent() {
        List<UserRejectRecord> rejectRecords = userRejectRecordService.selectList(
                Condition.create().eq("del_flag", 0));
        List<UserBanRecord> banRecords = userBanRecordService.selectList(
                Condition.create().eq("del_flag", 0));
        Set<Integer> users = Sets.newHashSet();
        if (CollectionUtils.isNotEmpty(rejectRecords)) {
            users.addAll(rejectRecords.stream().map(UserRejectRecord::getUserId).collect(Collectors.toSet()));
        }
        if (CollectionUtils.isNotEmpty(banRecords)) {
            users.addAll(banRecords.stream().map(UserBanRecord::getUserId).collect(Collectors.toSet()));
        }
        return users;
    }

}
