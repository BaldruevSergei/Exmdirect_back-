package org.example.exmdirect_new.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.example.exmdirect_new.entity.Student;
import org.example.exmdirect_new.service.StudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/students")
@CrossOrigin(origins = "*")
public class StudentController {

    private static final Logger logger = LoggerFactory.getLogger(StudentController.class);

    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    public List<Student> getAllStudents() {
        return studentService.getAllStudents();
    }
    @DeleteMapping("/students")
    public ResponseEntity<String> deleteAllStudents() {
        studentService.deleteAll(); // Ensure this method is implemented in your service
        return ResponseEntity.ok("All students have been deleted.");
    }


    @Operation(summary = "Импорт студентов из Excel файла")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Студенты успешно загружены"),
            @ApiResponse(responseCode = "400", description = "Ошибка в формате файла"),
            @ApiResponse(responseCode = "500", description = "Ошибка на стороне сервера")
    })
    @PostMapping(value = "/import", consumes = "multipart/form-data")
    public ResponseEntity<String> importStudents(@RequestPart("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Ошибка: Файл пустой");
            }

            logger.info("Файл успешно загружен: {}", file.getOriginalFilename());
            logger.info("Размер файла: {} байт", file.getSize());
            studentService.importStudentsFromExcel(file);
            return ResponseEntity.ok("Студенты успешно загружены из файла.");
        } catch (IllegalArgumentException e) {
            logger.error("Ошибка при обработке файла", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Ошибка: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Ошибка сервера при загрузке файла", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Произошла ошибка при загрузке файла.");
        }
    }
}
