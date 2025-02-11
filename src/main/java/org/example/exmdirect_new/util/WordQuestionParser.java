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

            for (XWPFParagraph paragraph : paragraphs) {
                String text = paragraph.getText().trim();

                // Пропускаем пустые строки
                if (text.isEmpty()) {
                    expectingAnswers = false;
                    continue;
                }

                if (!expectingAnswers) {
                    // Новая строка без пустых строк сверху — это вопрос
                    if (currentQuestion != null) {
                        questions.add(currentQuestion);
                    }
                    currentQuestion = new Question();
                    currentQuestion.setText(text);
                    currentQuestion.setAnswers(new ArrayList<>()); // ✅ Инициализируем список ответов
                    expectingAnswers = true;
                } else if (currentQuestion != null) {
                    // Это строка с ответами
                    if (text.startsWith("*")) {
                        currentQuestion.setQuestionType(QuestionType.SINGLE_CHOICE);
                        currentQuestion.getAnswers().add(new Answer(text.substring(1).trim(), true));
                    } else if (text.startsWith("#")) {
                        currentQuestion.setQuestionType(QuestionType.MULTIPLE_CHOICE);
                        currentQuestion.getAnswers().add(new Answer(text.substring(1).trim(), true));
                    } else if (text.startsWith("[") && text.endsWith("]")) {
                        currentQuestion.setQuestionType(QuestionType.FREE_TEXT);
                        currentQuestion.setCorrectTextAnswer(text.substring(1, text.length() - 1).trim());
                    } else if (text.contains("=")) {
                        currentQuestion.setQuestionType(QuestionType.MATCHING);
                        String[] pair = text.split("=", 2);
                        String key = pair[0].trim();
                        String value = pair.length > 1 ? pair[1].trim() : "";

                        // ✅ Проверяем, что список не `null`, и инициализируем его
                        if (currentQuestion.getMatchingPairs() == null) {
                            currentQuestion.setMatchingPairs(new ArrayList<>());
                        }
                        currentQuestion.getMatchingPairs().add(new MatchingPair(key, value));
                    } else {
                        // Если тип еще не определен — устанавливаем "упорядочивание"
                        if (currentQuestion.getQuestionType() == null) {
                            currentQuestion.setQuestionType(QuestionType.ORDERING);
                        }

                        // ✅ Проверяем, что список `orderedAnswers` не `null`
                        if (currentQuestion.getOrderedAnswers() == null) {
                            currentQuestion.setOrderedAnswers(new ArrayList<>());
                        }
                        currentQuestion.getOrderedAnswers().add(text.trim());
                    }
                }
            }

            // Добавляем последний вопрос в список
            if (currentQuestion != null) {
                questions.add(currentQuestion);
            }
        }
        return questions;
    }
}
