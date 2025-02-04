package org.example.exmdirect_new.service;


import org.example.exmdirect_new.entity.Teacher;
import org.example.exmdirect_new.repository.TeacherRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TeacherService extends AbstractUserService<Teacher> {

    private final TeacherRepository teacherRepository;

    public TeacherService(TeacherRepository teacherRepository) {
        super(teacherRepository);
        this.teacherRepository = teacherRepository;
    }

    // Получить учителей по предмету
    public List<Teacher> getTeachersBySubject(String subject) {
        return teacherRepository.findBySubjectIgnoreCase(subject);
    }

    // Получить классного руководителя определённого класса
    public Optional<Teacher> getClassMentor(String classMentor) {
        return teacherRepository.findByClassMentor(classMentor);
    }

    // Получить список учителей, работающих в определённом классе
    public List<Teacher> getTeachersByClassId(Long classId) {
        return teacherRepository.findTeachersByClassId(classId);
    }
}
