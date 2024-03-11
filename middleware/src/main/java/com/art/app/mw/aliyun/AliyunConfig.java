package com.art.app.mw.aliyun;

import com.aliyun.oss.ClientConfiguration;
import com.aliyun.oss.OSSClient;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 */
@Configuration
@ConfigurationProperties(prefix = "aliyun.config")
@Data
public class AliyunConfig {
    private String endpoint;
    private String accessKeyId;
    private String accessKeySecret;
    private String bucketName;
    private int maxConnections;
    private int connectionTimeout;
    private int maxErrorRetry;
    private int socketTimeout;
    private String template;
    static final String sign = "";
    static final String product = "Dysmsapi";
    static final String action = "SendSms";
    static final String version = "2017-05-25";
    //产品域名,开发者无需替换
    static final String domain = "";
    static final String region = "";

    @Bean
    public ClientConfiguration getClientConfiguration() {
        ClientConfiguration conf = new ClientConfiguration();
        conf.setMaxConnections(maxConnections);
        conf.setConnectionTimeout(connectionTimeout);
        conf.setMaxErrorRetry(maxErrorRetry);
        conf.setSocketTimeout(socketTimeout);
        return conf;
    }

    @Bean
    public OSSClient getOssClient(ClientConfiguration clientConfiguration) {
        return new OSSClient(endpoint, accessKeyId, accessKeySecret, clientConfiguration);
    }

    @Bean
    public IAcsClient getIAcsClient() throws ClientException {
        IClientProfile profile = DefaultProfile.getProfile(region, accessKeyId, accessKeySecret);
        DefaultProfile.addEndpoint(region, region, product, domain);
        return new DefaultAcsClient(profile);
    }
}
