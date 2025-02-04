package org.example.exmdirect_new.util;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NameParser {

    // Регулярное выражение: Имя и фамилия могут содержать буквы (латиница и кириллица) и дефис
    private static final Pattern NAME_PATTERN = Pattern.compile("^([А-ЯЁа-яёA-Za-z-]+)\\s+([А-ЯЁа-яёA-Za-z-]+)$");

    /**
     * Разбирает полное имя на имя и фамилию
     * @param fullName строка с полным именем (Имя Фамилия)
     * @return массив из двух элементов: имя и фамилия
     * @throws IllegalArgumentException если формат имени неверный
     */
    public static String[] parseFullName(String fullName) {
        if (fullName == null || fullName.trim().isEmpty()) {
            throw new IllegalArgumentException("Имя не может быть пустым");
        }

        Matcher matcher = NAME_PATTERN.matcher(fullName.trim());

        if (matcher.matches()) {
            return new String[]{matcher.group(1), matcher.group(2)}; // [Имя, Фамилия]
        } else {
            throw new IllegalArgumentException("Неверный формат имени: " + fullName);
        }
    }
}
