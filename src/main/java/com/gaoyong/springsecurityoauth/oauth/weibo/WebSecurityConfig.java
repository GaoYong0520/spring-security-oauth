package com.gaoyong.springsecurityoauth.oauth.weibo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @author 高勇01
 * @date 2021/7/6 12:22
 */
@Configuration
@EnableWebSecurity //启用Spring Security.
////会拦截注解了@PreAuthrize注解的配置.
@EnableGlobalMethodSecurity(prePostEnabled=true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter  {
    @Value("${weibo.oauth2:/oauth2/weibo}")
    private String oauthUrl;
    
    @Autowired
    private AuthenticationManager authenticationManager;
    
    // @Autowired
    // private WeiboAuthenticationFilter weiboAuthenticationFilter;
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .formLogin().loginProcessingUrl("/login")
                .and()
                .authorizeRequests()
                .antMatchers( "/login", "/resources/**", "/static/**").permitAll()
                // .anyRequest() // 任何请求
                // .authenticated()// 都需要身份认证
                .and()
                // .logout().invalidateHttpSession(true).deleteCookies("JSESSIONID").logoutSuccessHandler(customLogoutSuccessHandler).permitAll()
                // .and()
                .csrf().disable();
    
        // 添加自定义Filter
         http.addFilterAfter(weiboAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                 .authorizeRequests().antMatchers("/oauth2/weibo").permitAll().anyRequest().authenticated();
    }
    
    
    @Bean
    public WeiboAuthenticationFilter weiboAuthenticationFilter() {
        // 自定义认证filter，需要实现CustomAuthenticationProcessingFilter和CustomerAuthenticationProvider
        // filter将过滤url并把认证信息塞入authentication作为CustomerAuthenticationProvider.authenticate的入参
        WeiboAuthenticationFilter filter = new WeiboAuthenticationFilter(oauthUrl);
        
        // 默认自定义认证方式grant_type为authorization_code方式，如果直接返回内容，则需自定义success和fail handler
        // filter.setAuthenticationSuccessHandler(new CustomAuthenticationSuccessHandler());
        filter.setAuthenticationFailureHandler(new WeiboAuthenticationFailureHandler());
        
        filter.setAuthenticationManager(authenticationManager);
        return filter;
    }
    
    @Override
    public void configure(AuthenticationManagerBuilder auth) {
        // 定义认证的provider用于实现用户名和密码认证
        // auth.authenticationProvider(new UsernamePasswordAuthenticationProvider(usernamePasswordUserDetailService));
        // 自定义provider用于实现自定义的登录认证, 如不需要其它形式认证如短信登录，可删除
        auth.authenticationProvider(new WeiboAuthenticationProvider());
    }
    
    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
