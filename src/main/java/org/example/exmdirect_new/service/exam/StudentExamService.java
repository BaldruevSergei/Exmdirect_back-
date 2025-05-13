package org.example.exmdirect_new.service.exam;

import org.example.exmdirect_new.dto.StudentAnswerDTO;
import org.example.exmdirect_new.dto.StudentExamResultDTO;
import org.example.exmdirect_new.entity.Student;
import org.example.exmdirect_new.entity.exam.*;
import org.example.exmdirect_new.repository.StudentRepository;
import org.example.exmdirect_new.repository.exam.ExamRepository;
import org.example.exmdirect_new.repository.exam.QuestionRepository;
import org.example.exmdirect_new.repository.exam.StudentExamRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class StudentExamService {

    private final StudentExamRepository studentExamRepository;
    private final ExamRepository examRepository;
    private final StudentRepository studentRepository;
    private final QuestionRepository questionRepository;


    public StudentExamService(StudentExamRepository studentExamRepository, ExamRepository examRepository, StudentRepository studentRepository, QuestionRepository questionRepository) {
        this.studentExamRepository = studentExamRepository;
        this.examRepository = examRepository;
        this.studentRepository = studentRepository;
        this.questionRepository = questionRepository;
    }

    // Начать экзамен
    public StudentExam startExam(Long studentId, Long examId) {
        return studentExamRepository.findByStudentIdAndExamId(studentId, examId)
                .orElseGet(() -> {
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
                });
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

    public int evaluateExam(Long examId, List<StudentAnswerDTO> answers) {
        List<Question> questions = questionRepository.findByExamId(examId);
        int score = 0;

        for (Question question : questions) {
            StudentAnswerDTO dto = answers.stream()
                    .filter(a -> a.questionId.equals(question.getId()))
                    .findFirst()
                    .orElse(null);

            if (dto == null) continue;

            switch (question.getQuestionType()) {
                case FREE_TEXT -> {
                    String correct = question.getCorrectTextAnswer();
                    if (correct != null && dto.answer != null &&
                            correct.trim().equalsIgnoreCase(dto.answer.trim())) {
                        score++;
                    }
                }

                case SINGLE_CHOICE -> {
                    if (dto.answer != null &&
                            question.getAnswers().stream()
                                    .anyMatch(a -> a.isCorrect() &&
                                            a.getText().equalsIgnoreCase(dto.answer))) {
                        score++;
                    }
                }

                case MULTIPLE_CHOICE -> {
                    if (dto.selected != null) {
                        List<String> correctOptions = question.getAnswers().stream()
                                .filter(Answer::isCorrect)
                                .map(Answer::getText)
                                .map(String::trim)
                                .toList();

                        Set<String> correctSet = new HashSet<>(correctOptions);
                        Set<String> studentSet = dto.selected.stream()
                                .map(String::trim)
                                .collect(Collectors.toSet());

                        if (correctSet.equals(studentSet)) {
                            score++;
                        }
                    }
                }

                // Остальные типы можно добавить позже:
                // MATCHING, ORDERING, BOOLEAN, FILE_UPLOAD и др.
            }
        }

        return score;
    }
    public List<StudentExamResultDTO> getResultsByStudentId(Long studentId) {
        List<StudentExam> exams = studentExamRepository.findByStudentId(studentId);

        return exams.stream()
                .map(e -> new StudentExamResultDTO(
                        e.getExam().getId(),
                        e.getExam().getName(),
                        e.getExam().getSubject().getName(),
                        e.getScore(),
                        e.isCompleted(),
                        e.getCompletedAt()
                ))
                .toList();
    }



}
