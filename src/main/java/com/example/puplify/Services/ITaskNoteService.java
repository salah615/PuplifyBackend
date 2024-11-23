package com.example.puplify.Services;

import com.example.puplify.Entities.Course;
import com.example.puplify.Entities.Note;
import com.example.puplify.Entities.Task;

import java.util.List;

public interface ITaskNoteService {

    List<Note> getNotesByUser(long userId);
    Note createNote(Note note);
    Note getNoteById(long id);
    Note updateNoteById(long id, Note note);
    void deleteNoteById(long id);


    List<Task> getTasksByUser(long userId);

    List<Task> getTasksByCourse(long courseId);

    Task createTask(Task task);
    Task getTaskById(long id);
    Task updateTaskById(long id,Task task);
    void deleteTaskById(long id);

    // New methods for managing courses
    List<Course> getCourses();
    List<Course> getCoursesByUser(long userId); // For courses of professor
    Course createCourse(Course course);
    Course getCourseById(long id);
    Course updateCourseById(long id, Course course);
    void deleteCourseById(long id);

}