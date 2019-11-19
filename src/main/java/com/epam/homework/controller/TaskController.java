package com.epam.homework.controller;

import com.epam.homework.entity.Task;
import com.epam.homework.entity.TaskPriority;
import com.epam.homework.entity.User;
import com.epam.homework.entity.UserDto;
import com.epam.homework.exception.SubscriptionException;
import com.epam.homework.service.TaskService;
import com.epam.homework.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/user/{userId}/task")
public class TaskController {

    private final TaskService taskService;
    private final UserService userService;

    @Autowired
    public TaskController(TaskService taskService, UserService userService) {
        this.taskService = taskService;
        this.userService = userService;
    }

    @PostMapping("/{taskId}/change-priority")
    public Task setTaskPriority(
            @PathVariable Long taskId,
            @RequestParam TaskPriority taskPriority
    ) {
        return taskService.setTaskPriority(taskId, taskPriority);
    }

    @PostMapping("/{taskId}/complete")
    public Task markTaskComplete(@PathVariable Long taskId) {
        return taskService.markTaskComplete(taskId, true);
    }

    @PostMapping("/{taskId}/not-complete")
    public Task markTaskNotComplete(@PathVariable Long taskId) {
        return taskService.markTaskComplete(taskId, false);
    }

    @PostMapping("/create")
    public Task createTask(
            @RequestParam(required = true) String description,
            @PathVariable Long userId
    ) {
        Task task = taskService.createTask(description, userId);
        System.out.println("Task created: \n" + task.getTaskDescription());
        return task;
    }

    @DeleteMapping("/{taskId}/delete")
    public void deleteTask(@PathVariable Long taskId) {
        taskService.deleteTask(taskId);
    }

    @GetMapping("/all")
    public List<Task> findAllUserTask(@PathVariable Long userId) {
        return taskService.findAllTasksByUserId(userId);
    }

    @GetMapping("/{taskId}")
    public Task findTaskById(@PathVariable Long taskId) {
        return taskService.findTask(taskId);
    }

    @PostMapping("/{taskId}/upload")
    public Task uploadFile(
            @PathVariable Long taskId,
            @RequestParam MultipartFile file,
            @PathVariable Long userId
    ) throws IOException {
        if (!userService.isSubscribed(userId)) {
            throw new SubscriptionException("Subscribe to upload files!");
        }
        return taskService.saveFile(taskId, file);
    }
}