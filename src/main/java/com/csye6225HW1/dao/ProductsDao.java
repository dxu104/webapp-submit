package com.csye6225HW1.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.csye6225HW1.POJO.Product;
//import com.csye6225HW1.POJO.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
//@Repository
public interface ProductsDao extends BaseMapper<Product> {


}
