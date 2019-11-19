package com.epam.homework.aspect;

import com.epam.homework.dao.UserRepository;
import com.epam.homework.entity.Task;
import com.epam.homework.entity.User;
import com.epam.homework.entity.UserDto;
import com.epam.homework.exception.TaskOverflowException;
import com.epam.homework.service.TaskService;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Aspect
@Component
public class SubscriptionCheck {
    private final UserRepository userRepository;
    private TaskService taskService;
    private static final String IS_SUBSCRIBED = "5ebe2294ecd0e0f08eab7690d2a6ee69";
    private static final int MAX_TASKS = 10;

    @Autowired
    public SubscriptionCheck(UserRepository userRepository, TaskService taskService) {
        this.userRepository = userRepository;
        this.taskService = taskService;
    }

    @Pointcut("execution(public * com.epam.homework.service.TaskServiceImpl.createTask(..)) && args(description, userDto,..))")
    public void onTaskCreate(String description, UserDto userDto) {
    }

    @Before("onTaskCreate(description, userDto)")
    public void beforeTaskCreate(String description, UserDto userDto) {
        System.out.println("Aspect is here!");
        System.out.println(userDto);
        User user = userRepository.findByEmail(userDto.getEmail());
        if (!user.getSubscription().equals(IS_SUBSCRIBED)) {
            List<Task> taskList = new ArrayList<>();
            taskList = taskService.findAllTasksByUserId(user.getId());
            if (taskList.size() >= 10) {
                throw new TaskOverflowException(
                        "Unsubscribed user can only have not more than 10 tasks!");
            }
        }
    }
}