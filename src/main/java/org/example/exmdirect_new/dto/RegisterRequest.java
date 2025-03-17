package org.example.exmdirect_new.dto;

import org.example.exmdirect_new.entity.UserRole;
import lombok.Data;

@Data
public class RegisterRequest {
    private String firstName;
    private String lastName;
    private String login;
    private String password;
    private String email;
    private UserRole userRole;
}