package com.epam.homework.service;

import com.epam.homework.entity.User;
import com.epam.homework.entity.UserDto;

public interface UserService {

    User registerNewUser(UserDto userDto);

    User signIn(UserDto userDto);

    void subscribe(String userEmail);

    boolean isSubscribed(Long userId);
}
