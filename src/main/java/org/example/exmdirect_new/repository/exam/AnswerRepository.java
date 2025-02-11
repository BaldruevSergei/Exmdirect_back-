package org.example.exmdirect_new.repository.exam;

import org.example.exmdirect_new.entity.exam.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {
    // Добавьте кастомные методы, если нужно
}
