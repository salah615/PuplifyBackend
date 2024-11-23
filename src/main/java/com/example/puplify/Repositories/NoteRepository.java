package com.example.puplify.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.example.puplify.Entities.Note;
import com.example.puplify.Entities.User;

import java.util.List;

@Repository
public interface NoteRepository extends JpaRepository<Note,Long> {
    List<Note> findByUserId(User user);
}
