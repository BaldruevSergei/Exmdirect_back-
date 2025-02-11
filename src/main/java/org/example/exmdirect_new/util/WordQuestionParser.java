package org.example.exmdirect_new.util;

import org.apache.poi.xwpf.usermodel.*;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.example.exmdirect_new.entity.exam.*;

import java.io.InputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WordQuestionParser {

    public static List<Question> parseQuestionsFromWord(InputStream inputStream) throws IOException {
        List<Question> questions = new ArrayList<>();

        try (XWPFDocument document = new XWPFDocument(inputStream)) {
            List<XWPFParagraph> paragraphs = document.getParagraphs();

            Question currentQuestion = null;
            boolean expectingAnswers = false;
            List<String> correctAnswers = new ArrayList<>(); // ✅ Список для хранения правильных ответов

            for (XWPFParagraph paragraph : paragraphs) {
                String text = paragraph.getText().trim();

                // Пропускаем пустые строки
                if (text.isEmpty()) {
                    expectingAnswers = false;
                    continue;
                }

                if (!expectingAnswers) {
                    // Новый вопрос
                    if (currentQuestion != null) {
                        // Если вопрос был SINGLE_CHOICE, сохраняем единственный правильный ответ
                        if (currentQuestion.getQuestionType() == QuestionType.SINGLE_CHOICE && !correctAnswers.isEmpty()) {
                            currentQuestion.setCorrectTextAnswer(correctAnswers.get(0));
                        }
                        // Если вопрос MULTIPLE_CHOICE, сохраняем список правильных ответов как строку
                        else if (currentQuestion.getQuestionType() == QuestionType.MULTIPLE_CHOICE && !correctAnswers.isEmpty()) {
                            currentQuestion.setCorrectTextAnswer(String.join(", ", correctAnswers));
                        }

                        questions.add(currentQuestion);
                    }

                    currentQuestion = new Question();
                    currentQuestion.setText(text);
                    currentQuestion.setAnswers(new ArrayList<>());
                    correctAnswers.clear(); // ✅ Очищаем правильные ответы для нового вопроса
                    expectingAnswers = true;
                } else if (currentQuestion != null) {
                    // Это строка с ответами
                    if (text.startsWith("*")) {
                        currentQuestion.setQuestionType(QuestionType.SINGLE_CHOICE);
                        String answerText = text.substring(1).trim();
                        currentQuestion.getAnswers().add(new Answer(answerText, true));
                        correctAnswers.add(answerText); // ✅ Добавляем правильный ответ
                    } else if (text.startsWith("#")) {
                        currentQuestion.setQuestionType(QuestionType.MULTIPLE_CHOICE);
                        String answerText = text.substring(1).trim();
                        currentQuestion.getAnswers().add(new Answer(answerText, true));
                        correctAnswers.add(answerText); // ✅ Добавляем правильный ответ
                    } else if (text.startsWith("[") && text.endsWith("]")) {
                        currentQuestion.setQuestionType(QuestionType.FREE_TEXT);
                        currentQuestion.setCorrectTextAnswer(text.substring(1, text.length() - 1).trim());
                    } else if (text.contains("=")) {
                        currentQuestion.setQuestionType(QuestionType.MATCHING);
                        String[] pair = text.split("=", 2);
                        String key = pair[0].trim();
                        String value = pair.length > 1 ? pair[1].trim() : "";

                        if (currentQuestion.getMatchingPairs() == null) {
                            currentQuestion.setMatchingPairs(new ArrayList<>());
                        }
                        currentQuestion.getMatchingPairs().add(new MatchingPair(key, value));
                    } else {
                        if (currentQuestion.getQuestionType() == null) {
                            currentQuestion.setQuestionType(QuestionType.ORDERING);
                        }
                        if (currentQuestion.getOrderedAnswers() == null) {
                            currentQuestion.setOrderedAnswers(new ArrayList<>());
                        }
                        currentQuestion.getOrderedAnswers().add(text.trim());
                    }
                }
            }

            // Добавляем последний вопрос
            if (currentQuestion != null) {
                if (currentQuestion.getQuestionType() == QuestionType.SINGLE_CHOICE && !correctAnswers.isEmpty()) {
                    currentQuestion.setCorrectTextAnswer(correctAnswers.get(0));
                } else if (currentQuestion.getQuestionType() == QuestionType.MULTIPLE_CHOICE && !correctAnswers.isEmpty()) {
                    currentQuestion.setCorrectTextAnswer(String.join(", ", correctAnswers));
                }

                questions.add(currentQuestion);
            }
        }
        return questions;
    }
}
