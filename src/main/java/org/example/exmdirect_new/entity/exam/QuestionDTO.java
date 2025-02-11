package org.example.exmdirect_new.entity.exam;


import lombok.Data;
import org.example.exmdirect_new.entity.exam.Question;
import org.example.exmdirect_new.entity.exam.Answer;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class QuestionDTO {
    private Long id;
    private String text;
    private String questionType;
    private List<String> correctAnswers; // Правильные ответы

    public QuestionDTO(Question question) {
        this.id = question.getId();
        this.text = question.getText();
        this.questionType = question.getQuestionType().name(); // Предполагаем, что это ENUM
        this.correctAnswers = question.getAnswers().stream()
                .filter(Answer::isCorrect) // Фильтруем только правильные ответы
                .map(Answer::getText) // Получаем текст правильных ответов
                .collect(Collectors.toList());
    }
}
