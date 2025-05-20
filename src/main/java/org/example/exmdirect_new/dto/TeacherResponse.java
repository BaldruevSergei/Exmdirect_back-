package org.example.exmdirect_new.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TeacherResponse {
    private Long id;
    private String login;
    private String plainPassword;
    private String firstName;
    private String lastName;
    private String email;
    private String subject;
}
