package com.csye6225HW1.controller;

import com.csye6225HW1.POJO.User;
import com.csye6225HW1.service.IUsersService;
import com.csye6225HW1.util.UserDemo;
import com.csye6225HW1.util.UserPassingLoginAuth;
import com.timgroup.statsd.StatsDClient;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
@Slf4j
@RestController
@RequestMapping("/v1/users")
public class UserController {


    @Autowired
    IUsersService usersService;
    @Autowired
    private StatsDClient metricsClient;

    @GetMapping("/{id}")
    public UserDemo getUser(@PathVariable Long id) {
        log.info("Info message:get user");
        metricsClient.incrementCounter("endpoint.user.http.get");

        long startTime = System.currentTimeMillis();

        User LoginUser = UserPassingLoginAuth.getUser();
        if(!LoginUser.getId().equals(id)){
            throw new RuntimeException("");
        }

        UserDemo userdemo = usersService.getUser(id);

        long elapsedTime = System.currentTimeMillis() - startTime;
        metricsClient.recordExecutionTime("endpoint.imageId.http.get.timer", elapsedTime);
        log.info("Info message:get user；ElapsedTime = " +elapsedTime);
        return userdemo;

    }
@PostMapping("")
public UserDemo UserSignUp(@RequestBody User user) {
    log.info("Info message:post user");
    metricsClient.incrementCounter("endpoint.user.http.post");

    long startTime = System.currentTimeMillis();

    UserDemo userDemo =   usersService.initialUser(user);

    long elapsedTime = System.currentTimeMillis() - startTime;
    metricsClient.recordExecutionTime("endpoint.imageId.http.get.timer", elapsedTime);
    log.info("Info message:post user；ElapsedTime = " +elapsedTime);
    return userDemo;
}


    @PutMapping ("/{id}")
    public void updateUser(@PathVariable Long id, @RequestBody User user) {
        log.info("Info message:put user");
        metricsClient.incrementCounter("endpoint.user.http.put");

        long startTime = System.currentTimeMillis();

        User LoginUser = UserPassingLoginAuth.getUser();
        if(!LoginUser.getId().equals(id)){
            throw new RuntimeException();
        }
        if(!user.getUsername().equals(LoginUser.getUsername())){
            throw new RuntimeException();
        }

        if( user.getAccountCreated()!=null||user.getAccountUpdated()!=null)

        {
            throw new RuntimeException();
        }

        usersService.update(id, user);

        long elapsedTime = System.currentTimeMillis() - startTime;
        metricsClient.recordExecutionTime("endpoint.imageId.http.get.timer", elapsedTime);
        log.info("Info message:put user；ElapsedTime = " +elapsedTime);

    }




//   @DeleteMapping("/{id}")
//   //@PathVariable = let us know the id is from the url path
//   public boolean delete(@PathVariable  Integer id) {
//        return usersService.removeById(id);

   }




