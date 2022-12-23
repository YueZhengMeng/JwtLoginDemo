package com.shou.jwtlogindemo.service;

import com.shou.jwtlogindemo.dao.UserDao;
import com.shou.jwtlogindemo.po.User;
import com.shou.jwtlogindemo.security.JwtUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    JwtUserDetailsService jwtUserDetailsService;

    public List<User> getAllUser() {
        return userDao.selectAllUser();
    }

    public User getUserById(int userID) {
        return userDao.selectUserByID(userID);
    }

    public User getUserByName(String username) {
        return userDao.selectUserByName(username);
    }

    public int registerUser(User user) {
        return userDao.addUser(user);
    }

    public User getMyRole() {
        int userID = jwtUserDetailsService.getLoginUserId();
        return userDao.selectUserByID(userID);
    }
}
