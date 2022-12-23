package com.shou.jwtlogindemo.controller;

import com.shou.jwtlogindemo.po.User;
import com.shou.jwtlogindemo.service.UserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "查看所有User信息",notes = "管理员权限")
    List<User> getAllUser()
    {
        return userService.getAllUser();
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "用户注册",notes = "所有权限")
    int registerUser(@RequestBody User user)
    {
        return userService.registerUser(user);
    }

    @GetMapping("/role")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "获取当前User的所有信息",notes = "主要用于获取role信息\n所有权限")
    User getMyRole()
    {
        return userService.getMyRole();
    }

}
