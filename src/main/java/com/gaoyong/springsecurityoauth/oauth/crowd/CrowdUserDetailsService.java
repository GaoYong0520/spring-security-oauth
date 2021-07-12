package com.gaoyong.springsecurityoauth.oauth.crowd;

import com.atlassian.crowd.model.user.User;
import com.atlassian.crowd.service.client.CrowdClient;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;

/**
 * @author 高勇01
 * @date 2021/7/9 14:01
 */
@Component
public class CrowdUserDetailsService {
    @Autowired
    private CrowdClient crowdClient;
    
    public User crowdAuthority(String username, String password) {
        try {
           return crowdClient.authenticateUser(username, password);
        } catch (Exception e) {
            throw new BadCredentialsException("Crowd authority error!");
        }
    }
    
    @Data
    public static class CrowdUserDetails implements UserDetails {
        private String username;
        private String displayName;
        private String email;
        public CrowdUserDetails(String username) {
            this.username = username;
        }
    
        public CrowdUserDetails(User user) {
            String name = user.getName();
            String firstName = user.getFirstName();
            String lastName = user.getLastName();
            String emailAddress = user.getEmailAddress();
            String displayName = user.getDisplayName();
            
            this.username = name;
            this.displayName = displayName;
            this.email = emailAddress;
            
        }
        
        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return Collections.singletonList(new SimpleGrantedAuthority("CROWD_USER"));
        }
    
        @Override
        public String getPassword() {
            return null;
        }
    
        @Override
        public String getUsername() {
            return username;
        }
    
        @Override
        public boolean isAccountNonExpired() {
            return true;
        }
    
        @Override
        public boolean isAccountNonLocked() {
            return true;
        }
    
        @Override
        public boolean isCredentialsNonExpired() {
            return true;
        }
    
        @Override
        public boolean isEnabled() {
            return true;
        }
    
        
    
        @Override
        public boolean equals(Object another) {
            return false;
        }
    
        @Override
        public String toString() {
            return null;
        }
    
        @Override
        public int hashCode() {
            return 0;
        }
        
    }
}
