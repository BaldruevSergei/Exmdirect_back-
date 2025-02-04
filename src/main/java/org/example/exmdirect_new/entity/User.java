package org.example.exmdirect_new.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;


@Data
@SuperBuilder
@AllArgsConstructor
@ToString
@NoArgsConstructor
@MappedSuperclass // Указывает, что это базовый класс для других сущностей
public abstract class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String login;

    @Column(nullable = false)
    private String password;

    @Column(nullable = true, unique = true)
    private String email; // Email будет уникальным после обновления пользователем

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole userRole; // Enum для ролей
}
