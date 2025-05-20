package org.example.exmdirect_new.service;

import org.example.exmdirect_new.dto.LoginRequest;
import org.example.exmdirect_new.entity.Teacher;
import org.example.exmdirect_new.repository.TeacherRepository;
import org.example.exmdirect_new.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    public String login(LoginRequest request) {
        Optional<Teacher> optionalTeacher = teacherRepository.findByLogin(request.getLogin());

        if (optionalTeacher.isEmpty()) {
            throw new RuntimeException("Пользователь не найден");
        }

        Teacher teacher = optionalTeacher.get();

        if (!passwordEncoder.matches(request.getPassword(), teacher.getPassword())) {
            throw new RuntimeException("Неверный пароль");
        }

        return jwtUtil.generateToken(teacher.getLogin());
    }
}
