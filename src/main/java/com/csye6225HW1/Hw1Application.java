package com.csye6225HW1;

import com.csye6225HW1.config.EncodeConfig;
import com.gitee.sunchenbin.mybatis.actable.manager.handler.StartUpHandler;
import com.gitee.sunchenbin.mybatis.actable.manager.handler.StartUpHandlerImpl;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication

@MapperScan({"com.csye6225HW1.dao","com.gitee.sunchenbin.mybatis.actable.dao.*"})

//当我们定义的@componentScan会覆盖@SpringBootApplication扫描的范围，也就是com.csye6225HW1
//因此我们需要在@componentScan中添加当前工程需要扫描的包范围。
@ComponentScan({"com.gitee.sunchenbin.mybatis.actable.manager.*","com.csye6225HW1"})

public class Hw1Application {
    public static void main(String[] args) {
        ConfigurableApplicationContext run = new SpringApplication(Hw1Application.class).run(args);
        //SpringApplication.run(Hw1Application.class, args);
        StartUpHandler bean =run.getBean(StartUpHandler.class, args);
        bean.startHandler();

    }

}
