package com.example.notemanagment.Repository;

import com.example.notemanagment.Models.Professor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfRepo  extends JpaRepository<Professor, Integer> {
    public Professor findByName(String  name);
}