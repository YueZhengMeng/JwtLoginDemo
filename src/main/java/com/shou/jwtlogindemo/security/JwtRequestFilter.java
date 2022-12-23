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
        String username = null;
        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            String jwtToken = requestTokenHeader.substring("Bearer ".length());
            try {
                username = JwtUtil.getUserNameFromToken(jwtToken);
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
            //否则会严重影响静态资源
            logger.warn("JWT Token does not begin with Bearer String");
        }*/

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails userDetails = this.jwtUserDetailsService.loadUserByUsername(username);
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());
            usernamePasswordAuthenticationToken
                    .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        }
        chain.doFilter(request, response);
    }

}
