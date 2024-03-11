package com.art.app.web.controller;

import com.alibaba.fastjson.JSON;
import com.art.app.common.Constants;
import com.art.app.common.enums.ResourceTypeEnum;
import com.art.app.common.enums.ResponseCodeEnum;
import com.art.app.common.util.Base64Utils;
import com.art.app.web.annotation.LoginRequired;
import com.art.app.web.annotation.SignaturePass;
import com.art.app.web.bean.BizException;
import com.art.app.web.bean.request.RequestVo;
import com.art.app.web.bean.response.ResponseVo;
import com.art.app.web.service.ContentService;
import com.google.common.collect.Maps;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Objects;

@RestController
public class ContentController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ContentController.class);

    @Autowired
    private ContentService contentService;

    @GetMapping(value = "/message/push")
    @SignaturePass
    public ResponseVo pushMsg(@RequestBody RequestVo requestVo) {
        LOGGER.info("pushMsg:{}", requestVo);
        if (MapUtils.isEmpty(requestVo.getBizParams())
                || Objects.isNull(requestVo.getBizParams().get("msgId"))
                || Objects.isNull(requestVo.getBizParams().get("target"))
                || Objects.isNull(requestVo.getBizParams().get("platform"))) {
            throw new BizException(ResponseCodeEnum.PARAM_ERROR);
        }
        ResponseVo responseVo = new ResponseVo(ResponseCodeEnum.SUCCESS);
        contentService.pushMsg(requestVo.getBizParams());
        return responseVo;
    }

    /**
     * 首页tab配置
     *
     * @return
     */
    @GetMapping(value = "/homepage/config")
    @SignaturePass
    public ResponseVo getHomePageConfig(@RequestParam(value = "data") String data) {
        LOGGER.info("getHomePageConfig");
        RequestVo requestVo = JSON.parseObject(Base64Utils.decodeParams(data), RequestVo.class);
        ResponseVo responseVo = new ResponseVo(ResponseCodeEnum.SUCCESS);
        responseVo.setData(contentService.getHomePageConfig(requestVo.getBaseParams()));
        return responseVo;
    }

    /**
     * 首页轮播接口
     *
     * @return
     */
    @GetMapping(value = "/carousel")
    @SignaturePass
    public ResponseVo carousel() {
        LOGGER.info("carousel");
        ResponseVo responseVo = new ResponseVo(ResponseCodeEnum.SUCCESS);
        responseVo.setData(contentService.carousel());
        return responseVo;
    }

    /**
     * 首页各tab列表
     *
     * @param data
     * @return
     */
    @GetMapping(value = "/homepage/list")
    @SignaturePass
    @SuppressWarnings("all")
    public ResponseVo homePageList(@RequestParam(value = "data") String data) {
        RequestVo requestVo = JSON.parseObject(Base64Utils.decodeParams(data), RequestVo.class);
        LOGGER.info("homePageList:{}", requestVo);
        if (MapUtils.isEmpty(requestVo.getBizParams())
                || Objects.isNull(requestVo.getBizParams().get("type"))) {
            throw new BizException(ResponseCodeEnum.PARAM_ERROR);
        }
        ResponseVo responseVo = new ResponseVo(ResponseCodeEnum.SUCCESS);
        responseVo.setData(contentService.homePageList(requestVo.getBizParams(), requestVo.getBaseParams().getToken()));
        return responseVo;
    }

    /**
     * 精选各tab列表
     *
     * @param data
     * @return
     */
    @GetMapping(value = "/select/list")
    @SuppressWarnings("all")
    @SignaturePass
    public ResponseVo selectList(@RequestParam(value = "data") String data) {
        RequestVo requestVo = JSON.parseObject(Base64Utils.decodeParams(data), RequestVo.class);
        LOGGER.info("selectList:{}", requestVo);
        if (MapUtils.isEmpty(requestVo.getBizParams())
                || Objects.isNull(requestVo.getBizParams().get("type"))
                || Objects.isNull(requestVo.getBizParams().get("pageIndex"))
                || Objects.isNull(requestVo.getBizParams().get("pageSize"))) {
            throw new BizException(ResponseCodeEnum.PARAM_ERROR);
        }
        ResponseVo responseVo = new ResponseVo(ResponseCodeEnum.SUCCESS);
        responseVo.setData(contentService.selectList(requestVo.getBizParams(), requestVo.getBaseParams().getToken()));
        return responseVo;
    }


    /**
     * 精选各tab列表
     *
     * @param data
     * @return
     */
    @GetMapping(value = "/select/detail")
    @SignaturePass
    @SuppressWarnings("all")
    public ResponseVo selectDetail(@RequestParam(value = "data") String data) {
        RequestVo requestVo = JSON.parseObject(Base64Utils.decodeParams(data), RequestVo.class);
        LOGGER.info("selectDetail:{}", requestVo);
        if (MapUtils.isEmpty(requestVo.getBizParams())
                || Objects.isNull(requestVo.getBizParams().get("resourceId"))) {
            throw new BizException(ResponseCodeEnum.PARAM_ERROR);
        }
        ResponseVo responseVo = new ResponseVo(ResponseCodeEnum.SUCCESS);
        responseVo.setData(contentService.selectDetail(
                requestVo.getBizParams(), requestVo.getBaseParams().getToken(), requestVo.getBaseParams().getSource()));
        return responseVo;
    }

    /**
     * 首页各tab列表
     *
     * @param data
     * @return
     */
    @GetMapping(value = "/draft/list")
    @LoginRequired
    @SuppressWarnings("all")
    public ResponseVo draftList(@RequestParam(value = "data") String data) {
        RequestVo requestVo = JSON.parseObject(Base64Utils.decodeParams(data), RequestVo.class);
        LOGGER.info("draftList:{}", requestVo);
        ResponseVo responseVo = new ResponseVo(ResponseCodeEnum.SUCCESS);
        responseVo.setData(contentService.draftList(requestVo.getBaseParams().getToken()));
        return responseVo;
    }

    /**
     * @param data
     * @return
     */
    @GetMapping(value = "/training/list")
    @SignaturePass
    public ResponseVo trainingList(@RequestParam(value = "data") String data) {
        RequestVo requestVo = JSON.parseObject(Base64Utils.decodeParams(data), RequestVo.class);
        LOGGER.info("trainingList:{}", requestVo);
        if (MapUtils.isEmpty(requestVo.getBizParams())
                || Objects.isNull(requestVo.getBizParams().get("pageIndex"))
                || Objects.isNull(requestVo.getBizParams().get("pageSize"))) {
            throw new BizException(ResponseCodeEnum.PARAM_ERROR);
        }
        ResponseVo responseVo = new ResponseVo(ResponseCodeEnum.SUCCESS);
        responseVo.setData(contentService.trainingList(requestVo.getBizParams()));
        return responseVo;
    }

    /**
     * @param data
     * @return
     */
    @GetMapping(value = "/training/detail")
    @SignaturePass
    @SuppressWarnings("all")
    public ResponseVo trainingDetail(@RequestParam(value = "data") String data) {
        RequestVo requestVo = JSON.parseObject(Base64Utils.decodeParams(data), RequestVo.class);
        LOGGER.info("trainingDetail:{}", requestVo);
        if (MapUtils.isEmpty(requestVo.getBizParams())
                || Objects.isNull(requestVo.getBizParams().get("resourceId"))) {
            throw new BizException(ResponseCodeEnum.PARAM_ERROR);
        }
        ResponseVo responseVo = new ResponseVo(ResponseCodeEnum.SUCCESS);
        responseVo.setData(contentService.trainingDetail(
                requestVo.getBizParams(), requestVo.getBaseParams().getToken(), requestVo.getBaseParams().getSource()));
        return responseVo;
    }

    /**
     * @param data
     * @return
     */
    @GetMapping(value = "/exhibition/detail")
    @SignaturePass
    @SuppressWarnings("all")
    public ResponseVo exhibitionDetail(@RequestParam(value = "data") String data) {
        RequestVo requestVo = JSON.parseObject(Base64Utils.decodeParams(data), RequestVo.class);
        LOGGER.info("exhibitionDetail:{}", requestVo);
        if (MapUtils.isEmpty(requestVo.getBizParams())
                || Objects.isNull(requestVo.getBizParams().get("resourceId"))) {
            throw new BizException(ResponseCodeEnum.PARAM_ERROR);
        }
        ResponseVo responseVo = new ResponseVo(ResponseCodeEnum.SUCCESS);
        responseVo.setData(contentService.exhibitionDetail(
                requestVo.getBizParams(), requestVo.getBaseParams().getToken(), requestVo.getBaseParams().getSource()));
        return responseVo;
    }

    /**
     * @param data
     * @return
     */
    @GetMapping(value = "/video/list")
    @LoginRequired
    @SignaturePass
    @SuppressWarnings("all")
    public ResponseVo videoList(@RequestParam(value = "data") String data) {
        RequestVo requestVo = JSON.parseObject(Base64Utils.decodeParams(data), RequestVo.class);
        LOGGER.info("videoList:{}", requestVo);
        if (MapUtils.isEmpty(requestVo.getBizParams())
                || Objects.isNull(requestVo.getBizParams().get("pageIndex"))
                || Objects.isNull(requestVo.getBizParams().get("pageSize"))) {
            throw new BizException(ResponseCodeEnum.PARAM_ERROR);
        }
        ResponseVo responseVo = new ResponseVo(ResponseCodeEnum.SUCCESS);
        responseVo.setData(contentService.videoList(requestVo.getBizParams()));
        return responseVo;
    }

    /**
     * @param data
     * @return
     */
    @GetMapping(value = "/video/detail")
    @SignaturePass
    @SuppressWarnings("all")
    public ResponseVo videoDetail(@RequestParam(value = "data") String data) {
        RequestVo requestVo = JSON.parseObject(Base64Utils.decodeParams(data), RequestVo.class);
        LOGGER.info("courseDetail:{}", requestVo);
        if (MapUtils.isEmpty(requestVo.getBizParams())
                || Objects.isNull(requestVo.getBizParams().get("resourceId"))) {
            throw new BizException(ResponseCodeEnum.PARAM_ERROR);
        }
        ResponseVo responseVo = new ResponseVo(ResponseCodeEnum.SUCCESS);
        responseVo.setData(contentService.videoDetail(
                requestVo.getBizParams(), requestVo.getBaseParams().getToken(), requestVo.getBaseParams().getSource()));
        return responseVo;
    }

    /**
     * @param data
     * @return
     */
    @GetMapping(value = "/course/list")
    @SignaturePass
    public ResponseVo courseList(@RequestParam(value = "data") String data) {
        RequestVo requestVo = JSON.parseObject(Base64Utils.decodeParams(data), RequestVo.class);
        LOGGER.info("courseList:{}", requestVo);
        if (MapUtils.isEmpty(requestVo.getBizParams())
                || Objects.isNull(requestVo.getBizParams().get("pageIndex"))
                || Objects.isNull(requestVo.getBizParams().get("pageSize"))) {
            throw new BizException(ResponseCodeEnum.PARAM_ERROR);
        }
        ResponseVo responseVo = new ResponseVo(ResponseCodeEnum.SUCCESS);
        responseVo.setData(contentService.courseList(requestVo.getBizParams()));
        return responseVo;
    }

    /**
     * @param data
     * @return
     */
    @GetMapping(value = "/course/detail")
    @SignaturePass
    @SuppressWarnings("all")
    public ResponseVo courseDetail(@RequestParam(value = "data") String data) {
        RequestVo requestVo = JSON.parseObject(Base64Utils.decodeParams(data), RequestVo.class);
        LOGGER.info("courseDetail:{}", requestVo);
        if (MapUtils.isEmpty(requestVo.getBizParams())
                || Objects.isNull(requestVo.getBizParams().get("resourceId"))) {
            throw new BizException(ResponseCodeEnum.PARAM_ERROR);
        }
        ResponseVo responseVo = new ResponseVo(ResponseCodeEnum.SUCCESS);
        responseVo.setData(contentService.courseDetail(
                requestVo.getBizParams(), requestVo.getBaseParams().getToken(), requestVo.getBaseParams().getSource()));
        return responseVo;
    }

    /**
     * @param data
     * @return
     */
    @GetMapping(value = "/sketch/list")
    @SignaturePass
    @SuppressWarnings("all")
    public ResponseVo sketchList(@RequestParam(value = "data") String data) {
        RequestVo requestVo = JSON.parseObject(Base64Utils.decodeParams(data), RequestVo.class);
        LOGGER.info("sketch:{}", requestVo);
        if (MapUtils.isEmpty(requestVo.getBizParams())
                || Objects.isNull(requestVo.getBizParams().get("pageIndex"))
                || Objects.isNull(requestVo.getBizParams().get("pageSize"))) {
            throw new BizException(ResponseCodeEnum.PARAM_ERROR);
        }
        ResponseVo responseVo = new ResponseVo(ResponseCodeEnum.SUCCESS);
        responseVo.setData(contentService.sketchList(requestVo.getBizParams()));
        return responseVo;
    }

    /**
     * @param data
     * @return
     */
    @GetMapping(value = "/sketch/detail")
    @SignaturePass
    @SuppressWarnings("all")
    public ResponseVo sketchDetail(@RequestParam(value = "data") String data) {
        RequestVo requestVo = JSON.parseObject(Base64Utils.decodeParams(data), RequestVo.class);
        LOGGER.info("sketchDetail:{}", requestVo);
        if (MapUtils.isEmpty(requestVo.getBizParams())
                || Objects.isNull(requestVo.getBizParams().get("resourceId"))) {
            throw new BizException(ResponseCodeEnum.PARAM_ERROR);
        }
        ResponseVo responseVo = new ResponseVo(ResponseCodeEnum.SUCCESS);
        responseVo.setData(contentService.sketchDetail(
                requestVo.getBizParams(), requestVo.getBaseParams().getToken(), requestVo.getBaseParams().getSource()));
        return responseVo;
    }

    /**
     * 首页搜索
     *
     * @param data
     * @return
     */
    @GetMapping(value = "/search")
    @SuppressWarnings("all")
    public ResponseVo search(@RequestParam(value = "data") String data) {
        RequestVo requestVo = JSON.parseObject(Base64Utils.decodeParams(data), RequestVo.class);
        LOGGER.info("search:{}", requestVo);
        if (MapUtils.isEmpty(requestVo.getBizParams())
                || Objects.isNull(requestVo.getBizParams().get("keywords"))) {
            throw new BizException(ResponseCodeEnum.PARAM_ERROR);
        }
        ResponseVo responseVo = new ResponseVo(ResponseCodeEnum.SUCCESS);
        responseVo.setData(contentService.homePageSearch(requestVo.getBizParams()));
        return responseVo;
    }

    /**
     * 搜索热词
     *
     * @param data
     * @return
     */
    @GetMapping(value = "/search/hot")
    @SuppressWarnings("all")
    public ResponseVo searchHot(@RequestParam(value = "data") String data) {
        RequestVo requestVo = JSON.parseObject(Base64Utils.decodeParams(data), RequestVo.class);
        LOGGER.info("searchHot:{}", requestVo);
        ResponseVo responseVo = new ResponseVo(ResponseCodeEnum.SUCCESS);
        responseVo.setData(contentService.searchHot());
        return responseVo;
    }

    /**
     * 首页搜索列表
     *
     * @param data
     * @return
     */
    @GetMapping(value = "/search/list")
    @SuppressWarnings("all")
    public ResponseVo searchList(@RequestParam(value = "data") String data) {
        RequestVo requestVo = JSON.parseObject(Base64Utils.decodeParams(data), RequestVo.class);
        LOGGER.info("search:{}", requestVo);
        if (MapUtils.isEmpty(requestVo.getBizParams())
                || Objects.isNull(requestVo.getBizParams().get("type"))
                || Objects.isNull(requestVo.getBizParams().get("keywords"))) {
            throw new BizException(ResponseCodeEnum.PARAM_ERROR);
        }
        ResponseVo responseVo = new ResponseVo(ResponseCodeEnum.SUCCESS);
        responseVo.setData(contentService.searchList(requestVo.getBizParams(), requestVo.getBaseParams().getToken()));
        return responseVo;
    }

    /**
     * 资源详情页
     *
     * @param data
     * @return
     */
    @GetMapping(value = "/article/detail")
    @SignaturePass
    @SuppressWarnings("all")
    public ResponseVo articleDetail(@RequestParam(value = "data") String data) {
        RequestVo requestVo = JSON.parseObject(Base64Utils.decodeParams(data), RequestVo.class);
        LOGGER.info("getResourceDetail:{}", requestVo);
        if (MapUtils.isEmpty(requestVo.getBizParams())
                || Objects.isNull(requestVo.getBizParams().get("resourceId"))) {
            throw new BizException(ResponseCodeEnum.PARAM_ERROR);
        }
        ResponseVo responseVo = new ResponseVo(ResponseCodeEnum.SUCCESS);
        responseVo.setData(contentService.getArticleDetail(
                requestVo.getBizParams(), requestVo.getBaseParams().getToken(), requestVo.getBaseParams().getSource()));
        return responseVo;
    }

    /**
     * 对资源的点赞/取消点赞
     *
     * @param requestVo
     * @return
     */
    @PostMapping(value = "/resource/like")
    @SignaturePass
    @LoginRequired
    public ResponseVo likeResource(@RequestBody RequestVo requestVo) {
        LOGGER.info("likeResource:{}", requestVo);
        Map<String, Object> params = requestVo.getBizParams();
        if (Objects.isNull(params)
                || Objects.isNull(params.get("type"))
                || Objects.isNull(params.get("resourceId"))
                || Objects.isNull(params.get("action"))) {
            throw new BizException(ResponseCodeEnum.PARAM_ERROR);
        }
        ResponseVo responseVo = new ResponseVo(ResponseCodeEnum.SUCCESS);
        contentService.likeResource(requestVo.getBizParams(), requestVo.getBaseParams().getToken());
        return responseVo;
    }

    /**
     * 对资源的点赞/取消点赞
     *
     * @param requestVo
     * @return
     */
    @PostMapping(value = "/publish")
    @SignaturePass
    @LoginRequired
    public ResponseVo publish(@RequestBody RequestVo requestVo) {
        LOGGER.info("publish:{}", requestVo);
        Map<String, Object> params = requestVo.getBizParams();
        if (Objects.isNull(params)
                || Objects.isNull(params.get("title"))
                || Objects.isNull(params.get("mode"))
                || Objects.isNull(params.get("type"))
                || Objects.isNull(params.get("blocks"))) {
            throw new BizException(ResponseCodeEnum.PARAM_ERROR);
        }
        ResponseVo responseVo = new ResponseVo(ResponseCodeEnum.SUCCESS);
        String publishRet = contentService.publish(requestVo.getBizParams(),
                requestVo.getBaseParams().getToken(),
                Integer.parseInt(String.valueOf(params.get("mode"))));
        responseVo.setData(publishRet);
        if (Constants.DRAFT_FULL.equalsIgnoreCase(publishRet)) {
            responseVo.setMessage(ResponseCodeEnum.DRAFT_FULL.getMsg());
            responseVo.setCode(ResponseCodeEnum.DRAFT_FULL.getCode());
        } else {
            Map<String, Object> publishResult = Maps.newHashMap();
            publishResult.put("resourceId", publishRet);
            publishResult.put("detailUrl", contentService.getDetailUrl(ResourceTypeEnum.ARTICLE, publishRet));
            responseVo.setData(publishResult);
        }
        return responseVo;
    }

    /**
     * 对资源的点赞/取消点赞
     *
     * @param requestVo
     * @return
     */
    @PostMapping(value = "/article/delete")
    @LoginRequired
    public ResponseVo deleteArticle(@RequestBody RequestVo requestVo) {
        LOGGER.info("deleteArticle:{}", requestVo);
        Map<String, Object> params = requestVo.getBizParams();
        if (Objects.isNull(params)
                || Objects.isNull(params.get("resourceId"))) {
            throw new BizException(ResponseCodeEnum.PARAM_ERROR);
        }
        ResponseVo responseVo = new ResponseVo(ResponseCodeEnum.SUCCESS);
        contentService.deleteArticle(requestVo.getBizParams(),
                requestVo.getBaseParams().getToken());
        return responseVo;
    }


    /**
     * 检测更新
     *
     * @param data
     * @return
     */
    @GetMapping(value = "/update/check")
    @SignaturePass
    public ResponseVo updateCheck(@RequestParam(value = "data") String data) {
        RequestVo requestVo = JSON.parseObject(Base64Utils.decodeParams(data), RequestVo.class);
        LOGGER.info("updateCheck:{}", requestVo);
        if (MapUtils.isEmpty(requestVo.getBizParams())
                || Objects.isNull(requestVo.getBizParams().get("os"))
                || Objects.isNull(requestVo.getBizParams().get("versionName"))) {
            throw new BizException(ResponseCodeEnum.PARAM_ERROR);
        }
        ResponseVo responseVo = new ResponseVo(ResponseCodeEnum.SUCCESS);
        responseVo.setData(contentService.updateCheck(requestVo.getBizParams()));
        return responseVo;
    }

    /**
     * 举报原因
     *
     * @param data
     * @return
     */
    @GetMapping(value = "/complain/reason")
    @SignaturePass
    public ResponseVo complainReason(@RequestParam(value = "data") String data) {
        RequestVo requestVo = JSON.parseObject(Base64Utils.decodeParams(data), RequestVo.class);
        LOGGER.info("complainReason:{}", requestVo);
        ResponseVo responseVo = new ResponseVo(ResponseCodeEnum.SUCCESS);
        responseVo.setData(contentService.complainReason());
        return responseVo;
    }

    /**
     * 举报
     *
     * @param
     * @return
     */
    @PostMapping(value = "/complain")
    @SignaturePass
    public ResponseVo complain(@RequestBody RequestVo requestVo) {
        LOGGER.info("complain:{}", requestVo);
        if (MapUtils.isEmpty(requestVo.getBizParams())
                || Objects.isNull(requestVo.getBizParams().get("type"))
                || Objects.isNull(requestVo.getBizParams().get("resourceId"))
                || Objects.isNull(requestVo.getBizParams().get("reason"))) {
            throw new BizException(ResponseCodeEnum.PARAM_ERROR);
        }
        ResponseVo responseVo = new ResponseVo(ResponseCodeEnum.SUCCESS);
        contentService.complain(requestVo.getBizParams(), requestVo.getBaseParams().getToken());
        return responseVo;
    }


    /**
     * 不感兴趣
     *
     * @param
     * @return
     */
    @PostMapping(value = "/resource/ban")
    @SignaturePass
    @LoginRequired
    public ResponseVo banResource(@RequestBody RequestVo requestVo) {
        LOGGER.info("banResource:{}", requestVo);
        if (MapUtils.isEmpty(requestVo.getBizParams())
                || Objects.isNull(requestVo.getBizParams().get("type"))
                || Objects.isNull(requestVo.getBizParams().get("resourceId"))) {
            throw new BizException(ResponseCodeEnum.PARAM_ERROR);
        }
        ResponseVo responseVo = new ResponseVo(ResponseCodeEnum.SUCCESS);
        contentService.banResource(requestVo.getBizParams(), requestVo.getBaseParams().getToken());
        return responseVo;
    }


    /**
     * 屏蔽作者
     *
     * @param
     * @return
     */
    @PostMapping(value = "/author/ban")
    @SignaturePass
    @LoginRequired
    public ResponseVo banAuthor(@RequestBody RequestVo requestVo) {
        LOGGER.info("banAuthor:{}", requestVo);
        if (MapUtils.isEmpty(requestVo.getBizParams())
                || Objects.isNull(requestVo.getBizParams().get("authorId"))
                || Objects.isNull(requestVo.getBizParams().get("action"))) {
            throw new BizException(ResponseCodeEnum.PARAM_ERROR);
        }
        ResponseVo responseVo = new ResponseVo(ResponseCodeEnum.SUCCESS);
        contentService.banAuthor(requestVo.getBizParams(), requestVo.getBaseParams().getToken());
        return responseVo;
    }

    /**
     * 拼团列表页
     *
     * @param data
     * @return
     */
    @GetMapping(value = "/assemble/list")
    @SignaturePass
    public ResponseVo assembleList(@RequestParam(value = "data") String data) {
        RequestVo requestVo = JSON.parseObject(Base64Utils.decodeParams(data), RequestVo.class);
        LOGGER.info("assembleList:{}", requestVo);
        if (MapUtils.isEmpty(requestVo.getBizParams())
                || Objects.isNull(requestVo.getBizParams().get("type"))
                || Objects.isNull(requestVo.getBizParams().get("resourceId"))
                || Objects.isNull(requestVo.getBizParams().get("pageIndex"))
                || Objects.isNull(requestVo.getBizParams().get("pageSize"))) {
            throw new BizException(ResponseCodeEnum.PARAM_ERROR);
        }
        ResponseVo responseVo = new ResponseVo(ResponseCodeEnum.SUCCESS);
        responseVo.setData(contentService.assembleList(requestVo.getBizParams()));
        return responseVo;
    }

    /**
     * 拼团详情页
     *
     * @param data
     * @return
     */
    @GetMapping(value = "/assemble/detail")
    @SignaturePass
    public ResponseVo assembleDetail(@RequestParam(value = "data") String data) {
        RequestVo requestVo = JSON.parseObject(Base64Utils.decodeParams(data), RequestVo.class);
        LOGGER.info("assembleDetail:{}", requestVo);
        if (MapUtils.isEmpty(requestVo.getBizParams())
                || Objects.isNull(requestVo.getBizParams().get("assembleGroupId"))) {
            throw new BizException(ResponseCodeEnum.PARAM_ERROR);
        }
        ResponseVo responseVo = new ResponseVo(ResponseCodeEnum.SUCCESS);
        responseVo.setData(contentService.assembleDetail(requestVo.getBizParams()));
        return responseVo;
    }

}
