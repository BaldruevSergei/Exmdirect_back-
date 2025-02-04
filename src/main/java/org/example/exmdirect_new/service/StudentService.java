package org.example.exmdirect_new.service;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.example.exmdirect_new.entity.SchoolClass;
import org.example.exmdirect_new.entity.Student;
import org.example.exmdirect_new.entity.UserRole;
import org.example.exmdirect_new.repository.StudentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.*;

@Service
public class StudentService {

    private static final Logger logger = LoggerFactory.getLogger(StudentService.class);

    private final StudentRepository studentRepository;
    private final SchoolClassService schoolClassService;

    public StudentService(StudentRepository studentRepository, SchoolClassService schoolClassService) {
        this.studentRepository = studentRepository;
        this.schoolClassService = schoolClassService;
    }

    // Получить список студентов, которых обучает определённый учитель
    public List<Student> getStudentsByTeacherId(Long teacherId) {
        return studentRepository.findStudentsByTeacherId(teacherId);
    }

    // Получить всех студентов
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    // Импорт студентов из Excel (xls, xlsx)
    public void importStudentsFromExcel(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Файл пустой. Загрузите корректный файл.");
        }

        String filename = file.getOriginalFilename();
        if (filename == null || (!filename.endsWith(".xls") && !filename.endsWith(".xlsx"))) {
            throw new IllegalArgumentException("Файл должен быть формата .xls или .xlsx");
        }

        Workbook workbook = null;
        try {
            workbook = filename.endsWith(".xls")
                    ? new HSSFWorkbook(file.getInputStream())
                    : new XSSFWorkbook(file.getInputStream());

            if (workbook.getNumberOfSheets() == 0) {
                throw new IllegalArgumentException("Файл не содержит листов.");
            }

            Sheet sheet = workbook.getSheetAt(0);
            if (sheet.getPhysicalNumberOfRows() == 0) {
                throw new IllegalArgumentException("Файл не содержит данных.");
            }

            Row headerRow = sheet.getRow(0);
            if (headerRow == null || headerRow.getCell(2) == null) {
                throw new IllegalArgumentException("Отсутствует заголовок класса в C1.");
            }

            String classInfo = headerRow.getCell(2).getStringCellValue();
            if (classInfo == null || !classInfo.contains(":")) {
                throw new IllegalArgumentException("Неверный формат заголовка в C1 (должен содержать ':').");
            }

            String className = classInfo.split(":")[0].trim();
            SchoolClass schoolClass = schoolClassService.getAllClasses().stream()
                    .filter(c -> c.getName().equals(className))
                    .findFirst()
                    .orElseGet(() -> schoolClassService.saveClass(new SchoolClass(className, null)));

            List<Student> students = new ArrayList<>();
            SecureRandom random = new SecureRandom();

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) {
                    logger.warn("Пустая строка. Пропускаем строку {}", i + 1);
                    continue;
                }

                Cell nameCell = row.getCell(1);
                if (nameCell == null || nameCell.getCellType() == CellType.BLANK) {
                    logger.warn("Пустое поле имени. Пропускаем строку {}", i + 1);
                    continue;
                }

                String fullName = nameCell.getStringCellValue().trim();
                if (fullName.isEmpty()) {
                    logger.warn("Пустое поле ФИО. Пропускаем строку {}", i + 1);
                    continue;
                }

                try {
                    String[] parsedName = parseFullName(fullName);
                    String firstName = parsedName[0];
                    String lastName = parsedName[1];

                    String login = generateLogin(firstName, lastName);

                    // Проверяем, существует ли студент с таким же именем, фамилией и логином
                    if (studentRepository.existsByFirstNameAndLastNameAndLogin(firstName, lastName, login)) {
                        logger.warn("Дублирующий студент {} {} с логином {} уже существует. Пропускаем строку {}", firstName, lastName, login, i + 1);
                        continue;
                    }

                    Student student = Student.builder()
                            .firstName(firstName)
                            .lastName(lastName)
                            .login(login)
                            .password(new BCryptPasswordEncoder().encode("defaultPassword"))
                            .userRole(UserRole.STUDENT)
                            .schoolClass(schoolClass)
                            .build();
                    students.add(student);
                } catch (IllegalArgumentException e) {
                    logger.error("Ошибка в строке {}: {}", i + 1, e.getMessage());
                }
            }

            if (!students.isEmpty()) {
                studentRepository.saveAll(students);
                logger.info("Загружено {} студентов.", students.size());
            } else {
                throw new IllegalArgumentException("Файл не содержит корректных записей.");
            }
        } finally {
            if (workbook != null) {
                workbook.close();
            }
        }
    }

    // Метод для генерации логина
    private String generateLogin(String firstName, String lastName) {
        return (firstName.toLowerCase() + "." + lastName.toLowerCase()).replaceAll("[^a-zа-я0-9.]", "");
    }

    // Метод для разбора полного имени
    private String[] parseFullName(String fullName) {
        fullName = fullName.trim();

        // Разбиваем на части
        String[] parts = fullName.split("\\s+");
        if (parts.length < 2) {
            throw new IllegalArgumentException("Неверный формат имени: " + fullName);
        }

        String firstName = parts[0]; // Первое слово — имя
        String lastName = parts[1];  // Второе слово — фамилия

        // Проверяем, если фамилия содержит монгольское окончание "-гийн", "-ын", "-ийн"
        if (lastName.matches(".*(гийн|ын|ийн|н)$") && parts.length > 2) {
            lastName = String.join(" ", Arrays.copyOfRange(parts, 1, parts.length)); // Фамилия состоит из нескольких слов
        }

        return new String[]{firstName, lastName};
    }
}
