package com.example.notemanagment.Repository;

import com.example.notemanagment.Models.Module;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ModuleRepo extends JpaRepository<Module, Integer> {
    public Module findByname(String  name);
}
