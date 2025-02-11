package org.example.exmdirect_new.entity.exam;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.exmdirect_new.entity.Student;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "student_exams")
public class StudentExam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student; // Связь со студентом

    @ManyToOne
    @JoinColumn(name = "exam_id", nullable = false)
    private Exam exam; // Связь с экзаменом

    @Column(nullable = false)
    private Integer score; // Результат экзамена

    @Column(nullable = false)
    private boolean completed; // Завершен ли экзамен

    @Column(nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date completedAt; // Дата и время завершения экзамена
}
