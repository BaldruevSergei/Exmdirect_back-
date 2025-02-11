package org.example.exmdirect_new.entity.exam;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "matching_pairs")
public class MatchingPair {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String fixedPart; // Фиксированная строка

    @Column(nullable = false, columnDefinition = "TEXT")
    private String matchingAnswer; // Вариант ответа

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private Question question; // Связь с вопросом

    // Конструктор для парсера
    public MatchingPair(String fixedPart, String matchingAnswer) {
        this.fixedPart = fixedPart;
        this.matchingAnswer = matchingAnswer;
    }
}
