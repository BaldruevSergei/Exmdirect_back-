package org.example.exmdirect_new.service;

import org.example.exmdirect_new.entity.Student;
import org.example.exmdirect_new.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService extends AbstractUserService<Student> {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        super(studentRepository);
        this.studentRepository = studentRepository;
    }

    // Получить список студентов по ID класса
    public List<Student> getStudentsByClassId(Long classId) {
        return studentRepository.findBySchoolClass_Id(classId);
    }

    // Получить список студентов, которых обучает определённый учитель
    public List<Student> getStudentsByTeacherId(Long teacherId) {
        return studentRepository.findStudentsByTeacherId(teacherId);
    }
}
