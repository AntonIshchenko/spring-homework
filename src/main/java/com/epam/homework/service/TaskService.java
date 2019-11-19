package com.epam.homework.service;

import com.epam.homework.entity.Task;
import com.epam.homework.entity.TaskPriority;
import com.epam.homework.entity.User;
import com.epam.homework.entity.UserDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface TaskService {
    Task createTask(String description, Long userId);

    void deleteTask(Long id);

    List<Task> findAllTasksByUserId(Long userId);

    Task setTaskPriority(Long taskId, TaskPriority taskPriority);

    Task markTaskComplete(Long taskId, Boolean completed);

    Task saveFile(Long taskId, MultipartFile file) throws IOException;

    Task findTask(Long taskId);
}