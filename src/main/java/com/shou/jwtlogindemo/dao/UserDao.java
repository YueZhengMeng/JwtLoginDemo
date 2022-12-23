package com.shou.jwtlogindemo.dao;

import com.shou.jwtlogindemo.po.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface UserDao {
    @Select("select * from user")
    List<User> selectAllUser();

    @Select("select * from user where userID=#{userID}")
    User selectUserByID(int userID);

    @Select("select * from user where username=#{username}")
    User selectUserByName(String username);

    @Insert("insert into user(username,password) values(#{username},#{password})")
    int addUser(User user);

    @Update("update user set username = #{username},password = #{password},role = #{role} where userID = #{userID}")
    int updateUser(User user);

    @Delete("delete from user where userID = #{userID}")
    int deleteUser(int userID);
}
