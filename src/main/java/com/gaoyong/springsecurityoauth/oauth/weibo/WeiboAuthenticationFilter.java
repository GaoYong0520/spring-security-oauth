package com.gaoyong.springsecurityoauth.oauth.weibo;

import com.gaoyong.springsecurityoauth.oauth.weibo.api.ApiResponse;
import com.gaoyong.springsecurityoauth.oauth.weibo.api.vo.WeiboToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

import static com.gaoyong.springsecurityoauth.oauth.weibo.api.WeiboClient.CLIENT;

/**
 * @author 高勇01
 * @date 2021/6/21 15:07
 */
@Slf4j
// @Component
public class WeiboAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    public WeiboAuthenticationFilter(String filterProcessesUrl) {
        super(new AntPathRequestMatcher(filterProcessesUrl, "GET"));
    }
    
    
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String code = request.getParameter("code");
        log.info(code);
        
        
        CLIENT.initClient("https://api.weibo.com", code);
        WeiboToken token = CLIENT.processCall(CLIENT.weiboApi.accessToken(CLIENT.clientId, CLIENT.clientSecret, "authorization_code", code, "http://gaoyong.info:8555/oauth2/weibo"));
        CLIENT.token = token.getAccess_token();
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("WeiboUser"));
    
        Object o = CLIENT.processCall(CLIENT.weiboApi.userShow(token.getAccess_token(), Long.valueOf(token.getUid()), null));
        AbstractAuthenticationToken authenticationToken = new AbstractAuthenticationToken(authorities) {
            @Override
            public Object getCredentials() {
                return code;
            }
        
            @Override
            public Object getPrincipal() {
                return o;
            }
        };
        authenticationToken.setDetails(authenticationDetailsSource.buildDetails(request));
        return this.getAuthenticationManager().authenticate(authenticationToken);
    }
    
    
}
