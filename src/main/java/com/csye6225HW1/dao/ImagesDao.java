package com.csye6225HW1.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.csye6225HW1.POJO.Image;
import com.csye6225HW1.POJO.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
//@Repository
public interface ImagesDao extends BaseMapper<Image> {
    @Select("select * from tbl_image where product_id=#{product_id}")
    List<Image> selectList(Long productId);



}
