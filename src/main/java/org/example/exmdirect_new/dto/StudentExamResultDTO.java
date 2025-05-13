package org.example.exmdirect_new.dto;

import lombok.Data;
import java.util.Date;

@Data
public class StudentExamResultDTO {
    private Long examId;
    private String examName;
    private String subjectName;
    private int score;
    private boolean completed;
    private Date completedAt;

    public StudentExamResultDTO(Long examId, String examName, String subjectName,
                                int score, boolean completed, Date completedAt) {
        this.examId = examId;
        this.examName = examName;
        this.subjectName = subjectName;
        this.score = score;
        this.completed = completed;
        this.completedAt = completedAt;
    }
}
