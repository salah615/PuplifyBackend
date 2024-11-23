package com.example.puplify.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.puplify.Entities.Course;
import com.example.puplify.Entities.Task;
import com.example.puplify.Entities.User;

import java.util.Date;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByUserId(User user);

    List<Task> findByCourse(Course course);

    List<Task> findByCourseIdOrderByDueDateAsc(Long courseId);
    long countByCompletedFalseAndDueDateBefore(Date date);

    List<Task> findByCourseId(Long courseId);

}