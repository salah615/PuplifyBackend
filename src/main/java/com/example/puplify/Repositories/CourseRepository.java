package com.example.puplify.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.puplify.Entities.Course;
import com.example.puplify.Entities.Note;
import com.example.puplify.Entities.User;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course,Long>{

    List<Course> findByUserId(User user);

}
