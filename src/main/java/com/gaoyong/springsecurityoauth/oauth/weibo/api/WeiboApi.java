package com.gaoyong.springsecurityoauth.oauth.weibo.api;


import com.gaoyong.springsecurityoauth.oauth.weibo.api.vo.WeiboToken;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;
import java.util.TreeSet;

public interface WeiboApi {
    @POST("/oauth2/access_token")
    @FormUrlEncoded
    Call<WeiboToken> accessToken(@Field("client_id") String clientId,
                                              @Field("client_secret") String clientSecret,
                                              @Field("grant_type") String grantType,
                                              @Field("code") String code,
                                              @Field("redirect_uri") String redirectUri);
    
    @POST("/oauth2/get_token_info")
    @FormUrlEncoded
    Call<Object> tokenInfo(@Field("access_token") String accessToken);
    
    @GET("/2/users/show.json")
    Call<Object> userShow(@Query("access_token") String accessToken,
                                           @Query("uid") Long uid,
                                           @Query("screen_name") String screenName);


}
