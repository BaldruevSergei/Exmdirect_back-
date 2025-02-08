package org.example.exmdirect_new.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "teachers")
public class Teacher  extends User {
    @Column(nullable = false)
    private String subject; // Преподаваемый предмет

    @Column(nullable = true)
    private String classMentor; // Класс, в котором учитель является классным руководителем (не обязательно)

    @OneToMany(mappedBy = "teacher", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ClassSubject> classSubjects; // В каких классах и какие предметы он ведет

    public Teacher(Long id, String firstName, String lastName, String login, String password, String email, UserRole userRole, String subject, String classMentor, List<ClassSubject> classSubjects) {
        super(id, firstName, lastName, login, password, email, userRole); // Вызов конструктора суперкласса
        this.subject = subject;
        this.classMentor = classMentor;
        this.classSubjects = classSubjects;
    }

}

