package com.csye6225HW1.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.csye6225HW1.POJO.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UsersDao extends BaseMapper<User> {

    @Select("select * from tbl_user where username=#{username}")
    List<User> selectByUsername(String username);

//    @Select("select * from tbl_user where id=#{id}")
//    List<User> selectByID(Long id);

    @Select("select * from tbl_user where username=#{username}")
    User getUserByUsername(String username);


//
//    User getUserByUsername(String username);
}
