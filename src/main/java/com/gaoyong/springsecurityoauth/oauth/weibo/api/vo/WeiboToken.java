package com.gaoyong.springsecurityoauth.oauth.weibo.api.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 高勇01
 * @date 2021/7/6 18:12
 */
@NoArgsConstructor
@Data
public class WeiboToken {
    
    private String access_token;
    private Integer expires_in;
    private String remind_in;
    private String uid;
}
