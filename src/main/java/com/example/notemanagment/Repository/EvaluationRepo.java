package com.example.notemanagment.Repository;

import com.example.notemanagment.Models.Evaluation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EvaluationRepo extends JpaRepository<Evaluation, Long> {
}
