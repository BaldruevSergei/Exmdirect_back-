package org.example.exmdirect_new.controller;

import org.example.exmdirect_new.dto.LoginRequest;
import org.example.exmdirect_new.dto.StudentDto;
import org.example.exmdirect_new.entity.Student;
import org.example.exmdirect_new.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    // ✅ Обработка логина
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        Optional<Student> studentOpt = studentRepository.findByLogin(request.login);
        if (studentOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Неверный логин");
        }

        Student student = studentOpt.get();

        // ✅ сравнение по паролю: если в базе пароль захеширован
        if (!student.getPassword().equals(request.password) &&
                !passwordEncoder.matches(request.password, student.getPassword())) {
            return ResponseEntity.badRequest().body("Неверный пароль");
        }

        StudentDto response = new StudentDto(
                student.getId(),
                student.getFirstName(),
                student.getLastName(),
                student.getLogin(),
                student.getSchoolClass().getName(),
                student.getUserRole().name()
        );

        return ResponseEntity.ok(response);
    }

    // ✅ Обработка preflight (OPTIONS) — чтобы CORS не падал
    @RequestMapping(value = "/login", method = RequestMethod.OPTIONS)
    public ResponseEntity<?> handleOptions() {
        return ResponseEntity.ok().build();
    }
}
