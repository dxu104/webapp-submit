package com.csye6225HW1.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.csye6225HW1.POJO.User;
import com.csye6225HW1.util.UserDemo;
import org.springframework.security.core.userdetails.UserDetails;




public interface IUsersService extends IService<User> {


    UserDemo getUser(Long id);
    public UserDemo initialUser(User user);
    public void update(Long id,User user);
    public UserDetails loadUserByUsername(String username);

}
