package com.csye6225HW1.POJO;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.gitee.sunchenbin.mybatis.actable.annotation.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;


@Data
@Table
//@TableName("tbl_user")
//userDetail obejct
public class Product  {
    //primary key
    @TableId(type = IdType.ASSIGN_ID)
    @IsNotNull
    private Long id;


    @TableField
    @IsNotNull
    private String name;

    @TableField
    @IsNotNull
    private String description;

    @TableField
    @IsNotNull
    private String sku;


    @TableField
    @IsNotNull
    private String manufacturer;

    @TableField
    @IsNotNull
    private String quantity;

//    @JsonProperty("date_added")
    @TableField(fill = FieldFill.INSERT)
    @IgnoreUpdate
    //@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String dateAdded;

   // @JsonProperty("date_last_updated")
    @TableField(fill = FieldFill.INSERT_UPDATE)
   // @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date dateLastUpdated;

    //@JsonProperty("owne1r_user_id")

    @TableField
    private Long ownerUserId;}

/**
 * 全部采用actable自有的注解
 */
//@Table(comment = "actable简单配置")
//public class Product {
//
//    @IsKey
//    @IsAutoIncrement
//    private Long id;
//
//    @Column
//    @Index
//    @IsNotNull
//    private String name;
//
//    @Column
//    private Date createTime;
//
//    @Column(defaultValue = "false")
//    private Boolean isTrue;
//
//    @Column
//    private Integer age;
//
//    @Column
//    private BigDecimal price;
//
//    @Column
//    @Unique
//    private String identitycard;
//
//}



