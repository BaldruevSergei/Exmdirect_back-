package org.example.exmdirect_new.controller;

import org.example.exmdirect_new.entity.Teacher;
import org.example.exmdirect_new.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        Optional<Teacher> teacherOpt = teacherRepository.findByLogin(request.getLogin());

        if (teacherOpt.isEmpty()) {
            return ResponseEntity.status(401).body("Пользователь не найден");
        }

        Teacher teacher = teacherOpt.get();
        if (!passwordEncoder.matches(request.getPassword(), teacher.getPassword())) {
            return ResponseEntity.status(401).body("Неверный пароль");
        }

        // В проде желательно вернуть JWT
        return ResponseEntity.ok(new AuthResponse(
                teacher.getId(),
                teacher.getLogin(),
                teacher.getFirstName(),
                teacher.getLastName(),
                teacher.getUserRole().toString()
        ));
    }

    // DTO запроса
    public static class LoginRequest {
        private String login;
        private String password;

        // Getters & setters
        public String getLogin() {
            return login;
        }
        public void setLogin(String login) {
            this.login = login;
        }
        public String getPassword() {
            return password;
        }
        public void setPassword(String password) {
            this.password = password;
        }
    }

    // DTO ответа
    public static class AuthResponse {
        private Long id;
        private String login;
        private String firstName;
        private String lastName;
        private String role;

        public AuthResponse(Long id, String login, String firstName, String lastName, String role) {
            this.id = id;
            this.login = login;
            this.firstName = firstName;
            this.lastName = lastName;
            this.role = role;
        }

        // Getters
        public Long getId() { return id; }
        public String getLogin() { return login; }
        public String getFirstName() { return firstName; }
        public String getLastName() { return lastName; }
        public String getRole() { return role; }
    }
}
