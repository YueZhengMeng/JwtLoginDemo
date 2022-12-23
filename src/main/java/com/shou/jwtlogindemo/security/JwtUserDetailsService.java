package com.shou.jwtlogindemo.security;

import com.shou.jwtlogindemo.dao.UserDao;
import com.shou.jwtlogindemo.po.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    private UserDao userDao;

    @Override
    public JwtUserDetail loadUserByUsername(String username) throws UsernameNotFoundException {
        User user= userDao.selectUserByName(username);
        if (user!=null) {
            return getUserAuthorities(user);
        } else {
            throw new UsernameNotFoundException("该用户名不存在: " + username);
        }
    }

    public JwtUserDetail loadUserByID(Integer userID) throws UsernameNotFoundException {
        User user= userDao.selectUserByID(userID);
        if (user!=null) {
            return getUserAuthorities(user);
        } else {
            throw new UsernameNotFoundException("该用户ID不存在: " + userID);
        }
    }

    private JwtUserDetail getUserAuthorities(User user) {
        String role = user.getRole();
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
        if (role.equals("teacher")) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + "student"));
        }
        if (role.equals("admin")) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + "teacher"));
            authorities.add(new SimpleGrantedAuthority("ROLE_" + "student"));
        }
        return new JwtUserDetail(user.getUserID(),user.getUsername(), user.getPassword(),authorities);
    }

    public JwtUserDetail getLoginUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            if (authentication instanceof AnonymousAuthenticationToken) {
                return null;
            }
            if (authentication instanceof UsernamePasswordAuthenticationToken) {
                return (JwtUserDetail) (authentication.getPrincipal());
            }
        }
        return null;
    }

    public int getLoginUserId() {
        return getLoginUser().getUserid();
    }

    public String getLoginUserName() {
        return getLoginUser().getUsername();
    }

}