package com.example.notemanagment.Repository;

import com.example.notemanagment.Models.ModuleElement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ModuleElementRepository extends JpaRepository<ModuleElement, Long> {

    List<ModuleElement> findByModuleId(Long moduleId);
    List<ModuleElement> findAll();

    List<ModuleElement> findByProfessorId(Integer userId);
}
