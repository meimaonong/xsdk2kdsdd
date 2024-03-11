package com.art.app.web.service;

import com.alibaba.fastjson.JSON;
import com.art.app.common.Constants;
import com.art.app.common.enums.ResponseCodeEnum;
import com.art.app.common.enums.VerifyCodeEnum;
import com.art.app.common.util.DatetimeUtils;
import com.art.app.common.util.TokenUtils;
import com.art.app.mw.aliyun.SmsManager;
import com.art.app.mw.redis.RedisUtil;
import com.art.app.orm.entity.ArtistApply;
import com.art.app.orm.entity.User;
import com.art.app.orm.entity.UserFocusAssociation;
import com.art.app.orm.entity.UserInfo;
import com.art.app.orm.entity.UserPushToken;
import com.art.app.orm.entity.UserToken;
import com.art.app.orm.entity.VerificationCode;
import com.art.app.orm.service.ArtistApplyService;
import com.art.app.orm.service.UserFocusAssociationService;
import com.art.app.orm.service.UserInfoService;
import com.art.app.orm.service.UserPushTokenService;
import com.art.app.orm.service.UserService;
import com.art.app.orm.service.UserTokenService;
import com.art.app.orm.service.VerificationCodeService;
import com.art.app.web.bean.BizException;
import com.art.app.web.bean.PushToken;
import com.art.app.web.bean.response.LoginResult;
import com.baomidou.mybatisplus.mapper.Condition;
import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class LoginService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginService.class);

    @Autowired
    private UserService userService;
    @Autowired
    private ContentService contentService;
    @Autowired
    private UserTokenService userTokenService;
    @Autowired
    private UserPushTokenService userPushTokenService;
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private VerificationCodeService verificationCodeService;
    @Autowired
    private UserFocusAssociationService focusAssociationService;
    @Autowired
    private ArtistApplyService artistApplyService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private SmsManager smsManager;
    @Autowired
    private UserBiz userBiz;
    @Value("${env.sms}")
    private String otpCode;
    @Value("${env.sms.flag}")
    private Boolean smsFlag;

    private static final char[] NUMBERS = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
    private static final char[] LETTERS =
            new char[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p',
                    'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};

    @SuppressWarnings("all")
    public String sendSms(String phone) {
        if (!smsFlag) {
            return otpCode;
        }

        if (Constants.TEST_ACCOUNT.contains(phone)) {
            LOGGER.warn("test account access:{}", phone);
            return Constants.TEST_VERIFY_CODE;
        }
        String lastCode = getLatestSms(phone);
        if (StringUtils.isNotBlank(lastCode)) {
            return lastCode;
        }
        List<VerificationCode> historyCodes = verificationCodeService.selectList(Condition.create().eq("mobile", phone)
                .orderDesc(Collections.singleton("created_at")).last("limit 10"));
        Date now = DatetimeUtils.now();
        if (CollectionUtils.isNotEmpty(historyCodes)) {
            // 一天最多10条
            Date today = DatetimeUtils.getStartOfToday();
            long sentToday = historyCodes.stream().filter(h -> h.getCreatedAt().compareTo(today) >= 0).count();
            if (sentToday > Constants.ONE_DAY_SMS_LIMIT) {
                throw new BizException(ResponseCodeEnum.TOO_FREQUENT_REQUEST);
            }
        }
        String code = buildVerifyCode();
        User user = userService.selectOne(Condition.create().eq("mobile", phone)
                .and().eq("disabled", 0).and().eq("del_flag", 0));
        VerificationCode newCode = new VerificationCode();
        newCode.setMobile(phone);
        newCode.setCode(code);
        newCode.setCreatedAt(now);
        newCode.setType(VerifyCodeEnum.REGISTER.getType());
        if (Objects.nonNull(user)) {
            newCode.setType(VerifyCodeEnum.LOGIN.getType());
        }
        newCode.setExpireAt(DateUtils.addMinutes(newCode.getCreatedAt(), 1));
        verificationCodeService.insert(newCode);
        markSms(phone, code);
        try {
            smsManager.sendSms(phone, code);
        } catch (Exception e) {
            LOGGER.error("sendSms fail phone:{}, code:{}", phone, code, e);
            throw new BizException(ResponseCodeEnum.SYSTEM_ERROR);
        }
        return code;
    }

    @SuppressWarnings("all")
    public Map<String, Object> validLogin(String token) {
        Map<String, Object> map = Maps.newHashMap();
        try {
            int userId = TokenUtils.getUserIdByToken(token);
            if (userId > 0) {
                map.put("isLogin", true);
            }
            UserInfo userInfo = userInfoService.selectOne(Condition.create()
                    .eq("user_id", userId).and().eq("del_flag", 0));
            if (Objects.nonNull(userInfo)) {
                map.put("degree", userInfo.getDegree());
            }
        } catch (Exception e) {
            map.put("isLogin", false);
            map.put("degree", 0);
        }
        return map;
    }

    @SuppressWarnings("all")
    @Transactional(rollbackFor = Exception.class)
    public LoginResult login(Map<String, Object> params, String clientId) {
        String phone = params.get("phone").toString();
        String code = params.get("verifyCode").toString();
        if (Constants.TEST_ACCOUNT.contains(phone)) {
            LOGGER.warn("test account login:{}, {}", phone, code);
            if (!Constants.TEST_VERIFY_CODE.equalsIgnoreCase(code)) {
                throw new BizException(ResponseCodeEnum.INVALID_VERIFY_CODE);
            }
        } else {
            if (!otpCode.equalsIgnoreCase(code)) {
                String latestCode = getLatestSms(phone);
                if (StringUtils.isBlank(latestCode) || !latestCode.equals(code)) {
                    throw new BizException(ResponseCodeEnum.INVALID_VERIFY_CODE);
                }
            }
        }

        boolean fromApp = true;
        if (StringUtils.isBlank(clientId)) {
            fromApp = false;
        }
        LoginResult loginResult = new LoginResult();
        User user = userService.selectOne(Condition.create().eq("mobile", phone)
                .and().eq("disabled", 0).and().eq("del_flag", 0));
        int userId = 0;
        if (Objects.nonNull(user)) {
            loginResult.setNewUserFlag(Constants.OLD_USER_FLAG);
            userId = user.getId();
        } else {
            Date now = DatetimeUtils.now();
            User newUser = new User();
            newUser.setMobile(phone);
            newUser.setCreatedAt(now);
            userService.insert(newUser);
            userId = newUser.getId();
            UserInfo newUserInfo = new UserInfo();
            newUserInfo.setCreatedAt(now);
            newUserInfo.setNickName(buildRandomNickName(phone));
            newUserInfo.setAvatar(Constants.DEFAULT_AVATAR);
            newUserInfo.setUserId(userId);
            //处理邀请逻辑
            userBiz.processInvition(phone, userId);
            //处理发券逻辑
            userBiz.processCoupon(userId);
            if (fromApp) {
                newUserInfo.setIsLogin(Constants.APP_LOGIN);
            }
            userInfoService.insert(newUserInfo);
        }

        String token = TokenUtils.buildToken(userId);
        loginResult.setToken(token);
        loginResult.setUserId(userId);
        UserToken userToken = new UserToken();
        userToken.setUserId(userId);
        userToken.setToken(token);
        if (fromApp) {
            userToken.setClientId(clientId);
        }
        disableTokenByUserId(userId);
        userTokenService.insert(userToken);
        if (Objects.nonNull(params.get("push"))) {
            List<PushToken> pushTokens = JSON.parseArray(JSON.toJSONString(params.get("push")), PushToken.class);
            for (PushToken pushToken : pushTokens) {
                if (StringUtils.isBlank(pushToken.getDeviceToken())) {
                    LOGGER.warn("bad push token for:{}", phone);
                    continue;
                }
                UserPushToken userPushToken = new UserPushToken();
                userPushToken.setUserId(userId);
                userPushToken.setSource(pushToken.getChannel());
                userPushToken.setToken(pushToken.getDeviceToken());
                userPushToken.setClientId(clientId);
                userPushToken.setDelFlag(0);
                userPushToken.setCreatedAt(DatetimeUtils.now());
                userPushToken.setUpdatedAt(DatetimeUtils.now());
                userPushTokenService.replace(userPushToken);
            }
        }
        return loginResult;
    }


    public static String buildRandomNickName(String phone) {
        try {
            if (StringUtils.isBlank(phone)) {
                LOGGER.error("bad phone:{}", phone);
            }
            return "xsl_" + buildRandNick() + phone.substring(8);
        } catch (Exception e) {
            LOGGER.error("buildRandomNickName error:{}", phone);
        }
        return "xsl_" + buildRandNick();
    }

    @SuppressWarnings("all")
    public void logout(String token) {
        int user = TokenUtils.getUserIdByToken(token);
        disableTokenByUserId(user);
    }

    @SuppressWarnings("all")
    private void disableTokenByUserId(int userId) {
        UserToken userToken = new UserToken();
        userToken.setDelFlag(1);
        userToken.setUpdatedTime(new Date());
        userTokenService.update(userToken,
                Condition.create().eq("user_id", userId).and().eq("del_flag", 0));
    }

    @SuppressWarnings("all")
    public void repair() {
        List<User> users = userService.selectList(Condition.create().eq("del_flag", 0));
        List<UserInfo> userInfos = userInfoService.selectList(Condition.create().eq("del_flag", 0));
        Map<Integer, String> idMobileMap = users.stream().collect(Collectors.toMap(User::getId, User::getMobile));
        Date now = DatetimeUtils.now();
        for (UserInfo userInfo : userInfos) {
            if (StringUtils.isBlank(userInfo.getNickName())) {
                userInfo.setNickName(buildRandomNickName(idMobileMap.get(userInfo.getUserId())));
                userInfo.setUpdatedAt(now);
                userInfoService.update(userInfo, Condition.create().eq("user_id", userInfo.getUserId()));
            }
        }
    }

    @SuppressWarnings("all")
    public void editProfile(String token, Map<String, Object> params) {
        int userId = TokenUtils.getUserIdByToken(token);
        UserInfo userInfo = new UserInfo();
        userInfo.setUpdatedAt(new Date());
        if (Objects.nonNull(params.get("nickName"))) {
            String nickName = String.valueOf(params.get("nickName"));
            if (nickName.length() > 15) {
                throw new BizException(ResponseCodeEnum.PARAM_ERROR);
            }
            userInfo.setNickName(nickName);
        }
        if (Objects.nonNull(params.get("avatar"))) {
            String avatar = String.valueOf(params.get("avatar"));
            userInfo.setAvatar(avatar);
        }
        if (Objects.nonNull(params.get("bgImgUrl"))) {
            String bgImgUrl = String.valueOf(params.get("bgImgUrl"));
            userInfo.setSpaceImgUrl(bgImgUrl);
        }
        if (Objects.nonNull(params.get("motto"))) {
            String motto = String.valueOf(params.get("motto"));
            userInfo.setMotto(motto);
        }
        try {
            if (!userInfoService.update(userInfo,
                    Condition.create().eq("user_id", userId).and().eq("del_flag", 0))) {
                LOGGER.warn("editProfile error:{}", token);
                throw new BizException(ResponseCodeEnum.SYSTEM_ERROR);
            }
        } catch (Exception e) {
            throw new BizException(ResponseCodeEnum.DUPLICATED_NICKNAME);
        }
    }

    @SuppressWarnings("all")
    public void applyArtist(String token) {
        int userId = TokenUtils.getUserIdByToken(token);
        if (userId > 0) {
//            redisUtil.set("artist_apply:" + userId, System.currentTimeMillis());
            ArtistApply artistApplyDb = artistApplyService.selectOne(Condition.create().
                    eq("user_id", userId).and().eq("del_flag", 0)
                    .and().eq("is_process", 0));
            if (Objects.isNull(artistApplyDb)) {
                ArtistApply artistApply = new ArtistApply();
                artistApply.setCreatedAt(new Date());
                artistApply.setUserId(userId);
                artistApplyService.insert(artistApply);
            }
            return;
        }
        throw new BizException(ResponseCodeEnum.INVALID_TOKEN);
    }

    @SuppressWarnings("all")
    public void focusAction(String token, int focusAt, int action) {
        int userId = TokenUtils.getUserIdByToken(token);
        User focusAtUser = userService.selectOne(
                Condition.create().eq("id", focusAt).and().eq("del_flag", 0));
        if (Objects.isNull(focusAtUser)) {
            throw new BizException(ResponseCodeEnum.PARAM_ERROR);
        }
        if (userId == focusAtUser.getId()) {
            throw new BizException(ResponseCodeEnum.FOCUS_ERROR);
        }
        Date now = DatetimeUtils.now();
        UserFocusAssociation previousFocus = focusAssociationService.selectOne(
                Condition.create().eq("user_id", userId)
                        .and().eq("focus_id", focusAt));
        if (Constants.CANCEL_FOCUS == action) {
            if (Objects.isNull(previousFocus) || previousFocus.getDelFlag() == 1) {
                throw new BizException(ResponseCodeEnum.PARAM_ERROR);
            }
            previousFocus.setUpdatedAt(now);
            previousFocus.setDelFlag(1);
            focusAssociationService.update(previousFocus, Condition.create().eq("id", previousFocus.getId()));
        } else if (Constants.FOCUS == action) {
            if (Objects.isNull(previousFocus) || previousFocus.getDelFlag() == 1) {
                UserFocusAssociation userFocusAssociation = new UserFocusAssociation();
                Optional.ofNullable(previousFocus).ifPresent(p -> userFocusAssociation.setId(p.getId()));
                if (Objects.isNull(previousFocus)) {
                    userFocusAssociation.setCreatedAt(now);
                }
                userFocusAssociation.setUserId(userId);
                userFocusAssociation.setFocusId(focusAt);
                userFocusAssociation.setDelFlag(0);
                focusAssociationService.insertOrUpdate(userFocusAssociation);
            }
        } else {
            throw new BizException(ResponseCodeEnum.PARAM_ERROR);
        }
        contentService.buildFocusByUserId(userId);
    }

    static String buildVerifyCode() {
        char[] randomVerifyCode = new char[4];
        for (int i = 0; i < 4; i++) {
            randomVerifyCode[i] = NUMBERS[new Random().nextInt(10)];
        }
        return String.valueOf(randomVerifyCode);
    }

    static String buildRandNick() {
        char[] randomNick = new char[4];
        for (int i = 0; i < 4; i++) {
            randomNick[i] = LETTERS[new Random().nextInt(26)];
        }
        return String.valueOf(randomNick);
    }

    /**
     * 在验证码失效前不允许再次请求获取验证码
     *
     * @param phone
     * @return
     */
    private String getLatestSms(String phone) {
        String code = null;
        try {
            Object latest = redisUtil.get(Constants.VERIFY_CODE_PREFIX + phone);
            if (Objects.nonNull(latest)) {
                code = String.valueOf(latest);
            }
        } catch (Exception e) {
            LOGGER.error("getCurrentVerifyCode error for :{}", phone, e);
        }
        return code;
    }

    private void markSms(String phone, String code) {
        try {
            redisUtil.set(Constants.VERIFY_CODE_PREFIX + phone, code, 60);
        } catch (Exception e) {
            LOGGER.error("getCurrentVerifyCode error for :{}", phone, e);
        }
    }
}
