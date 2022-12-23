package com.shou.jwtlogindemo.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shou.jwtlogindemo.po.User;
import com.shou.jwtlogindemo.security.handler.JwtAuthenticationFailureHandler;
import com.shou.jwtlogindemo.security.handler.JwtAuthenticationSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtLoginFilter extends OncePerRequestFilter {
    @Autowired
    JwtUserDetailsService jwtUserDetailsService;

    @Autowired
    JwtAuthenticationSuccessHandler jwtAuthenticationSuccessHandler;

    @Autowired
    JwtAuthenticationFailureHandler jwtAuthenticationFailureHandler;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //如果请求登录接口
        if ("/api/login".equals(request.getRequestURI())) {
            //如果请求方法是post
            if (request.getMethod().equals("post") || request.getMethod().equals("POST")) {
                //从请求中读取用户名和密码
                User user = new ObjectMapper().readValue(request.getInputStream(), User.class);
                //从数据库中查询
                JwtUserDetail jwtUserDetail = jwtUserDetailsService.loadUserByUsername(user.getUsername());
                if (jwtUserDetail != null) {
                    if (user.getPassword().equals(jwtUserDetail.getPassword())) {
                        //用户名与密码正确，则生成认证信息
                        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                                jwtUserDetail, null, jwtUserDetail.getAuthorities());
                        usernamePasswordAuthenticationToken
                                .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        //将认证信息加入上下文
                        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                        jwtAuthenticationSuccessHandler.onAuthenticationSuccess(request, response, usernamePasswordAuthenticationToken);
                    }
                    else {
                        jwtAuthenticationFailureHandler.onAuthenticationFailure(request, response, new BadCredentialsException("password error"));
                    }
                } else {
                    jwtAuthenticationFailureHandler.onAuthenticationFailure(request, response, new UsernameNotFoundException("user not found"));
                }
            }
            else {
                jwtAuthenticationFailureHandler.onAuthenticationFailure(request, response, new AuthenticationServiceException("http method error"));
            }
            //处理完登录请求可以直接结束过滤器链条，不必继续进行
            return;
        }
        else {
            filterChain.doFilter(request, response);
        }
    }
}
