package com.csye6225HW1.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.csye6225HW1.POJO.User;
import com.csye6225HW1.dao.UsersDao;
import com.csye6225HW1.service.IUsersService;
import com.csye6225HW1.util.UserDemo;
import com.csye6225HW1.util.UserPassingLoginAuth;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

//after extends UsersDao, Users, all method included by IusersService(MybatisPlus provided) will be impl atuomatically.
@Service
//usersDetailService provide user query service
public class UserServiceImplMP extends ServiceImpl<UsersDao, User> implements IUsersService, UserDetailsService {

    @Autowired
    UsersDao usersDao;

    @Autowired
    BCryptPasswordEncoder PwdEncoder;



    public UserDemo initialUser(User user) {

        List<User> users = usersDao.selectByUsername(user.getUsername());

        if(users!= null && users.size()>0){
            throw new RuntimeException("users already existed");
        }
        // save user
        // encode the password
        user.setPassword(PwdEncoder.encode(user.getPassword()));
        //mybatis plus provided insert method when we let UsersDao extend BaseMapper interface
        usersDao.insert(user);


        // Return the user information.
        UserDemo userDemo = new UserDemo();

        BeanUtils.copyProperties(user, userDemo);
        return userDemo;
    }

    public void update(Long id, User user) {
        User userToBeUpdated = usersDao.selectById(id);
        BeanUtils.copyProperties(user,userToBeUpdated,"createdTime","username","id");
        userToBeUpdated.setPassword(PwdEncoder.encode(userToBeUpdated.getPassword()));
        usersDao.updateById(userToBeUpdated);
    }

    public UserDemo getUser(Long id){
        Optional<User> users = Optional.ofNullable(usersDao.selectById(id));
        User user = users.get();
        UserDemo userDemo = new UserDemo();
        BeanUtils.copyProperties(user, userDemo);
        return userDemo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",username);
        //select one = by a filed you assigned to query,such as username
        User user = usersDao.selectOne(queryWrapper);
        //user=null, indicate no user found
        if(user == null) {
            log.error("Access denied: Username" + username + " not found");
            throw new UsernameNotFoundException("Username: " + username + "not found");
        }else{
            //for user UserHolder.getUser();
           UserPassingLoginAuth.saveUser(user);
            return user;
       }


    }}


