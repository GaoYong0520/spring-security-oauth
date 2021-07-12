package com.gaoyong.springsecurityoauth.oauth.crowd;

import lombok.Getter;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import javax.servlet.http.HttpServletRequest;

/**
 * @author 高勇01
 * @date 2021/7/12 14:43
 */
public class CrowdAuthenticationDetailsSource extends WebAuthenticationDetailsSource {
    @Getter
    private HttpServletRequest request;
    public CrowdAuthenticationDetailsSource() {
        super();
    }
    
    
    
    @Override
    public WebAuthenticationDetails buildDetails(HttpServletRequest context) {
        this.request = context;
        return new CrowdAuthenticationDetails(context);
    }
}
