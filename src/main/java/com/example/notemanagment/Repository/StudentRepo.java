package com.example.notemanagment.Repository;

import com.example.notemanagment.Models.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;


public interface StudentRepo extends JpaRepository<Student, Long> {

    List<Student> findByFieldIdAndSemesterId(Integer fieldId, Long semesterId);
    List<Student> findBySemester_IdAndField_Id(Long semesterId, Long fieldId);
}
