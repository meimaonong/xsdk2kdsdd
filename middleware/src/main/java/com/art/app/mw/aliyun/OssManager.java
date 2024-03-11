package com.art.app.mw.aliyun;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.GetObjectRequest;
import com.aliyun.oss.model.MatchMode;
import com.aliyun.oss.model.PolicyConditions;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Map;

@Component
public class OssManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(OssManager.class);
    @Autowired
    private AliyunConfig aliyunConfig;
    @Autowired
    private OSSClient ossClient;

    public void uploadStringToOSS(String bucketName, String content, String objectName) {
        ossClient.putObject(bucketName, objectName, new ByteArrayInputStream(content.getBytes()));
    }

    public void uploadFileByBytes(String bucketName, String objectName, byte[] fileContent) {
        ossClient.putObject(bucketName, objectName, new ByteArrayInputStream(fileContent));
    }

    public void uploadLocalFile(String bucketName, String localFilePath, String objectName) {
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(localFilePath);
        } catch (Exception e) {
            LOGGER.error("uploadLocalFile error", e);
        }
        ossClient.putObject(bucketName, objectName, inputStream);
    }

    public void downLoadFile(String bucketName, String objectName, String filePath) {
        ossClient.getObject(new GetObjectRequest(bucketName, objectName), new File(filePath));
    }

    public Map<String, String> getSign(String dir) throws UnsupportedEncodingException {
        long expireTime = 600;
        long expireEndTime = System.currentTimeMillis() + expireTime * 1000;
        Date expiration = new Date(expireEndTime);
        PolicyConditions policyConds = new PolicyConditions();
//            policyConds.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, 1048576000);
        policyConds.addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY, dir);

        String postPolicy = ossClient.generatePostPolicy(expiration, policyConds);
        byte[] binaryData = postPolicy.getBytes("utf-8");
        String encodedPolicy = BinaryUtil.toBase64String(binaryData);
        String postSignature = ossClient.calculatePostSignature(postPolicy);
        Map<String, String> respMap = Maps.newLinkedHashMap();
        respMap.put("accessid", aliyunConfig.getAccessKeyId());
        respMap.put("policy", encodedPolicy);
        respMap.put("signature", postSignature);
        respMap.put("dir", dir);
        respMap.put("host", getOssHost());
        respMap.put("expire", String.valueOf(expireEndTime / 1000));
        return respMap;
    }

    public String getOssHost() {
        // host的格式为 bucketname.endpoint
        return "https://" + aliyunConfig.getBucketName() + "." + aliyunConfig.getEndpoint();
    }

}
