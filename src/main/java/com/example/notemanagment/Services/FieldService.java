package com.example.notemanagment.Services;

import com.example.notemanagment.Models.Field;
import com.example.notemanagment.Repository.FieldRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FieldService {

    @Autowired
    private FieldRepo fieldRepo;

    public Field findById(Integer id) {
        return fieldRepo.findById(id).orElseThrow(() -> new RuntimeException("Field not found"));
    }
}
