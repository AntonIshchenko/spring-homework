import config.ApplicationConfig;
import controller.UserController;
import entities.Task;
import entities.User;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Application {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext();

        context.register(ApplicationConfig.class);
        context.refresh();

        UserController userController = context.getBean(UserController.class);

        userController.singUp(
                "name", "email@dot.com", "+789456123", "password");
        User user = userController.singIn("email@dot.com", "password");

        userController.singUp(
                "name212121", "email2@dot.com", "+789456123", "password");
        User user2 = userController.singIn("email2@dot.com", "password");

        Task task = userController.createTask("kuhvckwgw", user);
        userController.createTask("kuhv", user);
        userController.createTask("kuhv11111111111", user2);
        userController.findAllUserTask(user.getId());

        userController.subscribe(user, "super");
        userController.subscribe(user, "secret");
        userController.subscribe(user, "secret");
        userController.markTaskComplete(task.getTaskId());

        userController.findAllUserTask(user.getId());
        userController.findAllUserTask(user2.getId());
        userController.markTaskComplete(3L);
        userController.findAllUserTask(user2.getId());


    }
}
