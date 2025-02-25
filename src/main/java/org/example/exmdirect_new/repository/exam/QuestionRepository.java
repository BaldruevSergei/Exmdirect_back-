package org.example.exmdirect_new.repository.exam;


import jakarta.transaction.Transactional;
import org.example.exmdirect_new.entity.exam.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findByQuestionGroupId(Long groupId);
    @Transactional
    void deleteByQuestionGroupId(Long groupId); // Удаляет все вопросы по ID группы
    Optional<Question> findByTextAndQuestionGroupId(String text, Long questionGroupId);
}

