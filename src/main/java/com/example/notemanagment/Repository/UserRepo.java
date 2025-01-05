package com.example.notemanagment.Repository;


import com.example.notemanagment.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<User , Integer> {
    public User findByUsername(String username);
}