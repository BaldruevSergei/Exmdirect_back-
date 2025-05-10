package org.example.exmdirect_new.dto;

import lombok.Data;

@Data
public class StudentDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String login;
    private String className;
    private String role;

    public StudentDto(Long id, String firstName, String lastName, String login, String className, String role) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.login = login;
        this.className = className;
        this.role = role;
    }
}
