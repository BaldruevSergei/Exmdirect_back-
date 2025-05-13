package org.example.exmdirect_new.controller.exam;

import org.example.exmdirect_new.dto.StudentExamResultDTO;
import org.example.exmdirect_new.dto.SubmitExamRequest;
import org.example.exmdirect_new.entity.exam.StudentExam;
import org.example.exmdirect_new.service.exam.StudentExamService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<StudentExam> submitExam(@RequestBody SubmitExamRequest request) {
        int score = studentExamService.evaluateExam(request.examId, request.answers);
        StudentExam result = studentExamService.completeExam(request.studentId, request.examId, score);
        return ResponseEntity.ok(result);
    }

    // метод получения результата студента
    @GetMapping("/results/{studentId}")
    public ResponseEntity<List<StudentExamResultDTO>> getResults(@PathVariable Long studentId) {
        return ResponseEntity.ok(studentExamService.getResultsByStudentId(studentId));
    }



}
