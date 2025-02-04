package org.example.exmdirect_new.service;

import org.example.exmdirect_new.entity.SchoolClass;
import org.example.exmdirect_new.entity.Teacher;
import org.example.exmdirect_new.repository.SchoolClassRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SchoolClassService {

    private final SchoolClassRepository schoolClassRepository;

    public SchoolClassService(SchoolClassRepository schoolClassRepository) {
        this.schoolClassRepository = schoolClassRepository;
    }

    // Получить все классы
    public List<SchoolClass> getAllClasses() {
        return schoolClassRepository.findAll();
    }

    // Получить класс по ID
    public Optional<SchoolClass> getClassById(Long id) {
        return schoolClassRepository.findById(id);
    }

    // Получить все классы, которые курирует определённый учитель
    public List<SchoolClass> getClassesByTeacher(vTeacher teacher) {
        return schoolClassRepository.findByClassTeacher(teacher);
    }

    // Добавить новый класс
    public SchoolClass saveClass(SchoolClass schoolClass) {
        return schoolClassRepository.save(schoolClass);
    }

    // Удалить класс по ID
    public void deleteClass(Long id) {
        schoolClassRepository.deleteById(id);
    }
}
