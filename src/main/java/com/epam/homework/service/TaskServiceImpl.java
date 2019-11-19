package com.epam.homework.service;

import com.epam.homework.dao.TaskRepository;
import com.epam.homework.dao.UserRepository;
import com.epam.homework.entity.Task;
import com.epam.homework.entity.TaskPriority;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import com.epam.homework.entity.UserDto;
import com.epam.homework.exception.EntityNotFoundException;
import com.epam.homework.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;

@Service
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final ServletContext context;
    // @Value("${upload.path}")
    private final String UPLOAD_PATH;// = "classpath:uploads";

    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository, UserRepository userRepository, ServletContext context) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.context = context;
        UPLOAD_PATH = context.getRealPath("uploads");
        System.out.println(UPLOAD_PATH);
    }

    @Override
    public Task createTask(String description, Long userId) {

        Task task = Task.builder()
                .taskDescription(description)
                .priority(TaskPriority.MEDIUM)
                .taskComplete(false)
                .fileName("")
                .user(userRepository.findById(userId).orElseThrow(
                        () -> new UserNotFoundException("No user found!")))
                .build();
        return taskRepository.save(task);
    }

    @Override
    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }

    @Override
    public List<Task> findAllTasksByUserId(Long userId) {
        return taskRepository.findAllByUserId(userId);
    }

    @Override
    public Task setTaskPriority(Long taskId, TaskPriority taskPriority) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException("No task found!"));
        task.setPriority(taskPriority);
        return taskRepository.save(task);
    }

    @Override
    public Task markTaskComplete(Long taskId, Boolean completed) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException("No task found!"));
        task.setTaskComplete(completed);
        return taskRepository.save(task);
    }

    @Override
    public Task saveFile(Long taskId, MultipartFile file) throws IOException {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException("Task not fount!"));
        if (file != null && !file.getOriginalFilename().isEmpty()) {
            File destination = new File(UPLOAD_PATH);
            if (!destination.exists()) {
                destination.mkdirs();
            }

            String filename = UUID.randomUUID().toString() + "."
                    + file.getOriginalFilename();
            file.transferTo(new File(UPLOAD_PATH + "/" + filename));
            task.setFileName(filename);
            task = taskRepository.save(task);
        }
        return task;
    }

    @Override
    public Task findTask(Long taskId) {
        return taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException("No task found!"));
    }
}
