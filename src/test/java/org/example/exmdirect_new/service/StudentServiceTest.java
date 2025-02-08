package org.example.exmdirect_new.service;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.example.exmdirect_new.entity.SchoolClass;
import org.example.exmdirect_new.entity.Student;
import org.example.exmdirect_new.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private SchoolClassService schoolClassService;

    @InjectMocks
    private StudentService studentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private MockMultipartFile createMockExcelFile() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Students");

        // Header row
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(2).setCellValue("10A: Класс");

        // Student rows
        Row row1 = sheet.createRow(1);
        row1.createCell(1).setCellValue("Иван Петров");

        Row row2 = sheet.createRow(2);
        row2.createCell(1).setCellValue("Мария Иванова");

        workbook.write(out);
        workbook.close();

        return new MockMultipartFile("file", "students.xlsx",
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                new ByteArrayInputStream(out.toByteArray()));
    }

    @Test
    void testImportStudentsFromExcel_Success() throws IOException {
        MockMultipartFile mockFile = createMockExcelFile();

        SchoolClass mockClass = new SchoolClass("10A", null);
        when(schoolClassService.getAllClasses()).thenReturn(List.of(mockClass));
        when(studentRepository.existsByFirstNameAndLastNameAndLogin(anyString(), anyString(), anyString())).thenReturn(false);

        studentService.importStudentsFromExcel(mockFile);

        verify(studentRepository, times(1)).saveAll(anyList());
    }

    @Test
    void testImportStudentsFromExcel_EmptyFile_ShouldThrowException() {
        MockMultipartFile emptyFile = new MockMultipartFile("file", "empty.xlsx",
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", new byte[0]);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> studentService.importStudentsFromExcel(emptyFile));
        assertEquals("Файл пустой. Загрузите корректный файл.", exception.getMessage());
    }

    @Test
    void testImportStudentsFromExcel_InvalidFormat_ShouldThrowException() {
        MockMultipartFile invalidFile = new MockMultipartFile("file", "students.txt", "text/plain", "invalid data".getBytes());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> studentService.importStudentsFromExcel(invalidFile));
        assertEquals("Файл должен быть формата .xls или .xlsx", exception.getMessage());
    }
}
