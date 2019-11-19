package com.epam.homework.service;

import com.epam.homework.entity.AuthenticationDto;
import com.epam.homework.entity.User;
import com.epam.homework.entity.UserDto;

public interface UserService {

    User registerNewUser(UserDto userDto);

    User signIn(AuthenticationDto authDto);

    User getUser(Long userId);

    void subscribe(String userEmail);

    boolean isSubscribed(Long userId);
}
