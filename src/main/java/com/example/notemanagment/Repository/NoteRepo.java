package com.example.notemanagment.Repository;

import com.example.notemanagment.Models.Evaluation;
import com.example.notemanagment.Models.Note;
import com.example.notemanagment.Models.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NoteRepo extends JpaRepository<Note, Long> {
    Optional<Note> findByStudentAndEvaluation(Student student, Evaluation evaluation);
}
