package com.shou.jwtlogindemo.security;

import com.shou.jwtlogindemo.security.handler.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;//未登陆时返回 JSON 格式的数据给前端（否则为 html）

    @Autowired
    private JwtAccessDeniedHandler jwtAccessDeniedHandler;//无权访问返回的 JSON 格式数据给前端（否则为 403 html 页面）

    @Autowired
    private UserDetailsService jwtUserDetailsService;

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Autowired
    private JwtLoginFilter jwtLoginFilter;

    @Autowired
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        //替换用户信息的封装类，和明文密码加密器。
        auth
                .userDetailsService(jwtUserDetailsService)
                .passwordEncoder(new NonePasswordEncoder());
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        //关闭跨域防护
        httpSecurity
                .cors()
                .and()
                .csrf().disable();

        httpSecurity
                .authorizeRequests()
                .antMatchers("/v2/**").permitAll()
                .antMatchers("/swagger-resources/**").permitAll()
                .antMatchers("/swagger-ui/**").permitAll()
                .antMatchers("/druid/**").permitAll()
                .antMatchers("/webjars/**", "/static/**").permitAll()
                //上面的部分属于静态资源
                .antMatchers("/api/user/register").permitAll()
                .antMatchers("/api/login").permitAll()
                .antMatchers("/api/user/all").hasRole("admin")
                .anyRequest().authenticated();

        //关闭默认的表单登录
        httpSecurity
                .formLogin()
                .disable();

        httpSecurity
                .httpBasic()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint);

        httpSecurity
                .exceptionHandling()
                .accessDeniedHandler(jwtAccessDeniedHandler);
        //启动无状态模式
        httpSecurity
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        //加入jwt请求过滤器在靠前的位置，拦截并处理一般请求
        httpSecurity
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        //加入jwt登录请求过滤器，专门拦截并处理登录请求
        httpSecurity
                .addFilterBefore(jwtLoginFilter, JwtRequestFilter.class);
    }
}
