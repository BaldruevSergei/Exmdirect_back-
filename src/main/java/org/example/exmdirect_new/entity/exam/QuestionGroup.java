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
@Table(name = "question_groups")
public class QuestionGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name; // Название группы вопросов

    @ManyToOne
    @JoinColumn(name = "exam_id", nullable = false)
    private Exam exam; // Связь с экзаменом

    @OneToMany(mappedBy = "questionGroup", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Question> questions; // Связь с вопросами
}
