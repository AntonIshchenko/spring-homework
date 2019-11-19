package com.epam.homework.service;

import com.epam.homework.dao.TaskRepository;
import com.epam.homework.dao.UserRepository;
import com.epam.homework.entity.AuthenticationDto;
import com.epam.homework.entity.User;
import com.epam.homework.entity.UserDto;
import com.epam.homework.entity.UserRole;
import com.epam.homework.exception.UserNotFoundException;
import com.epam.homework.exception.WrongPassword;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final String KEYWORD = "secret";
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, TaskRepository taskRepository) {
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
    }

    @Override
    public User registerNewUser(UserDto userDto) {
        User user = new User().builder()
                .email(userDto.getEmail())
                .name(userDto.getName())
                .password(userDto.getPassword())
                .phoneNumber(userDto.getNumber())
                .userRole(UserRole.ORDINARY_USER)
                .subscription("")
                .build();
        if (userRepository.save(user) == null) {
            throw new RuntimeException("DAO exception");
        }
        return user;
    }

  @Override
  public User signIn(AuthenticationDto authDto) {
    User user = userRepository.findByEmail(authDto.getEmail());
    if (user == null) {
      throw new UserNotFoundException("Wrong email");
    }
    if (!authDto.getPassword().equals(user.getPassword())) {
      throw new WrongPassword("Wrong password");
    }
    return user;
  }



    @Override
    public void subscribe(String userEmail) {
        User user = userRepository.findByEmail(userEmail);
        String subscriptionKey = DigestUtils.md5Hex(KEYWORD);
        if (user.getSubscription().equals(subscriptionKey)) {
            System.out.println("Subscription already exists!");
            return;
        }
        user.setSubscription(subscriptionKey);
        userRepository.save(user);
    }

    @Override
    public boolean isSubscribed(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("No user found!"));
        return DigestUtils.md5Hex(KEYWORD).equals(user.getSubscription());
    }

    @Override
    public User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("No user found!"));
    }
}