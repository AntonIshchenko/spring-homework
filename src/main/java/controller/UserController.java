package controller;

import entities.Task;

public interface UserController  {

    void singUp();
    void singIn();
    Task createTask();
    void deleteTask(Integer taskId);
    void findAllUserTask(Integer userId);
    void markTaskComplete(Task task);
    void markTaskIncomplete(Task task);
}
