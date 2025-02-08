package org.example.exmdirect_new.service;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.example.exmdirect_new.entity.Teacher;
import org.example.exmdirect_new.entity.UserRole;
import org.example.exmdirect_new.repository.TeacherRepository;
import org.example.exmdirect_new.util.PasswordValidator;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
@Transactional
public class TeacherService {

    private final TeacherRepository teacherRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public TeacherService(TeacherRepository teacherRepository) {
        this.teacherRepository = teacherRepository;
    }

    // **1. Добавление нового учителя**
    public Teacher addTeacher(Teacher teacher) {
        teacher.setLogin(generateUniqueLogin()); // Логин временный (TeacherXXXX)
        teacher.setPassword(passwordEncoder.encode(generateTemporaryPassword())); // Временный пароль
        return teacherRepository.save(teacher);
    }

    // **2. Получение всех учителей**
    public List<Teacher> getAllTeachers() {
        return teacherRepository.findAll();
    }

    // **3. Получение учителя по ID**
    public Optional<Teacher> getTeacherById(Long id) {
        return teacherRepository.findById(id);
    }

    // **4. Удаление учителя**
    public void deleteTeacher(Long id) {
        teacherRepository.deleteById(id);
    }

    // **5. Обновление данных учителя**
    public Teacher updateTeacher(Long id, Teacher updatedTeacher) {
        return teacherRepository.findById(id)
                .map(existingTeacher -> {
                    existingTeacher.setFirstName(updatedTeacher.getFirstName());
                    existingTeacher.setLastName(updatedTeacher.getLastName());
                    existingTeacher.setSubject(updatedTeacher.getSubject());
                    return teacherRepository.save(existingTeacher);
                }).orElseThrow(() -> new RuntimeException("Учитель не найден"));
    }

    // **6. Поиск учителей по предмету**
    public List<Teacher> findBySubject(String subject) {
        return teacherRepository.findBySubjectIgnoreCase(subject);
    }

    // **7. Загрузка учителей из Excel**
    public List<Teacher> importTeachersFromExcel(MultipartFile file) {
        List<Teacher> teachers = new ArrayList<>();
        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // Пропускаем заголовок

                Teacher teacher = new Teacher();
                teacher.setFirstName(getCellValue(row.getCell(0)));
                teacher.setLastName(getCellValue(row.getCell(1)));
                teacher.setSubject(getCellValue(row.getCell(2)));
                teacher.setUserRole(UserRole.TEACHER);

                // Генерация временного логина и пароля
                teacher.setLogin(generateUniqueLogin());
                teacher.setPassword(passwordEncoder.encode(generateTemporaryPassword()));

                teachers.add(teacher);
            }
            teacherRepository.saveAll(teachers);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при загрузке файла: " + e.getMessage());
        }
        return teachers;
    }

    // **8. Смена логина (почты) и пароля при первом входе**
    public String updateLoginAndPassword(Long teacherId, String newEmail, String newPassword) {
        Optional<Teacher> teacherOpt = teacherRepository.findById(teacherId);
        if (teacherOpt.isEmpty()) {
            return "Учитель не найден";
        }

        Teacher teacher = teacherOpt.get();

        // Проверка валидности пароля
        if (!PasswordValidator.isValidPassword(newPassword)) {
            return "Пароль не соответствует требованиям";
        }

        // Проверка уникальности email (логина)
        if (teacherRepository.findByLogin(newEmail).isPresent()) {
            return "Этот email уже используется";
        }

        teacher.setLogin(newEmail); // Email теперь становится логином
        teacher.setEmail(newEmail);
        teacher.setPassword(passwordEncoder.encode(newPassword));
        teacherRepository.save(teacher);
        return "Логин (email) и пароль успешно обновлены";
    }

    // **Генерация уникального логина**
    private String generateUniqueLogin() {
        Random random = new Random();
        String login;
        do {
            login = "Teacher" + (1000 + random.nextInt(9000));
        } while (teacherRepository.findByLogin(login).isPresent());
        return login;
    }

    // **Генерация временного пароля (6 случайных цифр)**
    private String generateTemporaryPassword() {
        Random random = new Random();
        return String.format("%06d", random.nextInt(1000000));
    }

    // **Получение строки из ячейки Excel**
    private String getCellValue(Cell cell) {
        return (cell == null) ? "" : cell.toString().trim();
    }

    public void deleteAll() {
        teacherRepository.deleteAll();
    }
}
