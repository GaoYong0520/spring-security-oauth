package com.gaoyong.springsecurityoauth.oauth.weibo;

import com.gaoyong.springsecurityoauth.oauth.weibo.api.vo.WeiboToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import static com.gaoyong.springsecurityoauth.oauth.weibo.api.WeiboClient.CLIENT;

/**
 * @author 高勇01
 * @date 2021/7/6 12:40
 */
@Slf4j
public class WeiboAuthenticationProvider implements AuthenticationProvider {
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        authentication.setAuthenticated(true);
        return authentication;
    }
    
    @Override
    public boolean supports(Class<?> aClass) {
        return (WeiboAuthenticationProvider.class.isAssignableFrom(aClass));
    }
}
