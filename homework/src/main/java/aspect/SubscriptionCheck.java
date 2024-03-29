package aspect;

import entities.Task;
import entities.User;
import exception.TaskOverflowException;
import java.util.ArrayList;
import java.util.List;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import service.TaskService;

@Aspect
@Component
public class SubscriptionCheck {

  @Autowired
  public SubscriptionCheck(TaskService taskService) {
    this.taskService = taskService;
  }

  private TaskService taskService;
  private static final String IS_SUBSCRIBED = "5ebe2294ecd0e0f08eab7690d2a6ee69";
  private static final int MAX_TASKS = 10;

  @Pointcut("execution(public * service.TaskServiceImpl.createTask(..)) && args(description, user,..))")
  public void onTaskCreate(String description, User user) {
  }

  @Before("onTaskCreate(description, user)")
  public void beforeTaskCreate(String description, User user) {
    System.out.println("Aspect is here!");
    System.out.println(user);
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
