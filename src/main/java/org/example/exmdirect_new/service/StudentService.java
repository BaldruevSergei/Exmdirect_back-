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
import org.springframework.beans.factory.annotation.Autowired;
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
    private final BCryptPasswordEncoder passwordEncoder;
    private final SecureRandom random = new SecureRandom();

    @Autowired
    public StudentService(StudentRepository studentRepository,
                          SchoolClassService schoolClassService,
                          BCryptPasswordEncoder passwordEncoder) {
        this.studentRepository = studentRepository;
        this.schoolClassService = schoolClassService;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Student> getStudentsByTeacherId(Long teacherId) {
        return studentRepository.findStudentsByTeacherId(teacherId);
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public void importStudentsFromExcel(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Файл пустой. Загрузите корректный файл.");
        }

        Workbook workbook = file.getOriginalFilename().endsWith(".xls")
                ? new HSSFWorkbook(file.getInputStream())
                : new XSSFWorkbook(file.getInputStream());

        Sheet sheet = workbook.getSheetAt(0);
        List<Student> students = new ArrayList<>();

        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row == null || row.getCell(1) == null || row.getCell(1).getCellType() == CellType.BLANK) {
                logger.warn("Пропускаем строку {} из-за отсутствия данных", i + 1);
                continue;
            }

            String fullName = readCellAsString(row.getCell(1));
            if (fullName.isEmpty()) {
                logger.warn("Пропускаем строку {} из-за пустого имени", i + 1);
                continue;
            }

            String[] parsedName = parseFullName(fullName);
            String firstName = parsedName[0];
            String lastName = parsedName[1];

            String className = readCellAsString(row.getCell(2)).trim();
            SchoolClass schoolClass = schoolClassService.getAllClasses().stream()
                    .filter(c -> c.getName().equals(className))
                    .findFirst()
                    .orElseGet(() -> schoolClassService.saveClass(new SchoolClass(className, null)));

            if (studentRepository.existsByFirstNameAndLastNameAndSchoolClassId(firstName, lastName, schoolClass.getId())) {
                logger.warn("Студент {} {} уже существует в классе {}. Пропускаем строку {}", firstName, lastName, className, i + 1);
                continue;
            }

            String login = generateLogin(schoolClass);

            Student student = Student.builder()
                    .firstName(firstName)
                    .lastName(lastName)
                    .login(login)
                    .password(passwordEncoder.encode("defaultPassword"))
                    .userRole(UserRole.STUDENT)
                    .schoolClass(schoolClass)
                    .build();
            students.add(student);
        }

        if (!students.isEmpty()) {
            studentRepository.saveAll(students);
            logger.info("Загружено {} студентов.", students.size());
        }

        workbook.close();
    }

    private String readCellAsString(Cell cell) {
        if (cell == null) return "";
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue().trim();
            case NUMERIC -> String.valueOf((int) cell.getNumericCellValue());
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            default -> "";
        };
    }

    private String generateLogin(SchoolClass schoolClass) {
        int classNumber = extractClassNumber(schoolClass.getName());
        String login;
        do {
            int randomDigits = 1000 + random.nextInt(9000);
            login = "User" + classNumber + randomDigits;
        } while (studentRepository.findByLogin(login).isPresent());
        return login;
    }

    private int extractClassNumber(String className) {
        String digits = className.replaceAll("\\D", "");
        return digits.isEmpty() ? 0 : Integer.parseInt(digits);
    }

    private String[] parseFullName(String fullName) {
        fullName = fullName.trim();
        String[] parts = fullName.split("\\s+");
        if (parts.length < 2) {
            throw new IllegalArgumentException("Неверный формат имени: " + fullName);
        }
        return new String[]{parts[0], parts[1]};
    }

    public void deleteAll() {
        studentRepository.deleteAll();
    }

    public String updateEmail(Long studentId, String newEmail) {
        Optional<Student> studentOpt = studentRepository.findById(studentId);
        if (studentOpt.isEmpty()) {
            return "Студент не найден";
        }
        Student student = studentOpt.get();
        if (studentRepository.findByLogin(newEmail).isPresent()) {
            return "Этот email уже используется";
        }
        student.setLogin(newEmail);
        student.setEmail(newEmail);
        studentRepository.save(student);
        return "Email успешно обновлен";
    }
}
