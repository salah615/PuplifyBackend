package com.example.puplify.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.puplify.Entities.UserDetails;

import java.util.Optional;

public interface UserDetailsRepository extends JpaRepository<UserDetails, Integer> {
    Optional<UserDetails> findByUserId(Integer userId);

}

