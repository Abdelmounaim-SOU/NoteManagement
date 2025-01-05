package com.example.notemanagment.Repository;

import com.example.notemanagment.Models.Field;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FieldRepo extends JpaRepository<Field, Integer>  {
    public Field findByname(String  name);
}
