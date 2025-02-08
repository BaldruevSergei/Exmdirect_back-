package org.example.exmdirect_new.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.example.exmdirect_new.entity.Teacher;
import org.example.exmdirect_new.service.TeacherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/teachers")
@CrossOrigin(origins = "*")
public class TeacherController {

    private static final Logger logger = LoggerFactory.getLogger(TeacherController.class);

    private final TeacherService teacherService;

    @Autowired
    public TeacherController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    // **1. Получение всех учителей**
    @GetMapping
    public List<Teacher> getAllTeachers() {
        return teacherService.getAllTeachers();
    }

    // **2. Удаление всех учителей**
    @DeleteMapping("/delete-all")
    public ResponseEntity<String> deleteAllTeachers() {
        teacherService.deleteAll();
        return ResponseEntity.ok("Все учителя были удалены.");
    }

    // **3. Загрузка файла Excel с учителями (как в StudentController)**
    @Operation(summary = "Импорт учителей из Excel файла")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Учителя успешно загружены"),
            @ApiResponse(responseCode = "400", description = "Ошибка в формате файла"),
            @ApiResponse(responseCode = "500", description = "Ошибка на стороне сервера")
    })
    @PostMapping(value = "/import", consumes = "multipart/form-data")
    public ResponseEntity<String> importTeachers(@RequestPart("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Ошибка: Файл пустой");
            }

            logger.info("Файл успешно загружен: {}", file.getOriginalFilename());
            logger.info("Размер файла: {} байт", file.getSize());
            teacherService.importTeachersFromExcel(file);
            return ResponseEntity.ok("Учителя успешно загружены из файла.");
        } catch (IllegalArgumentException e) {
            logger.error("Ошибка при обработке файла", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Ошибка: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Ошибка сервера при загрузке файла", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Произошла ошибка при загрузке файла.");
        }
    }
}
