package org.example.exmdirect_new.service;

import org.example.exmdirect_new.entity.exam.QuestionGroup;
import org.example.exmdirect_new.repository.exam.QuestionGroupRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class QuestionGroupService {

    private final QuestionGroupRepository questionGroupRepository;

    public QuestionGroupService(QuestionGroupRepository questionGroupRepository) {
        this.questionGroupRepository = questionGroupRepository;
    }

    // Создание группы вопросов
    public QuestionGroup createQuestionGroup(QuestionGroup questionGroup) {
        return questionGroupRepository.save(questionGroup);
    }

    // Получение всех групп вопросов
    public List<QuestionGroup> getAllQuestionGroups() {
        return questionGroupRepository.findAll();
    }

    //  Получение группы вопросов по ID
    public Optional<QuestionGroup> getQuestionGroupById(Long id) {
        return questionGroupRepository.findById(id);
    }

    //  Удаление группы вопросов по ID
    public void deleteQuestionGroup(Long id) {
        questionGroupRepository.deleteById(id);
    }
}
