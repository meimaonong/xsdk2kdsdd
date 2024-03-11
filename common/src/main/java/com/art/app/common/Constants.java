package com.art.app.common;

import com.art.app.common.enums.ResourceTypeEnum;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class Constants {

    public static final String JWT_SECRET = "";

    public static final String VERIFY_CODE_PREFIX = "redis_vc_";
    public static final String HOMEPAGE_CONFIG = "redis_hp_config";

    public static final List<String> TEST_ACCOUNT = Lists.newArrayList(
            "16810246688", "16810246699", "16910246688", "16910246699");
    public static final String TEST_VERIFY_CODE = "3478";

    public static final int PLATFORM_IOS = 2;
    public static final int PLATFORM_ANDROID = 1;
    public static final int PLATFORM_ALL = 0;

    public static final Set<Integer> PLATFORMS = Sets.newHashSet(PLATFORM_IOS, PLATFORM_ANDROID, PLATFORM_ALL);

    public static final String ARTICLE_LIKE_HASHMAP = "redis_article_like_hash";
    public static final String PRODUCT_LIKE_HASHMAP = "redis_product_like_hash";
    public static final String TRAINING_LIKE_HASHMAP = "redis_training_like_hash";
    public static final String COURSE_LIKE_HASHMAP = "redis_course_like_hash";
    public static final String ARTIST_LIKE_HASHMAP = "redis_artist_like_hash";
    public static final String EXHIBITION_LIKE_HASHMAP = "redis_exhibition_like_hash";
    public static final String VIDEO_COURSE_LIKE_HASHMAP = "redis_video_course_like_hash";
    public static final String SKETCHING_LIKE_HASHMAP = "redis_sketching_like_hash";

    public static final String ARTICLE_LIKE_SET = "redis_article_like_set";
    public static final String PRODUCT_LIKE_SET = "redis_product_like_set";
    public static final String TRAINING_LIKE_SET = "redis_training_like_set";
    public static final String COURSE_LIKE_SET = "redis_course_like_set";
    public static final String ARTIST_LIKE_SET = "redis_artist_like_set";
    public static final String EXHIBITION_LIKE_SET = "redis_exhibition_like_set";
    public static final String VIDEO_COURSE_LIKE_SET = "redis_video_course_like_set";
    public static final String SKETCHING_LIKE_SET = "redis_sketching_like_set";

    public static final String LIKE_MUTEX = "redis_like_mutex";
    public static final String VIEW_MUTEX = "redis_view_mutex";

    public static final String ARTICLE_VIEW_HASHMAP = "redis_article_view_hash";
    public static final String PRODUCT_VIEW_HASHMAP = "redis_product_view_hash";
    public static final String TRAINING_VIEW_HASHMAP = "redis_training_view_hash";
    public static final String COURSE_VIEW_HASHMAP = "redis_course_view_hash";
    public static final String ARTIST_VIEW_HASHMAP = "redis_artist_view_hash";
    public static final String EXHIBITION_VIEW_HASHMAP = "redis_exhibition_view_hash";
    public static final String VIDEO_COURSE_VIEW_HASHMAP = "redis_video_course_view_hash";
    public static final String SKETCHING_VIEW_HASHMAP = "redis_sketching_view_hash";

    public static final String ARTICLE_VIEW_SET = "redis_article_view_set";
    public static final String PRODUCT_VIEW_SET = "redis_product_view_set";
    public static final String TRAINING_VIEW_SET = "redis_training_view_set";
    public static final String COURSE_VIEW_SET = "redis_course_view_set";
    public static final String ARTIST_VIEW_SET = "redis_artist_view_set";
    public static final String EXHIBITION_VIEW_SET = "redis_exhibition_view_set";
    public static final String VIDEO_COURSE_VIEW_SET = "redis_video_course_view_set";
    public static final String SKETCHING_VIEW_SET = "redis_sketching_view_set";

    public static final String ORDER_FEEDBACK_PREFIX = "redis_order_feedback_";
    public static final String REFUND_FEEDBACK_PREFIX = "redis_refund_feedback_";
    public static final String ORDER_ASSEMBLE_PREFIX = "redis_order_assemble_";

    public static final int ONE_DAY_SMS_LIMIT = 100;

    public static final int NEW_USER_FLAG = 1;
    public static final int OLD_USER_FLAG = 0;
    public static final int APP_LOGIN = 1;
    public static final int RESOURCE_LIKE = 1;
    public static final int RESOURCE_DISLIKE = 2;
    public static final int FROM_APP = 0;
    public static final int FROM_H5 = 1;
    public static final int DRAFT_LIMIT = 5;
    public static final String DRAFT_FULL = "5";

    public static final int FOCUS = 1;
    public static final int CANCEL_FOCUS = 2;
    public static final int DRAFT = 1;

    public static final String AUDIT_FAIL_REASONN = "*文章内容涉及人身攻击和反政府言论*";
    public static final String DEFAULT_AVATAR = "http://cdn.xs.com/xsl/common/image/head.jpg";

    public static final Map<String, String> RESOURCE_LIKE_KEY_MAP = Maps.newHashMap();
    public static final Map<String, String> RESOURCE_LIKE_SET_KEY_MAP = Maps.newHashMap();
    public static final Map<String, String> RESOURCE_VIEW_KEY_MAP = Maps.newHashMap();
    public static final Map<String, String> RESOURCE_VIEW_SET_KEY_MAP = Maps.newHashMap();

    public static final String MDC_ID = "reqId";

    public static final Integer MAX_PAYMENT_TIME = 60;

    public static final String COUPON_DESC = "coupon_desc";
    public static final String AS_DESC = "ad_desc";
    public static final String CONTRIBUTION_DESC = "contribution_desc";

    static {
        RESOURCE_LIKE_KEY_MAP.put(ResourceTypeEnum.ARTICLE.name(), Constants.ARTICLE_LIKE_HASHMAP);
        RESOURCE_LIKE_KEY_MAP.put(ResourceTypeEnum.PRODUCT.name(), Constants.PRODUCT_LIKE_HASHMAP);
        RESOURCE_LIKE_KEY_MAP.put(ResourceTypeEnum.TRAINING.name(), Constants.TRAINING_LIKE_HASHMAP);
        RESOURCE_LIKE_KEY_MAP.put(ResourceTypeEnum.COURSE.name(), Constants.COURSE_LIKE_HASHMAP);
        RESOURCE_LIKE_KEY_MAP.put(ResourceTypeEnum.ARTIST.name(), Constants.ARTIST_LIKE_HASHMAP);
        RESOURCE_LIKE_KEY_MAP.put(ResourceTypeEnum.EXHIBITION.name(), Constants.EXHIBITION_LIKE_HASHMAP);
        RESOURCE_LIKE_KEY_MAP.put(ResourceTypeEnum.VIDEO_COURSE.name(), Constants.VIDEO_COURSE_LIKE_HASHMAP);
        RESOURCE_LIKE_KEY_MAP.put(ResourceTypeEnum.SKETCHING_ACTIVITY.name(), Constants.SKETCHING_LIKE_HASHMAP);

        RESOURCE_LIKE_SET_KEY_MAP.put(ResourceTypeEnum.ARTICLE.name(), Constants.ARTICLE_LIKE_SET);
        RESOURCE_LIKE_SET_KEY_MAP.put(ResourceTypeEnum.PRODUCT.name(), Constants.PRODUCT_LIKE_SET);
        RESOURCE_LIKE_SET_KEY_MAP.put(ResourceTypeEnum.TRAINING.name(), Constants.TRAINING_LIKE_SET);
        RESOURCE_LIKE_SET_KEY_MAP.put(ResourceTypeEnum.COURSE.name(), Constants.COURSE_LIKE_SET);
        RESOURCE_LIKE_SET_KEY_MAP.put(ResourceTypeEnum.ARTIST.name(), Constants.ARTIST_LIKE_SET);
        RESOURCE_LIKE_SET_KEY_MAP.put(ResourceTypeEnum.EXHIBITION.name(), Constants.EXHIBITION_LIKE_SET);
        RESOURCE_LIKE_SET_KEY_MAP.put(ResourceTypeEnum.VIDEO_COURSE.name(), Constants.VIDEO_COURSE_LIKE_SET);
        RESOURCE_LIKE_SET_KEY_MAP.put(ResourceTypeEnum.SKETCHING_ACTIVITY.name(), Constants.SKETCHING_LIKE_SET);

        RESOURCE_VIEW_KEY_MAP.put(ResourceTypeEnum.ARTICLE.name(), Constants.ARTICLE_VIEW_HASHMAP);
        RESOURCE_VIEW_KEY_MAP.put(ResourceTypeEnum.PRODUCT.name(), Constants.PRODUCT_VIEW_HASHMAP);
        RESOURCE_VIEW_KEY_MAP.put(ResourceTypeEnum.TRAINING.name(), Constants.TRAINING_VIEW_HASHMAP);
        RESOURCE_VIEW_KEY_MAP.put(ResourceTypeEnum.COURSE.name(), Constants.COURSE_VIEW_HASHMAP);
        RESOURCE_VIEW_KEY_MAP.put(ResourceTypeEnum.ARTIST.name(), Constants.ARTIST_VIEW_HASHMAP);
        RESOURCE_VIEW_KEY_MAP.put(ResourceTypeEnum.EXHIBITION.name(), Constants.EXHIBITION_VIEW_HASHMAP);
        RESOURCE_VIEW_KEY_MAP.put(ResourceTypeEnum.VIDEO_COURSE.name(), Constants.VIDEO_COURSE_VIEW_HASHMAP);
        RESOURCE_VIEW_KEY_MAP.put(ResourceTypeEnum.SKETCHING_ACTIVITY.name(), Constants.SKETCHING_VIEW_HASHMAP);

        RESOURCE_VIEW_SET_KEY_MAP.put(ResourceTypeEnum.ARTICLE.name(), Constants.ARTICLE_VIEW_SET);
        RESOURCE_VIEW_SET_KEY_MAP.put(ResourceTypeEnum.PRODUCT.name(), Constants.PRODUCT_VIEW_SET);
        RESOURCE_VIEW_SET_KEY_MAP.put(ResourceTypeEnum.TRAINING.name(), Constants.TRAINING_VIEW_SET);
        RESOURCE_VIEW_SET_KEY_MAP.put(ResourceTypeEnum.COURSE.name(), Constants.COURSE_VIEW_SET);
        RESOURCE_VIEW_SET_KEY_MAP.put(ResourceTypeEnum.ARTIST.name(), Constants.ARTIST_VIEW_SET);
        RESOURCE_VIEW_SET_KEY_MAP.put(ResourceTypeEnum.EXHIBITION.name(), Constants.EXHIBITION_VIEW_SET);
        RESOURCE_VIEW_SET_KEY_MAP.put(ResourceTypeEnum.VIDEO_COURSE.name(), Constants.VIDEO_COURSE_VIEW_SET);
        RESOURCE_VIEW_SET_KEY_MAP.put(ResourceTypeEnum.SKETCHING_ACTIVITY.name(), Constants.SKETCHING_VIEW_SET);
    }

    private Constants() {
    }
}
