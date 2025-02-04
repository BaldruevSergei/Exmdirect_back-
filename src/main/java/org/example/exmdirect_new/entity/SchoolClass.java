package org.example.exmdirect_new.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "school_classes")
public class SchoolClass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "class_teacher_id")
    private Teacher classTeacher;

    @Column(nullable = false)
    private String name; // Название класса, например, "10A"

    @OneToMany(mappedBy = "schoolClass", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Student> students;

    @OneToMany(mappedBy = "schoolClass", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ClassSubject> subjects;

    public SchoolClass(String name, Teacher classTeacher) {
        this.name = name;
        this.classTeacher = classTeacher;

    }
}
