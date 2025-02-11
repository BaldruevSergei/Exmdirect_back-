package org.example.exmdirect_new.service.exam;

import org.example.exmdirect_new.entity.exam.Exam;
import org.example.exmdirect_new.entity.exam.ExamDTO;
import org.example.exmdirect_new.repository.exam.ExamRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExamService {

    private final ExamRepository examRepository;

    public ExamService(ExamRepository examRepository) {
        this.examRepository = examRepository;
    }

    // Получить все экзамены
    public List<ExamDTO> getAllExams() {
        return examRepository.findAll().stream()
                .map(ExamDTO::new)  // Преобразуем в DTO
                .collect(Collectors.toList());
    }

    // Получить экзамены по subjectId
    public List<ExamDTO> getExamsBySubject(Long subjectId) {
        return examRepository.findBySubjectId(subjectId).stream()
                .map(ExamDTO::new)
                .collect(Collectors.toList());
    }
}
