package org.example.exmdirect_new.controller.exam;

import io.swagger.v3.oas.annotations.Operation;
import org.example.exmdirect_new.entity.exam.ExamDTO;
import org.example.exmdirect_new.entity.exam.QuestionDTO;
import org.example.exmdirect_new.entity.exam.SubjectDTO;
import org.example.exmdirect_new.repository.exam.SubjectRepository;
import org.example.exmdirect_new.service.exam.ExamService;
import org.example.exmdirect_new.service.exam.QuestionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/questions")
public class QuestionController {

    private final QuestionService questionService;
    private final SubjectRepository subjectRepository;
    private final ExamService examService;

    public QuestionController(QuestionService questionService, SubjectRepository subjectRepository, ExamService examService) {
        this.questionService = questionService;
        this.subjectRepository = subjectRepository;
        this.examService = examService;

    }



    /**
     * Загрузка вопросов из файла Word и сохранение в указанную группу вопросов.
     *
     * @param file   Файл с вопросами в формате Word.
     * @param groupId ID группы вопросов.
     * @return Статус выполнения операции.
     */
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Загрузка вопросов из файла Word")
    public ResponseEntity<String> uploadQuestions(
            @RequestPart("file") MultipartFile file,
            @RequestParam("groupId") Long groupId) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("Файл пустой. Загрузите корректный файл.");
            }
            questionService.uploadQuestionsFromFile(file, groupId);
            return ResponseEntity.ok("Вопросы успешно загружены.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка при загрузке вопросов.");
        }
    }


    /**
     * Получить все вопросы по ID группы.
     *
     * @param groupId ID группы вопросов.
     * @return Список вопросов.
     */
    @GetMapping("/group/{groupId}")
    public ResponseEntity<List<QuestionDTO>> getQuestionsByGroup(@PathVariable Long groupId) {
        try {
            List<QuestionDTO> questions = questionService.getQuestionsByGroup(groupId)
                    .stream()
                    .map(QuestionDTO::new) // Преобразуем в DTO
                    .collect(Collectors.toList());
            return ResponseEntity.ok(questions);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }



    /**
     * Удаление вопроса по ID.
     *
     * @param questionId ID вопроса.
     * @return Статус выполнения операции.
     */
    @DeleteMapping("/{questionId}")
    public ResponseEntity<String> deleteQuestion(@PathVariable Long questionId) {
        try {
            questionService.deleteQuestion(questionId);
            return ResponseEntity.ok("Вопрос успешно удален.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Ошибка: " + e.getMessage());
        }
    }
    @DeleteMapping("/group/{groupId}/deleteAll")
    public ResponseEntity<String> deleteAllQuestions(@PathVariable Long groupId) {
        try {
            questionService.deleteAllQuestions(groupId);
            return ResponseEntity.ok("Все вопросы удалены для группы ID: " + groupId);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка удаления вопросов");
        }
    }


    @GetMapping("/subjects")
    public List<SubjectDTO> getSubjects() {
        return subjectRepository.findAll().stream()
                .map(SubjectDTO::new)
                .collect(Collectors.toList());
    }

    // Получить все экзамены
    @GetMapping("/exams")
    public ResponseEntity<List<ExamDTO>> getExams() {
        List<ExamDTO> exams = examService.getAllExams();
        return ResponseEntity.ok(exams);
    }

    // Получить экзамены по subjectId
    @GetMapping("/exams/subject/{subjectId}")
    public ResponseEntity<List<ExamDTO>> getExamsBySubject(@PathVariable Long subjectId) {
        List<ExamDTO> exams = examService.getExamsBySubject(subjectId);
        return ResponseEntity.ok(exams);
    }
    @GetMapping("/group/{groupId}/answers")
    public ResponseEntity<List<QuestionDTO>> getQuestionsWithAnswers(@PathVariable Long groupId) {
        List<QuestionDTO> questions = questionService.getQuestionsByGroup(groupId)
                .stream()
                .map(QuestionDTO::new) // Преобразование Question -> QuestionDTO
                .collect(Collectors.toList());

        return ResponseEntity.ok(questions);
    }


}
