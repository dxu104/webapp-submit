package com.csye6225HW1.controller;

import com.csye6225HW1.POJO.User;
import com.csye6225HW1.service.IUsersService;
import com.csye6225HW1.util.UserDemo;
import com.csye6225HW1.util.UserPassingLoginAuth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/users")
public class UserController {

    @Autowired
    IUsersService usersService;

    @GetMapping("/{id}")
    public UserDemo getUser(@PathVariable Long id) {

        User LoginUser = UserPassingLoginAuth.getUser();
        if(!LoginUser.getId().equals(id)){
            throw new RuntimeException("");
        }

        return usersService.getUser(id);
    }

    @PostMapping("")
    public UserDemo UserSignUp(@RequestBody User user) {
        return usersService.initialUser(user);
    }


    @PutMapping ("/{id}")
    public void updateUser(@PathVariable Long id, @RequestBody User user) {

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
    }


//   @DeleteMapping("/{id}")
//   //@PathVariable = let us know the id is from the url path
//   public boolean delete(@PathVariable  Integer id) {
//        return usersService.removeById(id);

   }




