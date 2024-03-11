package com.art.app.common.util;


import com.alibaba.fastjson.JSON;
import org.apache.commons.io.Charsets;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.util.Map;

public class HttpUtil {

    private HttpUtil() {
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpUtil.class);

    /**
     * 连接超时时间ms
     */
    private static final int CONNECT_TIMEOUT = 30000;
    /**
     * 获取数据的超时时间ms
     */
    private static final int SOCKET_TIMEOUT = 30000;
    /**
     * 从连接池中获取可用连接的时间ms
     */
    private static final int CONNECTION_REQUEST_TIMEOUT = 30000;
    /**
     * 连接池的最大连接数
     */
    private static final int POOL_MAX_TOTAL = 200;
    /**
     * 每一个路由的最大连接数
     */
    private static final int POOL_MAX_PRE_ROUTE = 20;
    /**
     * 可用空闲连接过期时间ms,重用空闲连接时会先检查是否空闲时间超过这个时间，如果超过，释放socket重新建立
     */
    private static final int POOL_CONNECTION_CHECK_TIMEOUT = 30000;
    /**
     * 请求超时配置
     */
    private static final RequestConfig requestConfig = RequestConfig.custom()
            .setConnectTimeout(CONNECT_TIMEOUT)
            .setSocketTimeout(SOCKET_TIMEOUT)
            .setConnectionRequestTimeout(CONNECTION_REQUEST_TIMEOUT)
            .build();
    /**
     * 配置连接池
     */
    private static final PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();

    static {
        connectionManager.setMaxTotal(POOL_MAX_TOTAL);
        connectionManager.setDefaultMaxPerRoute(POOL_MAX_PRE_ROUTE);
        connectionManager.setValidateAfterInactivity(POOL_CONNECTION_CHECK_TIMEOUT);
    }

    /**
     * POST请求
     *
     * @param url
     * @param params
     * @return
     */
    public static String doPost(String url, Map<String, Object> params) {
        CloseableHttpClient client = HttpClients.custom()
                .setDefaultRequestConfig(requestConfig)
                .setConnectionManager(connectionManager)
                .build();
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Content-Type", ContentType.APPLICATION_JSON.getMimeType());
        try {
            httpPost.setEntity(new StringEntity(JSON.toJSONString(params)));
        } catch (UnsupportedEncodingException e) {
            LOGGER.error("doPost build params for url:{}, error:{}", url, params);
            throw new RuntimeException();
        }
        try (CloseableHttpResponse response = client.execute(httpPost)) {
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                return EntityUtils.toString(response.getEntity(), Charsets.UTF_8);
            }
            throw new RuntimeException();
        } catch (Exception e) {
            LOGGER.error("doPost error for url:{}, params:{}", url, params, e);
            throw new RuntimeException();
        }
    }

    public static String doPostXml(String url, String params) {
        CloseableHttpClient client = HttpClients.custom()
                .setDefaultRequestConfig(requestConfig)
                .setConnectionManager(connectionManager)
                .build();
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Content-Type", ContentType.APPLICATION_XML.getMimeType());
        try {
            httpPost.setEntity(new StringEntity(params));
        } catch (UnsupportedEncodingException e) {
            LOGGER.error("doPost build params for url:{}, error:{}", url, params);
            throw new RuntimeException();
        }
        try (CloseableHttpResponse response = client.execute(httpPost)) {
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                return EntityUtils.toString(response.getEntity(), Charsets.UTF_8);
            }
            throw new RuntimeException();
        } catch (Exception e) {
            LOGGER.error("doPost error for url:{}, params:{}", url, params, e);
            throw new RuntimeException();
        }
    }

    /**
     * GET请求
     *
     * @param url
     * @return
     */
    public static String doGet(String url) {
        CloseableHttpClient client = HttpClients.custom()
                .setDefaultRequestConfig(requestConfig)
                .setConnectionManager(connectionManager)
                .build();
        HttpGet httpGet = new HttpGet(url);
        try (CloseableHttpResponse response = client.execute(httpGet)) {
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                return EntityUtils.toString(response.getEntity(), Charsets.UTF_8);
            }
            throw new RuntimeException();
        } catch (Exception e) {
            LOGGER.error("doPost error", e);
            throw new RuntimeException();
        }
    }
}

