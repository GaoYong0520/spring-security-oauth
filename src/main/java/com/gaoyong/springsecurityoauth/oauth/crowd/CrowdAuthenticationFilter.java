package com.gaoyong.springsecurityoauth.oauth.crowd;

import com.atlassian.crowd.model.user.User;
import com.atlassian.crowd.service.client.CrowdClient;
import com.gaoyong.springsecurityoauth.oauth.weibo.api.vo.WeiboToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

import static com.gaoyong.springsecurityoauth.oauth.weibo.api.WeiboClient.CLIENT;

/**
 * @author 高勇01
 * @date 2021/6/21 15:07
 */
@Slf4j
public class CrowdAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    // @Autowired
    // private CrowdUserDetailsService crowdUserDetailsService;
    
    public CrowdAuthenticationFilter(String filterProcessesUrl) {
        super(new AntPathRequestMatcher(filterProcessesUrl, "GET"));
    }
    
    
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        // AbstractAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        AbstractAuthenticationToken authenticationToken = new AbstractAuthenticationToken(null) {
            @Override
            public Object getCredentials() {
                return password;
            }

            @Override
            public Object getPrincipal() {
                return username;
            }
        };
        authenticationToken.setDetails(authenticationDetailsSource.buildDetails(request));
        return this.getAuthenticationManager().authenticate(authenticationToken);
        // return authenticationToken;
    }
}
