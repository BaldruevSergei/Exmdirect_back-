package org.example.exmdirect_new.repository;

import com.example.exmdirect_new.entity.ClassSubject;
import com.example.exmdirect_new.entity.SchoolClass;
import com.example.exmdirect_new.entity.Teacher;
import com.example.exmdirect_new.entity.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class TeacherRepositoryTest {

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private SchoolClassRepository schoolClassRepository;

    @Autowired
    private ClassSubjectRepository classSubjectRepository;

    private SchoolClass class1;
    private SchoolClass class2;
    private Teacher teacher1;
    private Teacher teacher2;

    @BeforeEach
    void setUp() {
        // Создаём классы
        class1 = new SchoolClass();
        class1.setName("10A");
        schoolClassRepository.save(class1);

        class2 = new SchoolClass();
        class2.setName("11B");
        schoolClassRepository.save(class2);

        // Создаём учителей
        teacher1 = new Teacher(null, "Alice", "Smith", "asmith", "password", "asmith@example.com", UserRole.TEACHER, "Mathematics", "10A", null);
        teacher2 = new Teacher(null, "Bob", "Johnson", "bjohnson", "password", "bjohnson@example.com", UserRole.TEACHER, "Physics", "11B", null);

        teacherRepository.save(teacher1);
        teacherRepository.save(teacher2);

        // 🔥 Добавляем предмет, чтобы работал `findTeachersByClassId()`
        ClassSubject subject1 = new ClassSubject(null, teacher1, class1, "Mathematics");
        classSubjectRepository.save(subject1);
    }

    @Test
    void findBySubject() {
        List<Teacher> teachers = teacherRepository.findBySubject("Mathematics");

        assertEquals(1, teachers.size());
        assertEquals("Alice", teachers.get(0).getFirstName());
    }

    @Test
    void findByClassMentor() {
        Optional<Teacher> classMentor = teacherRepository.findByClassMentor("10A");

        assertTrue(classMentor.isPresent());
        assertEquals("Alice", classMentor.get().getFirstName());
    }

    @Test
    void findBySubjectIgnoreCase() {
        List<Teacher> teachers = teacherRepository.findBySubjectIgnoreCase("mathematics");

        assertEquals(1, teachers.size());
        assertEquals("Alice", teachers.get(0).getFirstName());
    }

    @Test
    void findTeachersByClassId() {
        Long classId = class1.getId(); //  Используем правильный ID класса!

        List<Teacher> teachers = teacherRepository.findTeachersByClassId(classId);

        assertEquals(1, teachers.size());
        assertEquals("Alice", teachers.get(0).getFirstName());
    }
}
