package org.example.exmdirect_new.service.exam;

import org.example.exmdirect_new.dto.StudentAnswerDTO;
import org.example.exmdirect_new.entity.Student;
import org.example.exmdirect_new.entity.exam.Exam;
import org.example.exmdirect_new.entity.exam.Question;
import org.example.exmdirect_new.entity.exam.StudentExam;
import org.example.exmdirect_new.repository.StudentRepository;
import org.example.exmdirect_new.repository.exam.ExamRepository;
import org.example.exmdirect_new.repository.exam.QuestionRepository;
import org.example.exmdirect_new.repository.exam.StudentExamRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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

    public int evaluateExam(Long examId, List<StudentAnswerDTO> answers) {
        // Найди все вопросы этого экзамена (реализуй метод findByExamId)
        List<Question> questions = questionRepository.findByExamId(examId);

        Map<Long, String> correctAnswers = questions.stream()
                .collect(Collectors.toMap(
                        Question::getId,
                        Question::getCorrectTextAnswer
                ));

        int score = 0;
        for (StudentAnswerDTO answer : answers) {
            String correct = correctAnswers.get(answer.questionId);
            if (correct != null && correct.trim().equalsIgnoreCase(answer.answer.trim())) {
                score++;
            }
        }

        return score;
    }
}
