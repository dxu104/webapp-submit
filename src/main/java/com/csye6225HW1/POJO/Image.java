package com.csye6225HW1.POJO;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.gitee.sunchenbin.mybatis.actable.annotation.IsNotNull;
import com.gitee.sunchenbin.mybatis.actable.annotation.Table;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;


@Data
@Table(name = "tbl_image")
public class Image {
    //primary key
    @TableId(type = IdType.ASSIGN_ID)
    @TableField
    @IsNotNull
    private String imageId;

    @TableField
    @IsNotNull
    private String userId;

    @TableField
    @IsNotNull
    private Long productId;

    @TableField
    @IsNotNull
    private String fileName;






    @TableField(fill = FieldFill.INSERT)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String dateCreated;



    @TableField
    @IsNotNull
    private String s3BucketPath;



//    @TableField(fill = FieldFill.INSERT_UPDATE)
//    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//    private String dateLastUpdated;

}





