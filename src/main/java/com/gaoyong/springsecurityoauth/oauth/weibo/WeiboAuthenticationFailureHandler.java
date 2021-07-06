package com.gaoyong.springsecurityoauth.oauth.weibo;


import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class WeiboAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        // response.sendRedirect("https://api.weibo.com/oauth2/authorize?client_id=2284851312&redirect_uri=http://localhost/oauth2/weibo");
        response.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);
        // response.setCharacterEncoding("UTF-8");
        // response.setContentType("application/json; charset=utf-8");
        // response.setContentType("text/html;charset=UTF-8");
        // 要重定向的新位置
        String site = "https://api.weibo.com/oauth2/authorize?client_id=2284851312&redirect_uri=http://localhost/oauth2/weibo:";
        response.setHeader("Location", site);
    }
}
