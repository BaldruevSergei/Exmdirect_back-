package org.example.exmdirect_new.repository.exam;


import org.example.exmdirect_new.entity.exam.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findByQuestionGroupId(Long groupId);
}