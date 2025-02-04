package org.example.exmdirect_new.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "students")
public class Student extends User {

    @ManyToOne
    @JoinColumn(name = "school_class_id", nullable = false)
    private SchoolClass schoolClass; // связь с классом
    // Явный конструктор
    public Student(Long id, String firstName, String lastName, String login, String password, String email, UserRole userRole, SchoolClass schoolClass) {
        super(id, firstName, lastName, login, password, email, userRole); // Вызов конструктора суперкласса
        this.schoolClass = schoolClass;
    }
}
