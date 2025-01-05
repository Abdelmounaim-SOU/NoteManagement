package com.example.notemanagment.Repository;


import com.example.notemanagment.Models.Semester;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SemesterRepo extends JpaRepository<Semester, Integer> {
}
