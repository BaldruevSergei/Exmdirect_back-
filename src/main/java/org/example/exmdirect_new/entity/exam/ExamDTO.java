package org.example.exmdirect_new.entity.exam;

public class ExamDTO {
    private Long id;
    private String name;
    private int duration;

    public ExamDTO(Exam exam) {
        this.id = exam.getId();
        this.name = exam.getName();
        this.duration = exam.getDuration();
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

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
