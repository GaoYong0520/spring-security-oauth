package com.gaoyong.springsecurityoauth.oauth.crowd;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
        crowdSession();
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
        CrowdAuthenticationDetailsSource crowdWebAuthenticationDetailsSource = new CrowdAuthenticationDetailsSource();
        this.setAuthenticationDetailsSource(crowdWebAuthenticationDetailsSource);
        authenticationToken.setDetails(authenticationDetailsSource.buildDetails(request));
        return this.getAuthenticationManager().authenticate(authenticationToken);
        
        // return authenticationToken;
    }
    
    public void crowdSession() {
        ServletContext servletContext = this.getServletContext();
        WebApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(servletContext);
        log.info("ctx:{}", ctx);
        // httpAuthenticator = (HttpAuthenticator) ctx.getBean("httpAuthenticator");
        // try {
        //
        //     this.authenticated = this.httpAuthenticator.isAuthenticated(request, response);
        // } catch (RemoteException e) {
        //     e.printStackTrace();
        // } catch (InvalidAuthorizationTokenException e) {
        //     e.printStackTrace();
        // } catch (ApplicationAccessDeniedException e) {
        //
        //     e.printStackTrace();
        // }
        //
        // return this.authenticated;
    }
}
