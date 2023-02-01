package com.csye6225HW1.util;

import com.csye6225HW1.POJO.User;

public class UserPassingLoginAuth {

    private static final ThreadLocal<User> container = new ThreadLocal<>();

    public static void saveUser(User user) {
        container.set(user);
    }

    public static User getUser(){
        return container.get();
    }

    public static void deleteUser(){
        container.remove();
    }


}

