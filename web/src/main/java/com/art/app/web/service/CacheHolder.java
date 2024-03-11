package com.art.app.web.service;

import com.art.app.orm.entity.GeneralKeyValue;
import com.art.app.orm.entity.MemberDescriptionInfo;
import com.art.app.orm.service.GeneralKeyValueService;
import com.art.app.orm.service.MemberDescriptionInfoService;
import com.art.app.payment.utils.JackJsonUtil;
import com.art.app.web.bean.response.MemberHomeResult;
import com.baomidou.mybatisplus.mapper.Condition;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class CacheHolder {

    private static final Logger LOGGER = LoggerFactory.getLogger(CacheHolder.class);

    @Autowired
    private MemberDescriptionInfoService descriptionInfoService;
    @Autowired
    private GeneralKeyValueService generalKeyValueService;

    @SuppressWarnings("all")
    private final LoadingCache<String, String> generalKeyValues = CacheBuilder.newBuilder()
            .maximumSize(1000)
            .expireAfterWrite(1, TimeUnit.MINUTES)
            .expireAfterAccess(5, TimeUnit.MINUTES)
            .recordStats()
            //CacheLoader类 实现自动加载
            .build(new CacheLoader<String, String>() {
                @Override
                public String load(String key) {
                    return reloadGeneralKeyValues(key);
                }
            });

    @SuppressWarnings("all")
    private final LoadingCache<String, MemberHomeResult.MemberBenefitsBean> memberShipDescriptionCache = CacheBuilder.newBuilder()
            .maximumSize(1000)
            .expireAfterWrite(1, TimeUnit.MINUTES)
            .expireAfterAccess(5, TimeUnit.MINUTES)
            .recordStats()
            //CacheLoader类 实现自动加载
            .build(new CacheLoader<String, MemberHomeResult.MemberBenefitsBean>() {
                @Override
                public MemberHomeResult.MemberBenefitsBean load(String key) {
                    return buildByVersion(key);
                }
            });

    @SuppressWarnings("all")
    public String reloadGeneralKeyValues(String key) {
        GeneralKeyValue generalKeyValue = generalKeyValueService.selectOne(
                Condition.create().eq("del_flag", 0).and().eq("item", key));
        if (Objects.nonNull(generalKeyValue)) {
            return generalKeyValue.getContent();
        }
        return "";
    }

    public MemberHomeResult.MemberBenefitsBean getMemberDescriptionByVersion(String version) {
        try {
            return memberShipDescriptionCache.get(version);
        } catch (Exception e) {
            LOGGER.error("getMemberDescriptionByMemberVersion:{}", version, e);
        }
        MemberHomeResult.MemberBenefitsBean memberBenefitsBean = buildByVersion(version);
        memberShipDescriptionCache.put(version, memberBenefitsBean);
        return memberBenefitsBean;
    }

    public String getGeneralKeyValue(String key) {
        try {
            return generalKeyValues.get(key);
        } catch (Exception e) {
            LOGGER.error("getGeneralKeyValue:{}", key, e);
        }
        String value = reloadGeneralKeyValues(key);
        generalKeyValues.put(key, value);
        return value;
    }

    @SuppressWarnings("all")
    private MemberHomeResult.MemberBenefitsBean buildByVersion(String version) {
        List<MemberDescriptionInfo> memberDescriptionInfoList = descriptionInfoService.selectList(
                Condition.create().eq("del_flag", 0)
                        .and().eq("member_level_version", NumberUtils.toInt(version)));
        if (CollectionUtils.isEmpty(memberDescriptionInfoList)) {
            LOGGER.warn("buildByVersion :{} find null", version);
            return null;
        }
        MemberHomeResult.MemberBenefitsBean memberBenefitsBean = new MemberHomeResult.MemberBenefitsBean();
        Map<Integer, MemberDescriptionInfo> memberDescriptionInfoMap = memberDescriptionInfoList
                .stream().collect(Collectors.toMap(MemberDescriptionInfo::getMemberLevel, Function.identity()));
        for (Map.Entry<Integer, MemberDescriptionInfo> entry : memberDescriptionInfoMap.entrySet()) {
            MemberHomeResult.MemberBenefitsBean.BenefitBean benefitBean = new MemberHomeResult.MemberBenefitsBean.BenefitBean();
            benefitBean.setDesc(JackJsonUtil.toBean(entry.getValue().getContent(), new TypeReference<List<Map<String, Object>>>() {
            }));
            benefitBean.setPrice(entry.getValue().getPrice().toString());
            benefitBean.setPromotion(entry.getValue().getPromotion().toString());
            if (entry.getKey().equals(1)) {
                memberBenefitsBean.setNormal(benefitBean);
            } else {
                memberBenefitsBean.setSenior(benefitBean);
            }
        }
        return memberBenefitsBean;
    }

}
