package org.example.exmdirect_new.entity.exam;
import java.util.List;
import java.util.stream.Collectors;

public class SubjectDTO {
    private Long id;
    private String name;
    private List<ExamDTO> exams;

    public SubjectDTO(Subject subject) {
        this.id = subject.getId();
        this.name = subject.getName();
        this.exams = subject.getExams().stream()
                .map(ExamDTO::new)
                .collect(Collectors.toList());
    }

    // Геттеры и сеттеры

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ExamDTO> getExams() {
        return exams;
    }

    public void setExams(List<ExamDTO> exams) {
        this.exams = exams;
    }
}
