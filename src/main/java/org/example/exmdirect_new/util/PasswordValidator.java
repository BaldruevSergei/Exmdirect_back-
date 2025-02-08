package org.example.exmdirect_new.util;

import java.util.regex.Pattern;

public class PasswordValidator {

    private static final String PASSWORD_PATTERN = "^(?=.*[a-z])(?=.*[A-Z]).{6,}$";

    private PasswordValidator() {
        // Приватный конструктор, чтобы предотвратить создание экземпляров класса
    }

    public static boolean isValidPassword(String password) {
        return Pattern.matches(PASSWORD_PATTERN, password);
    }
}
