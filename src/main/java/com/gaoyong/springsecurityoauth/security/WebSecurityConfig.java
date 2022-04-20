package com.gaoyong.springsecurityoauth.security;

import com.gaoyong.springsecurityoauth.oauth.crowd.CrowdAuthenticationFilter;
import com.gaoyong.springsecurityoauth.oauth.crowd.CrowdAuthenticationFilter;
import com.gaoyong.springsecurityoauth.oauth.crowd.CrowdUserDetailsService;
import com.gaoyong.springsecurityoauth.oauth.crowd.RemoteCrowdAuthenticationProvider;
import com.gaoyong.springsecurityoauth.oauth.weibo.CrowdAuthenticationSuccessHandler;
import com.gaoyong.springsecurityoauth.oauth.weibo.WeiboAuthenticationFilter;
import com.gaoyong.springsecurityoauth.oauth.weibo.WeiboAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Collections;
import java.util.List;

/**
 * @author 高勇01
 * @date 2021/7/6 12:22
 */
@Configuration
@EnableWebSecurity //启用Spring Security.
////会拦截注解了@PreAuthrize注解的配置.
@EnableGlobalMethodSecurity(prePostEnabled=true)
// @ImportResource(value = "classpath:applicationContext-CrowdRestClient.xml")
// @ComponentScan("")
public class WebSecurityConfig extends WebSecurityConfigurerAdapter  {
    @Value("${weibo.oauth2:/oauth2/weibo}")
    private String oauthUrl;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    

    
    @Autowired
    private WeiboAuthenticationProvider weiboAuthenticationProvider;
    // @Autowired
    // private RemoteCrowdAuthenticationProvider crowdAuthenticationProvider;
    
    @Autowired
    private AuthenticationSuccessHandler weiboAuthenticationSuccessHandler;
    
    @Autowired
    private AuthenticationFailureHandler weiboAuthenticationFailureHandler;
    
    // @Autowired
    // private CrowdAuthenticationSuccessHandler crowdAuthenticationSuccessHandler;
    
    // @Autowired
    // private CrowdUserDetailsService crowdUserDetailsServiceImpl;
    
    // @Autowired
    // private AuthenticationManagerBuilder authenticationManagerBuilder;
    
    
    // @Autowired
    // private AuthenticationManagerBuilder authenticationManagerBuilder;
    
    // @Autowired
    // private WeiboAuthenticationFilter weiboAuthenticationFilter;
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .formLogin()
                .loginPage("/login")
                .loginProcessingUrl(oauthUrl)
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
        //  http.addFilterAfter(weiboAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
        //          .authorizeRequests().antMatchers(oauthUrl).permitAll();
                 // .and().authenticationProvider(new WeiboAuthenticationProvider());
        // http.addFilterAfter(crowdAuthenticationFilter(), WeiboAuthenticationFilter.class)
        //         .authorizeRequests().antMatchers("/oauth2/crowd").permitAll()
        //         .and()
        //         .authorizeRequests().anyRequest().authenticated();
    }
    
    
    @Bean
    public WeiboAuthenticationFilter weiboAuthenticationFilter() {
        // 自定义认证filter，需要实现CustomAuthenticationProcessingFilter和CustomerAuthenticationProvider
        // filter将过滤url并把认证信息塞入authentication作为CustomerAuthenticationProvider.authenticate的入参
        WeiboAuthenticationFilter filter = new WeiboAuthenticationFilter(oauthUrl);
        
        // 默认自定义认证方式grant_type为authorization_code方式，如果直接返回内容，则需自定义success和fail handler
        // filter.setAuthenticationSuccessHandler(new CustomAuthenticationSuccessHandler());
        // ProviderManager providerManager = new ProviderManager(Collections.singletonList(weiboAuthenticationProvider));
        filter.setAuthenticationFailureHandler(weiboAuthenticationFailureHandler);
        filter.setAuthenticationSuccessHandler(weiboAuthenticationSuccessHandler);
        filter.setAuthenticationManager(authenticationManager);
        return filter;
    }
    
    // // @Bean
    // public CrowdAuthenticationFilter crowdAuthenticationFilter() {
    //     // 自定义认证filter，需要实现CustomAuthenticationProcessingFilter和CustomerAuthenticationProvider
    //     // filter将过滤url并把认证信息塞入authentication作为CustomerAuthenticationProvider.authenticate的入参
    //     CrowdAuthenticationFilter filter = new CrowdAuthenticationFilter("/oauth2/crowd");
    //
    //     // 默认自定义认证方式grant_type为authorization_code方式，如果直接返回内容，则需自定义success和fail handler
    //     // filter.setAuthenticationSuccessHandler(new CustomAuthenticationSuccessHandler());
    //     // ProviderManager providerManager = new ProviderManager(Collections.singletonList(weiboAuthenticationProvider));
    //     filter.setAuthenticationFailureHandler(weiboAuthenticationFailureHandler);
    //     filter.setAuthenticationSuccessHandler(crowdAuthenticationSuccessHandler);
    //     List<AuthenticationProvider> providers = Collections.singletonList(crowdAuthenticationProvider);
    //     ProviderManager mapper = new ProviderManager(providers);
    //     filter.setAuthenticationManager(mapper);
    //     return filter;
    // }
    //
    // @Override
    // public void configure(AuthenticationManagerBuilder auth) {
    //     // 定义认证的provider用于实现用户名和密码认证
    //     // auth.authenticationProvider(new UsernamePasswordAuthenticationProvider(usernamePasswordUserDetailService));
    //     // 自定义provider用于实现自定义的登录认证, 如不需要其它形式认证如短信登录，可删除
    //     auth.authenticationProvider(weiboAuthenticationProvider);
    //     auth.authenticationProvider(crowdAuthenticationProvider);
    //     // super.configure(auth);
    //     // auth.authenticationProvider()
    // }
    
    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
    
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication().passwordEncoder(new BCryptPasswordEncoder())
                .withUser("simm").password ("123").roles("USER").and()
                .withUser("admin").password(passwordEncoder.encode("ct123!@#")).roles("USER","ADMIN");

        auth.authenticationProvider(weiboAuthenticationProvider);
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
