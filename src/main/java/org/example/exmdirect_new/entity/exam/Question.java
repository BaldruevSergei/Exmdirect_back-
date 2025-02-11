package org.example.exmdirect_new.entity.exam;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "questions")
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String text; // Текст вопроса

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private QuestionType questionType; // Тип вопроса

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Answer> answers; // Варианты ответа (если применимо)

    @Column(nullable = true)
    private String correctTextAnswer; // Верный ответ (если TEXT_INPUT)

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MatchingPair> matchingPairs; // Соответствия (если MATCHING)

    @ElementCollection
    @CollectionTable(name = "ordered_answers", joinColumns = @JoinColumn(name = "question_id"))
    @Column(name = "ordered_item")
    private List<String> orderedAnswers; // Упорядоченный список (если ORDERING)

    @ManyToOne
    @JoinColumn(name = "question_group_id", nullable = false)
    private QuestionGroup questionGroup; // Связь с группой вопросов
}
