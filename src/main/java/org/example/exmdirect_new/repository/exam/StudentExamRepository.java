package org.example.exmdirect_new.repository.exam;


import org.example.exmdirect_new.entity.exam.StudentExam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentExamRepository extends JpaRepository<StudentExam, Long> {
    List<StudentExam> findByStudentId(Long studentId);
}
