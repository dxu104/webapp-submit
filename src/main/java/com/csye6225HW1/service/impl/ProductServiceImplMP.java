package com.csye6225HW1.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.csye6225HW1.POJO.Product;
import com.csye6225HW1.POJO.User;
import com.csye6225HW1.dao.ProductsDao;
import com.csye6225HW1.dao.UsersDao;
import com.csye6225HW1.service.IProductsService;
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
//@Service
//usersDetailService provide user query service
public class ProductServiceImplMP extends ServiceImpl<ProductsDao, Product> implements IProductsService{



//    @Autowired
//    IProductsService iProductsService;
//
//
//
//    @Autowired
//    BCryptPasswordEncoder PwdEncoder;




       }





