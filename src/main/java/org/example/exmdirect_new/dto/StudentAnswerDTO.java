package org.example.exmdirect_new.dto;

public class StudentAnswerDTO {
    public Long questionId;
    public String answer;

    // Для Jackson нужен пустой конструктор
    public StudentAnswerDTO() {
    }

    public StudentAnswerDTO(Long questionId, String answer) {
        this.questionId = questionId;
        this.answer = answer;
    }
}