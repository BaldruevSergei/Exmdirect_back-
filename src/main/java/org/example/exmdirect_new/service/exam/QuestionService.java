package org.example.exmdirect_new.service.exam;

import org.example.exmdirect_new.entity.exam.Question;
import org.example.exmdirect_new.entity.exam.QuestionGroup;
import org.example.exmdirect_new.repository.exam.QuestionGroupRepository;
import org.example.exmdirect_new.repository.exam.QuestionRepository;
import org.example.exmdirect_new.util.WordQuestionParser;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final QuestionGroupRepository questionGroupRepository;

    public QuestionService(QuestionRepository questionRepository, QuestionGroupRepository questionGroupRepository) {
        this.questionRepository = questionRepository;
        this.questionGroupRepository = questionGroupRepository;
    }

    // Сохранение одного вопроса
    public Question createQuestion(Question question) {
        return questionRepository.save(question);
    }

    // Получение всех вопросов
    public List<Question> getAllQuestions() {
        return questionRepository.findAll();
    }

    // Получение вопросов по группе
    public List<Question> getQuestionsByGroup(Long groupId) {
        return questionRepository.findByQuestionGroupId(groupId);
    }

    // Удаление вопроса
    public void deleteQuestion(Long id) {
        questionRepository.deleteById(id);
    }
    // Удаление всех вопросов по ID группы
    public void deleteAllQuestions(Long groupId) {
        questionRepository.deleteByQuestionGroupId(groupId);
    }
    // Сохранение группы вопросов
    public QuestionGroup createQuestionGroup(QuestionGroup group) {
        return questionGroupRepository.save(group);
    }

    // Получение всех групп вопросов
    public List<QuestionGroup> getAllQuestionGroups() {
        return questionGroupRepository.findAll();
    }


    // Загрузка вопросов из файла Word
    public void uploadQuestionsFromFile(MultipartFile file, Long groupId) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Файл пустой. Загрузите корректный файл.");
        }

        System.out.println("Файл загружен, начинаем парсинг...");

        try {
            List<Question> questions = WordQuestionParser.parseQuestionsFromWord(file.getInputStream());
            System.out.println("Вопросов найдено: " + questions.size());

            if (questions.isEmpty()) {
                throw new IllegalArgumentException("Не найдено ни одного вопроса. Проверьте структуру документа.");
            }

            // Получаем группу вопросов
            QuestionGroup group = questionGroupRepository.findById(groupId)
                    .orElseThrow(() -> new IllegalArgumentException("Группа вопросов не найдена"));

            System.out.println("Группа найдена: " + group.getName());

            // Сохраняем вопросы
            for (Question question : questions) {
                System.out.println("Добавление вопроса: " + question.getText());

                if (question.getCorrectTextAnswer() == null || question.getCorrectTextAnswer().isEmpty()) {
                    throw new IllegalArgumentException("Ошибка: У вопроса '" + question.getText() + "' нет правильного ответа.");
                }

                question.setQuestionGroup(group);
                questionRepository.save(question);
            }

            System.out.println("Все вопросы сохранены.");

        } catch (Exception e) {
            System.err.println("Ошибка при обработке файла: " + e.getMessage());
            e.printStackTrace(); // Выведет полный стек ошибки в консоль
            throw new RuntimeException("Ошибка загрузки вопросов", e);
        }
    }
}