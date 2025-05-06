package org.example.exmdirect_new.controller;


import org.example.exmdirect_new.dto.LoginRequest;
import org.example.exmdirect_new.entity.Student;
import org.example.exmdirect_new.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin(origins = "*") // только для тестов
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        Optional<Student> studentOpt = studentRepository.findByLogin(request.login);
        if (studentOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Неверный логин");
        }

        Student student = studentOpt.get();
        if (!student.getPassword().equals(request.password)) {
            return ResponseEntity.badRequest().body("Неверный пароль"); // временно
        }


        return ResponseEntity.ok("OK:" + student.getId() + ":" + student.getFirstName());
    }

}
