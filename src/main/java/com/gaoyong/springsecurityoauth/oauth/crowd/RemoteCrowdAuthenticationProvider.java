package com.gaoyong.springsecurityoauth.oauth.crowd;

import com.atlassian.crowd.exception.*;
import com.atlassian.crowd.integration.http.CrowdHttpAuthenticator;
import com.atlassian.crowd.model.user.User;
import com.atlassian.crowd.service.client.CrowdClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Set;

/**
 * @author 高勇01
 * @date 2021/7/9 14:02
 */
@Component
public class RemoteCrowdAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    private CrowdHttpAuthenticator crowdHttpAuthenticator;
    
    @Autowired
    private CrowdUserDetailsService crowdUserDetailsService;
    // @Override
    // protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
    //
    // }
    
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String principal = (String) authentication.getPrincipal();
        String credentials = (String) authentication.getCredentials();
        CrowdAuthenticationDetails details = (CrowdAuthenticationDetails) authentication.getDetails();
        User user;
        UserDetails userDetails;
        if (principal != null && credentials != null) {
            user = crowdUserDetailsService.crowdAuthority(principal, credentials);
            userDetails = new CrowdUserDetailsService.CrowdUserDetails(user);
        } else {
            userDetails = retrieveUser(details.getRequest());
        }
        Set<SimpleGrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority("CROWD_USER"));
        AbstractAuthenticationToken result = new AbstractAuthenticationToken(authorities) {
    
            @Override
            public Object getCredentials() {
                return authentication.getCredentials();
            }
    
            @Override
            public Object getPrincipal() {
                return authentication.getPrincipal();
            }
        };
        result.setDetails(userDetails);
        result.setAuthenticated(true);
        return result;
    }
    
    
    protected UserDetails retrieveUser(HttpServletRequest request) throws AuthenticationException {
        try {
            String crowdToken = crowdHttpAuthenticator.getToken(request);
            User user = crowdHttpAuthenticator.getUser(request);
            if (user != null) {
                CrowdUserDetailsService.CrowdUserDetails userDetails = new CrowdUserDetailsService.CrowdUserDetails(user);
                userDetails.setCrowdToken(crowdToken);
                return userDetails;
            } else {
                throw new UsernameNotFoundException("");
            }
        } catch (Exception e) {
            throw new AuthenticationServiceException(e.getMessage());
        }
    }
    
    
    
    @Override
    public boolean supports(Class<?> authentication) {
        return (AbstractAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
