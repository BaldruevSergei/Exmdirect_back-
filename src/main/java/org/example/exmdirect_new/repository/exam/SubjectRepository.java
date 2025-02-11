package org.example.exmdirect_new.repository.exam;

import org.example.exmdirect_new.entity.exam.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {
    // Добавьте кастомные методы, если нужно
}
