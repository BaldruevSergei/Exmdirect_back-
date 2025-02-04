package org.example.exmdirect_new.service;


import org.example.exmdirect_new.entity.ClassSubject;
import org.example.exmdirect_new.entity.SchoolClass;
import org.example.exmdirect_new.entity.Teacher;
import org.example.exmdirect_new.repository.ClassSubjectRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClassSubjectService {

    private final ClassSubjectRepository classSubjectRepository;

    public ClassSubjectService(ClassSubjectRepository classSubjectRepository) {
        this.classSubjectRepository = classSubjectRepository;
    }

    public List<ClassSubject> getSubjectsByClass(SchoolClass schoolClass) {
        return classSubjectRepository.findBySchoolClass(schoolClass);
    }

    public List<ClassSubject> getSubjectsByTeacher(Teacher teacher) {
        return classSubjectRepository.findByTeacher(teacher);
    }

    public List<ClassSubject> getSubjectsByName(String subject) {
        return classSubjectRepository.findBySubject(subject);
    }

    public List<ClassSubject> getSubjectsByTeacherAndClass(Teacher teacher, SchoolClass schoolClass) {
        return classSubjectRepository.findByTeacherAndSchoolClass(teacher, schoolClass);
    }

    public Optional<ClassSubject> getSubjectById(Long id) {
        return classSubjectRepository.findById(id);
    }

    public ClassSubject saveSubject(ClassSubject classSubject) {
        return classSubjectRepository.save(classSubject);
    }

    public void deleteSubject(Long id) {
        classSubjectRepository.deleteById(id);
    }
}
