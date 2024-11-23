package com.example.puplify.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.puplify.Entities.Task;
import com.example.puplify.Entities.User;
import com.example.puplify.Repositories.CourseRepository;
import com.example.puplify.Repositories.TaskRepository;
import com.example.puplify.Repositories.UserRepository;

import java.util.*;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private UserRepository userRepository;


    @GetMapping("/course/{courseId}/burndown")
    public List<Map<String, Object>> getCourseBurndownData(@PathVariable Long courseId) {
        List<Task> tasks = taskRepository.findByCourseIdOrderByDueDateAsc(courseId);

        int totalTasks = tasks.size();
        int remainingTasks = totalTasks;
        List<Map<String, Object>> burndownData = new ArrayList<>();

        for (Task task : tasks) {
            Map<String, Object> dataPoint = new HashMap<>();
            dataPoint.put("date", task.getDueDate());
            dataPoint.put("remainingTasks", remainingTasks);
            burndownData.add(dataPoint);

            if (task.isCompleted()) {
                remainingTasks--;
            }
        }

        return burndownData;
    }

    @GetMapping("/overview")
    public Map<String, Object> getOverview() {
        long totalTasks = taskRepository.count();
        long dueTasks = taskRepository.countByCompletedFalseAndDueDateBefore(new Date());
        long totalCourses = courseRepository.count();

        Map<String, Object> overview = new HashMap<>();
        overview.put("totalTasks", totalTasks);
        overview.put("dueTasks", dueTasks);
        overview.put("totalCourses", totalCourses);

        return overview;
    }

    @GetMapping("/users")
    public List<Map<String, Object>> getUsers() {
        List<User> users = userRepository.findAll();
        List<Map<String, Object>> userList = new ArrayList<>();

        for (User user : users) {
            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("role", user.getRole());
            userInfo.put("firstName", user.getFirstName());
            userInfo.put("lastName", user.getLastName());
            userInfo.put("email", user.getEmail());
            userList.add(userInfo);
        }

        return userList;
    }

}
