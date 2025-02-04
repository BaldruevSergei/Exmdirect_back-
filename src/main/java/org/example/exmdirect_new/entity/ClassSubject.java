package org.example.exmdirect_new.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor // Автоматически создаст конструктор со всеми параметрами
@Table(name = "class_subjects")
public class ClassSubject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "teacher_id", nullable = false)
    private Teacher teacher;

    @ManyToOne
    @JoinColumn(name = "school_class_id", nullable = false)
    private SchoolClass schoolClass;

    @Column(nullable = false)
    private String subject;

    // Если вы хотите явный конструктор:
    public ClassSubject(Teacher teacher, SchoolClass schoolClass, String subject) {
        this.teacher = teacher;
        this.schoolClass = schoolClass;
        this.subject = subject;
    }
}
