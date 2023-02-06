package com.csye6225HW1;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication

//@MapperScan("com.csye6225HW1.dao")
@MapperScan({"com.gitee.sunchenbin.mybatis.actable.dao.*"})
@ComponentScan({"com.gitee.sunchenbin.mybatis.actable.manager.*"})


public class Hw1Application {
    public static void main(String[] args) {
        SpringApplication.run(Hw1Application.class, args);
    }

}
