package com.shou.jwtlogindemo.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class JwtUserDetail implements UserDetails {

    private int userid;
    private String username;
    private String password;

    private Collection<? extends GrantedAuthority> authorities;

    public JwtUserDetail() {
    }

    public JwtUserDetail(int userid, String username, String password, Collection<? extends GrantedAuthority> authorities) {
        this.userid = userid;
        this.username = username;
        this.password = password;
        this.authorities = authorities;
    }

    @Override
    public boolean equals(Object obj) {

        if (obj instanceof JwtUserDetail){
            //字符串的equals方法是已经重写过的
            return ((JwtUserDetail) obj).getUsername().equals(this.username);
        }else {
            return false;
        }
    }


    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
        this.authorities = authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
