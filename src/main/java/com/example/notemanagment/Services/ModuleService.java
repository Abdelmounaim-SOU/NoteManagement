package com.example.notemanagment.Services;

import com.example.notemanagment.Models.Module;
import com.example.notemanagment.Repository.ModuleRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ModuleService {
    @Autowired
    private ModuleRepo moduleRepository;

    public void save(Module module) {
        moduleRepository.save(module);
    }
}
