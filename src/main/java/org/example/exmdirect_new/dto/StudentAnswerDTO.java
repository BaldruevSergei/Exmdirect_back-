package org.example.exmdirect_new.dto;

import java.util.List;

public class StudentAnswerDTO {
    public Long questionId;
    public String answer; // Для TEXT_INPUT и SINGLE_CHOICE
    public List<String> selected; // Для MULTIPLE_CHOICE

    public StudentAnswerDTO() {
    }

    public StudentAnswerDTO(Long questionId, String answer) {
        this.questionId = questionId;
        this.answer = answer;
    }

    public StudentAnswerDTO(Long questionId, List<String> selected) {
        this.questionId = questionId;
        this.selected = selected;
    }
}
