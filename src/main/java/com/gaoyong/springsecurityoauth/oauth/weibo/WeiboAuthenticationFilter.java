package com.gaoyong.springsecurityoauth.oauth.weibo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

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
        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken("saas", "ct123!@#");
        authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
        return this.getAuthenticationManager().authenticate(authRequest);
    }
}
