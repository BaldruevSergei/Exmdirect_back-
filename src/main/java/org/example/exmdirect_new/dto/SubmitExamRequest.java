package org.example.exmdirect_new.dto;
import java.util.List;

public class SubmitExamRequest {
    public Long studentId;
    public Long examId;
    public List<StudentAnswerDTO> answers;

    public SubmitExamRequest() {}

    public SubmitExamRequest(Long studentId, Long examId, List<StudentAnswerDTO> answers) {
        this.studentId = studentId;
        this.examId = examId;
        this.answers = answers;
    }
}