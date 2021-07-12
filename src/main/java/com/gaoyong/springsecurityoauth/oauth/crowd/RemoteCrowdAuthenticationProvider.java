package com.gaoyong.springsecurityoauth.oauth.crowd;

import com.atlassian.crowd.exception.*;
import com.atlassian.crowd.model.user.User;
import com.atlassian.crowd.service.client.CrowdClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import java.util.Collections;
import java.util.Set;

/**
 * @author 高勇01
 * @date 2021/7/9 14:02
 */
@Component
public class RemoteCrowdAuthenticationProvider implements AuthenticationProvider {
    
    @Autowired
    private CrowdUserDetailsService crowdUserDetailsService;
    // @Override
    // protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
    //
    // }
    
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        User user = crowdUserDetailsService.crowdAuthority(authentication.getName(), authentication.getCredentials().toString());
        CrowdUserDetailsService.CrowdUserDetails userDetails = new CrowdUserDetailsService.CrowdUserDetails(user);
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
    
    // @Override
    // protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
    //     return null;
    // }
    
    
    
    @Override
    public boolean supports(Class<?> authentication) {
        return (AbstractAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
