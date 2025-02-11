package org.example.exmdirect_new.repository.exam;

import org.example.exmdirect_new.entity.exam.QuestionGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionGroupRepository extends JpaRepository<QuestionGroup, Long> {
    // Добавьте кастомные методы, если нужно
}
