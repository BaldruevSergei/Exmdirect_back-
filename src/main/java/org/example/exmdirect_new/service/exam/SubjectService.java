package org.example.exmdirect_new.service.exam;

import org.example.exmdirect_new.entity.exam.Subject;
import org.example.exmdirect_new.repository.exam.SubjectRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SubjectService {

    private final SubjectRepository subjectRepository;

    public SubjectService(SubjectRepository subjectRepository) {
        this.subjectRepository = subjectRepository;
    }

    // Создание предмета
    public Subject createSubject(Subject subject) {
        return subjectRepository.save(subject);
    }

    // Получение всех предметов
    public List<Subject> getAllSubjects() {
        return subjectRepository.findAll();
    }

    // Получение предмета по ID
    public Optional<Subject> getSubjectById(Long id) {
        return subjectRepository.findById(id);
    }

    // Удаление предмета по ID
    public void deleteSubject(Long id) {
        subjectRepository.deleteById(id);
    }
}
