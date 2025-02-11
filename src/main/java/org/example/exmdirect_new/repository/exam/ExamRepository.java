package org.example.exmdirect_new.repository.exam;

import org.example.exmdirect_new.entity.exam.Exam;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExamRepository extends JpaRepository<Exam, Long> {
    List<Exam> findBySubjectId(Long subjectId);
}
