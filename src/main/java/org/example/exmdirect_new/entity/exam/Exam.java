package org.example.exmdirect_new.entity.exam;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "exams")
public class Exam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name; // Название теста

    @Column(nullable = false)
    private Integer duration; // Продолжительность теста в минутах

    @ManyToOne
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject; // Связь с предметом

    @OneToMany(mappedBy = "exam", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<QuestionGroup> questionGroups; // Связанные группы вопросов

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date startTime; // Дата и время начала теста

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date endTime; // Дата и время окончания теста
}
