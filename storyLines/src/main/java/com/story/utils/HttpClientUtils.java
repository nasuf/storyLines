package com.story.utils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheConfig;

public class HttpClientUtils {
	
	private static final Logger logger = LoggerFactory.getLogger(HttpClientUtils.class);
	
    private static PoolingHttpClientConnectionManager cm;  
    private static String EMPTY_STR = "";  
    private static String UTF_8 = "UTF-8";

    private static final String PRAGRAM = "no-cache";
    private static final String CACHE_CONTROL = "no-cache";
  
    private static void init() {  
        if (cm == null) {  
            cm = new PoolingHttpClientConnectionManager();  
            cm.setMaxTotal(50);// 整个连接池最大连接数  
            cm.setDefaultMaxPerRoute(5);// 每路由最大连接数，默认值是2  
        }  
    }  
  
    /** 
     * 通过连接池获取HttpClient 
     *  
     * @return 
     */  
    private static CloseableHttpClient getHttpClient() {  
        init();  
        return HttpClients.custom().setConnectionManager(cm).build();  
    }  
  
    /** 
     *  
     * @param url 
     * @return 
     * @throws IOException 
     * @throws ParseException 
     */  
    public static HttpResult httpGetRequest(String url) throws ParseException, IOException {  
        HttpGet httpGet = new HttpGet(url);  
        return getResult(httpGet);  
    }  
    
    public static HttpEntity httpGetResponseEntity(String url) throws ParseException, IOException {  
        HttpGet httpGet = new HttpGet(url); 
        httpGet.setHeader(HttpHeaders.PRAGMA, PRAGRAM);
        httpGet.setHeader(HttpHeaders.CACHE_CONTROL, CACHE_CONTROL);
        return getResponseEntity(httpGet);  
    }   
  
    public static HttpResult httpGetRequest(String url, Map<String, Object> params) throws URISyntaxException, ParseException, IOException {  
        URIBuilder ub = new URIBuilder();  
        ub.setPath(url);  
  
        ArrayList<NameValuePair> pairs = covertParams2NVPS(params);  
        ub.setParameters(pairs);  
  
        HttpGet httpGet = new HttpGet(ub.build());  
        return getResult(httpGet);  
    }  
  
    public static HttpResult httpGetRequest(String url, Map<String, Object> headers, Map<String, Object> params)  
            throws URISyntaxException, ParseException, IOException {  
        URIBuilder ub = new URIBuilder();  
        ub.setPath(url);  
  
        ArrayList<NameValuePair> pairs = covertParams2NVPS(params);  
        ub.setParameters(pairs);  
  
        HttpGet httpGet = new HttpGet(ub.build());  
        for (Map.Entry<String, Object> param : headers.entrySet()) {  
            httpGet.addHeader(param.getKey(), String.valueOf(param.getValue()));  
        }  
        return getResult(httpGet);  
    }  
  
    public static HttpResult httpPostRequest(String url) throws ParseException, IOException {  
        HttpPost httpPost = new HttpPost(url);  
        return getResult(httpPost);  
    }  
  
    public static HttpResult httpPostRequest(String url, Map<String, Object> params) throws ParseException, IOException {  
        HttpPost httpPost = new HttpPost(url);  
        ArrayList<NameValuePair> pairs = covertParams2NVPS(params);  
        httpPost.setEntity(new UrlEncodedFormEntity(pairs, UTF_8));  
        return getResult(httpPost);  
    }  
  
    public static HttpResult httpPostRequest(String url, Map<String, Object> headers, Map<String, Object> params)  
            throws ParseException, IOException {  
        HttpPost httpPost = new HttpPost(url);  
  
        for (Map.Entry<String, Object> param : headers.entrySet()) {  
            httpPost.addHeader(param.getKey(), String.valueOf(param.getValue()));  
        }  
  
        ArrayList<NameValuePair> pairs = covertParams2NVPS(params);  
        httpPost.setEntity(new UrlEncodedFormEntity(pairs, UTF_8));  
  
        return getResult(httpPost);  
    }  
  
    private static ArrayList<NameValuePair> covertParams2NVPS(Map<String, Object> params) {  
        ArrayList<NameValuePair> pairs = new ArrayList<NameValuePair>();  
        for (Map.Entry<String, Object> param : params.entrySet()) {  
            pairs.add(new BasicNameValuePair(param.getKey(), String.valueOf(param.getValue())));  
        }  
  
        return pairs;  
    }  
  
    /** 
     * 处理Http请求 
     *  
     * @param request 
     * @return 
     * @throws IOException 
     * @throws ParseException 
     */  
    private static HttpResult getResult(HttpRequestBase request) throws ParseException, IOException {  
        // CloseableHttpClient httpClient = HttpClients.createDefault();  
        CloseableHttpClient httpClient = getHttpClient();  
        HttpEntity entity = null;
        CloseableHttpResponse response = null;
            try {
            	response = httpClient.execute(request);  
				entity = response.getEntity();  
				if (null != entity) {
					return new HttpResult(String.valueOf(response.getStatusLine().getStatusCode()), "success", EntityUtils.toString(
	                        response.getEntity(), UTF_8));
				} else {
					return new HttpResult(String.valueOf(response.getStatusLine().getStatusCode()), "failed", null);
				}
				
			} finally {
				if(null != response) {
					response.close();
				}
			}
    }  
    
    private static HttpEntity getResponseEntity(HttpRequestBase request) throws ParseException, IOException {  
        // CloseableHttpClient httpClient = HttpClients.createDefault();  
        CloseableHttpClient httpClient = getHttpClient();  
        HttpEntity entity = null;
        CloseableHttpResponse response = null;
//            try {
            	response = httpClient.execute(request);  
				return response.getEntity();  
//			} 
//            finally {
//				if(null != response) {
//					response.close();
//				}
//			}
    }  
   
}
