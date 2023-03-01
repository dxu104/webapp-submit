package com.csye6225HW1.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.csye6225HW1.POJO.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
//@Repository
public interface ProductsDao extends BaseMapper<Product> {
    @Select("select * from tbl_product where sku=#{sku}")
    List<Product> selectBySku(String sku);


}
