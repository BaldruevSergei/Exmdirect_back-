package org.example.exmdirect_new.entity.exam;

import jakarta.persistence.*;

@Entity
@Table(name = "test_entity")
public class TestEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;
}
