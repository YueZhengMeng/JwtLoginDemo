package com.shou.jwtlogindemo.security;

import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.InvalidClaimException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.shou.jwtlogindemo.security.handler.JwtAuthenticationEntryPoint;
import com.shou.jwtlogindemo.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUserDetailsService jwtUserDetailsService;

    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        final String requestTokenHeader = request.getHeader("Authorization");
        Integer userID = null;
        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            //检查请求格式，和获取token
            String jwtToken = requestTokenHeader.substring("Bearer ".length());
            try {
                //获取userID载荷
                userID = JwtUtil.getUserIdFromToken(jwtToken);
            } catch (SignatureVerificationException e) {
                SecurityContextHolder.clearContext();
                jwtAuthenticationEntryPoint.commence(request, response, new BadCredentialsException("签名错误"));
                return; //结束过滤器链，以下作用相同
            } catch (AlgorithmMismatchException e) {
                SecurityContextHolder.clearContext();
                jwtAuthenticationEntryPoint.commence(request, response, new BadCredentialsException("算法不匹配"));
                return;
            } catch (TokenExpiredException e) {
                SecurityContextHolder.clearContext();
                jwtAuthenticationEntryPoint.commence(request, response, new BadCredentialsException("Token过期"));
                return;
            } catch (InvalidClaimException e) {
                SecurityContextHolder.clearContext();
                jwtAuthenticationEntryPoint.commence(request, response, new BadCredentialsException("载荷错误"));
                return;
            }
        }
        /*else {
            //这种情况不要终结过滤器链
            //否则会导致静态资源无法访问
            //对于不携带token的请求，该过滤器直接放行，无视即可
            logger.warn("JWT Token does not begin with Bearer String");
        }*/

        if (userID != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            //如果token正常，则生成认证信息，加入上下文
            UserDetails userDetails = this.jwtUserDetailsService.loadUserByID(userID);
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());
            usernamePasswordAuthenticationToken
                    .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        }
        chain.doFilter(request, response);
    }

}
