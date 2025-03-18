package org.example.exmdirect_new.controller.exam;

import org.example.exmdirect_new.entity.exam.StudentExam;
import org.example.exmdirect_new.service.exam.StudentExamService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/student-exams")
public class StudentExamController {

    private final StudentExamService studentExamService;

    public StudentExamController(StudentExamService studentExamService) {
        this.studentExamService = studentExamService;
    }

    // Начать экзамен
    @PostMapping("/start")
    public ResponseEntity<StudentExam> startExam(@RequestParam Long studentId, @RequestParam Long examId) {
        return ResponseEntity.ok(studentExamService.startExam(studentId, examId));
    }

    // Завершить экзамен и отправить баллы
    @PostMapping("/submit")
    public ResponseEntity<StudentExam> submitExam(@RequestParam Long studentId, @RequestParam Long examId, @RequestParam Integer score) {
        return ResponseEntity.ok(studentExamService.completeExam(studentId, examId, score));
    }
}
