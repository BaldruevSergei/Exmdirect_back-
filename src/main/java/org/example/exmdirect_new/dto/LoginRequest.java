package org.example.exmdirect_new.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String login;
    private String password;
}