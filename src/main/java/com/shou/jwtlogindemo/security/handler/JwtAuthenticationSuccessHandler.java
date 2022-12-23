package com.shou.jwtlogindemo.security.handler;

import com.shou.jwtlogindemo.utils.JwtResponseMessage;
import com.shou.jwtlogindemo.utils.ResultEnum;
import com.shou.jwtlogindemo.utils.JwtUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;


@Component
public class JwtAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        httpServletResponse.setContentType("application/json;charset=utf-8");
        httpServletResponse.setStatus(ResultEnum.USER_LOGIN_SUCCESS.getCode());
        String jwtToken = JwtUtil.generateToken(authentication);
        Writer writer= new PrintWriter(new OutputStreamWriter(httpServletResponse.getOutputStream()),true);
        writer.write(JwtResponseMessage.LoginSuccess(ResultEnum.USER_LOGIN_SUCCESS,jwtToken));
        writer.flush();
    }
}
