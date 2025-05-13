package org.example.exmdirect_new.entity.exam;

public class ExamDTO {
    public Long id;
    public String name;
    public int duration;
    public Long subjectId;
    public String subjectName;

    public ExamDTO(Exam exam) {
        this.id = exam.getId();
        this.name = exam.getName();
        this.duration = exam.getDuration();
        this.subjectId = exam.getSubject().getId();
        this.subjectName = exam.getSubject().getName();
    }

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

    public Long getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Long subjectId) {
        this.subjectId = subjectId;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }
}

