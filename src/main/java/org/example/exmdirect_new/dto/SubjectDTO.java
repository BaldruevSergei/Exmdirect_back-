package org.example.exmdirect_new.dto;

public class SubjectDTO {
    private Long id;
    private String name;

    // Конструктор
    public SubjectDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    // Геттеры и сеттеры

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
