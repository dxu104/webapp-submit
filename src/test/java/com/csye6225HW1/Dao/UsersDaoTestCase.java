package com.csye6225HW1.Dao;

import com.csye6225HW1.POJO.User;
import com.csye6225HW1.dao.UsersDao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UsersDaoTestCase {
    @Autowired
    private UsersDao usersDao;


    @Test
    void testSave() {
        User user = new User();
        user.setFirstName("Jane");
        user.setLastName("Doe");
        user.setPassword("somepassword");
        user.setUsername("jane.doe@example.com");
        usersDao.insert(user);

    }

}
