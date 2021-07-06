package com.gaoyong.springsecurityoauth.oauth.weibo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

/**
 * @author 高勇01
 * @date 2021/7/6 12:40
 */
@Slf4j
public class WeiboAuthenticationProvider implements AuthenticationProvider {
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        log.info(authentication.toString());
        return authentication;
    }
    
    @Override
    public boolean supports(Class<?> aClass) {
        return (WeiboAuthenticationProvider.class.isAssignableFrom(aClass));
    }
}
