package org.example.exmdirect_new.service.exam;

import org.example.exmdirect_new.entity.Student;
import org.example.exmdirect_new.entity.exam.Exam;
import org.example.exmdirect_new.entity.exam.StudentExam;
import org.example.exmdirect_new.repository.StudentRepository;
import org.example.exmdirect_new.repository.exam.ExamRepository;
import org.example.exmdirect_new.repository.exam.StudentExamRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class StudentExamService {

    private final StudentExamRepository studentExamRepository;
    private final ExamRepository examRepository;
    private final StudentRepository studentRepository;

    public StudentExamService(StudentExamRepository studentExamRepository, ExamRepository examRepository, StudentRepository studentRepository) {
        this.studentExamRepository = studentExamRepository;
        this.examRepository = examRepository;
        this.studentRepository = studentRepository;
    }

    // Начать экзамен
    public StudentExam startExam(Long studentId, Long examId) {
        Optional<StudentExam> existingExam = studentExamRepository.findByStudentIdAndExamId(studentId, examId);
        if (existingExam.isPresent()) {
            throw new IllegalArgumentException("Этот экзамен уже начат!");
        }

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Студент не найден"));

        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new IllegalArgumentException("Экзамен не найден"));

        StudentExam studentExam = new StudentExam();
        studentExam.setStudent(student);
        studentExam.setExam(exam);
        studentExam.setScore(0);
        studentExam.setCompleted(false);
        studentExam.setCompletedAt(null);

        return studentExamRepository.save(studentExam);
    }

    // Завершить экзамен
    public StudentExam completeExam(Long studentId, Long examId, Integer score) {
        StudentExam studentExam = studentExamRepository.findByStudentIdAndExamId(studentId, examId)
                .orElseThrow(() -> new IllegalArgumentException("Экзамен не найден"));

        studentExam.setScore(score);
        studentExam.setCompleted(true);
        studentExam.setCompletedAt(new Date());

        return studentExamRepository.save(studentExam);
    }
}
