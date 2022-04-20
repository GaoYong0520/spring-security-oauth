package com.gaoyong.springsecurityoauth.oauth.weibo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author 高勇01
 * @date 2021/7/6 12:40
 */
@Slf4j
@Component
public class WeiboAuthenticationProvider implements AuthenticationProvider {
    
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("WeiboUser"));
        UsernamePasswordAuthenticationToken result = new UsernamePasswordAuthenticationToken(authentication.getPrincipal(),
                authentication.getCredentials(), authorities);
        result.setDetails(authentication.getDetails());
        return result;
    }
    
    @Override
    public boolean supports(Class<?> authentication) {
        // return true;
        return (AbstractAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
