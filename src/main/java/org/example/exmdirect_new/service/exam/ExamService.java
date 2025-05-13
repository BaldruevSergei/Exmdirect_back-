package org.example.exmdirect_new.service.exam;

import org.example.exmdirect_new.entity.exam.Exam;
import org.example.exmdirect_new.entity.exam.ExamDTO;
import org.example.exmdirect_new.repository.exam.ExamRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ExamService {

    private final ExamRepository examRepository;

    public ExamService(ExamRepository examRepository) {
        this.examRepository = examRepository;
    }

    // Создание нового экзамена
    public Exam createExam(Exam exam) {
        return examRepository.save(exam);
    }

    // Получить все экзамены (возвращаем DTO)
    public List<ExamDTO> getAllExams() {
        return examRepository.findAll().stream()
                .map(ExamDTO::new)
                .toList();
    }


    public List<ExamDTO> getBySubjectId(Long subjectId) {
        return examRepository.findBySubjectId(subjectId).stream()
                .map(ExamDTO::new)
                .toList();
    }


    // Получить экзамен по ID
    public Optional<Exam> getExamById(Long id) {
        return examRepository.findById(id);
    }

    // Получить экзамены по предмету
    public List<ExamDTO> getExamsBySubject(Long subjectId) {
        return examRepository.findBySubjectId(subjectId).stream()
                .map(ExamDTO::new)
                .collect(Collectors.toList());
    }

    // Удаление экзамена по ID
    public void deleteExam(Long id) {
        examRepository.deleteById(id);
    }
}
