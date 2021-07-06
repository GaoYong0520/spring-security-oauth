package com.gaoyong.springsecurityoauth.oauth.weibo.api;


// import com.google.gson.Gson;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

@Slf4j
public enum WeiboClient {
    /**
     * HTTP Client Instance
     */
    CLIENT;

    public WeiboApi weiboApi;
    public Retrofit retrofit;
    
    public final String clientId = "2284851312";
    public final String clientSecret = "a38b70212c2bd10da40ab70ab97c40e7";

    public String code;
    public String token;
    public static final HashMap<String, Set<String>> cookies = new HashMap<>();
    // private static final Logger logger=Logger.getAnonymousLogger();

    WeiboClient(){

    }
    public void initClient(String url, String code) {
        // okhttp ignore ssl verify
        X509TrustManager manager = new X509TrustManager() {
            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];//这里返回null会报错
            }
            @Override
            public void checkClientTrusted(X509Certificate[] certs, String authType) {
            }
            @Override
            public void checkServerTrusted(X509Certificate[] certs, String authType) {
            }
        };
        TrustManager[] trustAllCerts = new TrustManager[] {manager };
        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());

            int timeout = 10;
            OkHttpClient client = new OkHttpClient.Builder()
                    .sslSocketFactory(sc.getSocketFactory(), manager)
                    .addInterceptor(new AddCookiesInterceptor())
                    .addInterceptor(new ReceivedCookiesInterceptor())
                    .hostnameVerifier((hostname, session) -> true)
                    .connectTimeout(timeout, TimeUnit.SECONDS)
                    .callTimeout(timeout,TimeUnit.SECONDS)
                    .readTimeout(timeout, TimeUnit.SECONDS)
                    .writeTimeout(timeout, TimeUnit.SECONDS)
                    .build();
            ObjectMapper om = new ObjectMapper();
            om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            om.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
            om.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            retrofit = new Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(JacksonConverterFactory.create(om))
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .client(client)
                    .build();
            this.code = code;
            weiboApi = retrofit.create(WeiboApi.class);
        } catch (KeyManagementException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

    }
    
    public <T> T processCall(Call<T> call) {
        Response<T> response;
        try {
            response = call.execute();
        } catch (IOException e) {
            throw new RuntimeException("codesafe-call-error:" + call.request().url() + "\n" + e.getMessage());
        }
        if (response.isSuccessful()) {
            T body = response.body();
            if (body == null) {
                throw new RuntimeException("Response is empty!\n" + call.request().url());
            }
            // if (!"000".equals(body.getCode())) {
            //     throw new RuntimeException("codesafe api return error:\n" + body.getMsg() + call.request().url());
            // }
            log.info("body:{}", body);
            return body;
        }
        throw new RuntimeException();
        // return handleError(response, call);
    }

    public <T> T downloadFile(Call<T> call) {
        Response<T> response;
        try {
            response = call.execute();
        } catch (IOException e) {
            throw new RuntimeException("codesafe-call-error:" + call.request().url() + "\n" + e.getMessage());
        }
        if (response.isSuccessful()) {
            T result = response.body();
            if (result == null) {
                throw new RuntimeException("Response is empty!\n" + call.request().url());
            } else {
                return result;
            }
        }
        return handleError(response, call);
    }

    private <T> T handleError(Response<T> response, Call<T> call) {
        try {
            ResponseBody errorBody = response.errorBody();
            if (errorBody == null) {
                throw new RuntimeException("服务请求无响应或响应内容为空");
            } else {
                String error = errorBody.string();
                if (!error.equals("")) {
                    ObjectMapper objectMapper = new ObjectMapper();
                    try {
                        ApiResponse data = objectMapper.readValue(error, ApiResponse.class);
                        String retInfo = data.getMsg();
                        if (retInfo.equals("认证已过期") || retInfo.equals("未授权的访问")) {
                            throw new RuntimeException();
                        }
                        throw new RuntimeException(retInfo);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(error);
                    }
                } else {
                    throw new RuntimeException("服务请求无响应或响应内容为空");
                }
            }
        } catch (IOException e) {
            Request request = call.request();
            throw new RuntimeException("codesafe-call:" + request.url() + "\n" + e.getMessage());
        }
    }

    public static class AddCookiesInterceptor implements Interceptor {

        @Override
        public okhttp3.Response intercept(Chain chain) throws IOException {
            Request.Builder builder = chain.request().newBuilder();
            if (!cookies.isEmpty()) {
                cookies.forEach((k,v) -> builder.addHeader(k, String.join(";", v)));
            }
            return chain.proceed(builder.build());
        }
    }

    public static class ReceivedCookiesInterceptor implements Interceptor {

        public ReceivedCookiesInterceptor() {
            super();
        }

        @Override
        public okhttp3.Response intercept(Chain chain) throws IOException {
            Set<String> cookie = cookies.computeIfAbsent("Cookie", k -> new HashSet<>());

            okhttp3.Response originalResponse = chain.proceed(chain.request());
            //这里获取请求返回的cookie
            List<String> headers = originalResponse.headers("Set-Cookie");
            if (!headers.isEmpty()) {
                cookie.addAll(headers);
            }
            return originalResponse;
        }
    }
}
