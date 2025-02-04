package org.example.exmdirect_new.service;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.example.exmdirect_new.entity.SchoolClass;
import org.example.exmdirect_new.entity.Student;
import org.example.exmdirect_new.entity.UserRole;
import org.example.exmdirect_new.repository.StudentRepository;
import org.example.exmdirect_new.util.NameParser;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class StudentService extends AbstractUserService<Student> {

    private final StudentRepository studentRepository;
    private final SchoolClassService schoolClassService;

    public StudentService(StudentRepository studentRepository, SchoolClassService schoolClassService) {
        super(studentRepository);
        this.studentRepository = studentRepository;
        this.schoolClassService = schoolClassService;
    }

    // Получить список студентов по ID класса
    public List<Student> getStudentsByClassId(Long classId) {
        return studentRepository.findBySchoolClass_Id(classId);
    }

    // Получить список студентов, которых обучает определённый учитель
    public List<Student> getStudentsByTeacherId(Long teacherId) {
        return studentRepository.findStudentsByTeacherId(teacherId);
    }

    // Импорт учеников из Excel (xls, xlsx)
    public void importStudentsFromExcel(MultipartFile file) throws IOException {
        // 1. Проверяем, что файл не пустой
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Файл пустой. Загрузите корректный файл.");
        }

        // 2. Проверяем формат файла
        String filename = file.getOriginalFilename();
        if (filename == null || (!filename.endsWith(".xls") && !filename.endsWith(".xlsx"))) {
            throw new IllegalArgumentException("Файл должен быть формата .xls или .xlsx");
        }

        Workbook workbook = null;
        try {
            // 3. Определяем тип Excel-файла
            if (filename.endsWith(".xls")) {
                workbook = new HSSFWorkbook(file.getInputStream());
            } else {
                workbook = new XSSFWorkbook(file.getInputStream());
            }

            // 4. Проверяем, что хотя бы один лист присутствует
            if (workbook.getNumberOfSheets() == 0) {
                throw new IllegalArgumentException("Файл не содержит листов.");
            }

            // 5. Получаем первый лист
            Sheet sheet = workbook.getSheetAt(0);
            if (sheet.getPhysicalNumberOfRows() == 0) {
                throw new IllegalArgumentException("Файл не содержит данных.");
            }

            // 6. Читаем заголовок (C1) и определяем класс
            Row headerRow = sheet.getRow(0);
            if (headerRow == null || headerRow.getCell(2) == null) {
                throw new IllegalArgumentException("Неверный формат файла: отсутствует заголовок в C1.");
            }

            String classInfo = headerRow.getCell(2).getStringCellValue();
            if (classInfo == null || !classInfo.contains(":")) {
                throw new IllegalArgumentException("Формат заголовка C1 неверный (должен содержать ':').");
            }

            String className = classInfo.split(":")[0].trim();

            // 7. Находим или создаём класс
            SchoolClass schoolClass = schoolClassService.getAllClasses().stream()
                    .filter(c -> c.getName().equals(className))
                    .findFirst()
                    .orElseGet(() -> schoolClassService.saveClass(new SchoolClass(className, null)));

            // 8. Читаем список студентов
            List<Student> students = new ArrayList<>();
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                Cell nameCell = row.getCell(1);
                if (nameCell == null || nameCell.getStringCellValue().trim().isEmpty()) {
                    System.err.println("Ошибка в строке " + (i + 1) + ": отсутствует имя и фамилия.");
                    continue;
                }

                String fullName = nameCell.getStringCellValue().trim();
                try {
                    String[] parsedName = NameParser.parseFullName(fullName);
                    String firstName = parsedName[0];
                    String lastName = parsedName[1];

                    Student student = Student.builder()
                            .firstName(firstName)
                            .lastName(lastName)
                            .login(firstName.toLowerCase() + "." + lastName.toLowerCase())
                            .password(encoder.encode("defaultPassword")) // Шифруем пароль
                            .userRole(UserRole.STUDENT)
                            .schoolClass(schoolClass)
                            .build();
                    students.add(student);
                } catch (IllegalArgumentException e) {
                    System.err.println("Ошибка в строке " + (i + 1) + ": " + e.getMessage());
                }
            }

            // 9. Сохраняем студентов в базу
            if (!students.isEmpty()) {
                studentRepository.saveAll(students);
                System.out.println("Загружено " + students.size() + " студентов.");
            } else {
                throw new IllegalArgumentException("Файл не содержит корректных записей.");
            }
        } finally {
            if (workbook != null) {
                workbook.close();
            }
        }
    }

}
