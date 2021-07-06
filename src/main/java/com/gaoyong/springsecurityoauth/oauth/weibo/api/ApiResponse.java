package com.gaoyong.springsecurityoauth.oauth.weibo.api;

import lombok.Data;

/**
 * @author 高勇01
 * @date 2021/7/6 17:46
 */
@Data
public class ApiResponse<T> {
    private String code;
    private String msg;
    private T data;
    
}
