package org.example.exmdirect_new.util;

import org.apache.poi.xwpf.usermodel.*;
import org.example.exmdirect_new.entity.exam.*;

import java.io.InputStream;
import java.io.IOException;
import java.util.*;

public class WordQuestionParser {

    public static List<Question> parseQuestionsFromWord(InputStream inputStream) throws IOException {
        List<Question> questions = new ArrayList<>();
        List<String> skippedQuestions = new ArrayList<>();
        Set<String> questionTexts = new HashSet<>();

        try (XWPFDocument document = new XWPFDocument(inputStream)) {
            List<XWPFParagraph> paragraphs = document.getParagraphs();

            Question currentQuestion = null;
            List<String> correctAnswers = new ArrayList<>();
            List<String> orderedAnswers = new ArrayList<>();
            List<MatchingPair> matchingPairs = new ArrayList<>();
            boolean expectingAnswers = false;

            for (XWPFParagraph paragraph : paragraphs) {
                String text = paragraph.getText().trim();

                if (text.isEmpty()) {
                    if (currentQuestion != null) {
                        finalizeQuestion(currentQuestion, correctAnswers, orderedAnswers, matchingPairs, skippedQuestions, questions, questionTexts);
                    }
                    currentQuestion = null;
                    correctAnswers.clear();
                    orderedAnswers.clear();
                    matchingPairs.clear();
                    expectingAnswers = false;
                    continue;
                }

                if (!expectingAnswers) {
                    currentQuestion = new Question();
                    currentQuestion.setText(text);
                    currentQuestion.setAnswers(new ArrayList<>());
                    expectingAnswers = true;
                } else if (currentQuestion != null) {
                    if (text.startsWith("*")) {
                        currentQuestion.setQuestionType(QuestionType.SINGLE_CHOICE);
                        String answerText = text.substring(1).trim();
                        currentQuestion.getAnswers().add(new Answer(answerText, true));
                        correctAnswers.add(answerText);
                    } else if (text.startsWith("#")) {
                        currentQuestion.setQuestionType(QuestionType.MULTIPLE_CHOICE);
                        String answerText = text.substring(1).trim();
                        currentQuestion.getAnswers().add(new Answer(answerText, true));
                        correctAnswers.add(answerText);
                    } else if (text.startsWith("[") && text.endsWith("]")) {
                        currentQuestion.setQuestionType(QuestionType.FREE_TEXT);
                        String answerText = text.substring(1, text.length() - 1).trim();
                        correctAnswers.add(answerText.toLowerCase());
                        if (answerText.matches("[0-9]+[.,]?[0-9]*")) {
                            correctAnswers.add(answerText.replace(",", "."));
                            correctAnswers.add(answerText.replace(".", ","));
                        }
                        currentQuestion.setCorrectTextAnswer(String.join(" | ", correctAnswers));
                    } else if (text.contains("==") || text.contains("--") || text.contains("=")) {
                        currentQuestion.setQuestionType(QuestionType.MATCHING);
                        String[] pair = text.split("==|--|=", 2);
                        if (pair.length == 2) {
                            matchingPairs.add(new MatchingPair(pair[0].trim(), pair[1].trim()));
                        }
                    } else if (!text.startsWith("*") && !text.startsWith("#") && !text.startsWith("[") && !text.endsWith("]")) {
                        if (currentQuestion.getQuestionType() == null || currentQuestion.getQuestionType() == QuestionType.MATCHING) {
                            currentQuestion.setQuestionType(QuestionType.ORDERING);
                        }
                        orderedAnswers.add(text);
                    }
                }
            }

            if (currentQuestion != null) {
                finalizeQuestion(currentQuestion, correctAnswers, orderedAnswers, matchingPairs, skippedQuestions, questions, questionTexts);
            }
        }

        System.out.println("Пропущенные вопросы без правильного ответа:");
        for (String question : skippedQuestions) {
            System.out.println("- " + question);
        }

        return questions;
    }

    private static void finalizeQuestion(Question question, List<String> correctAnswers, List<String> orderedAnswers, List<MatchingPair> matchingPairs, List<String> skippedQuestions, List<Question> questions, Set<String> questionTexts) {
        if (questionTexts.contains(question.getText())) {
            System.out.println("Дублирующийся вопрос пропущен: " + question.getText());
            return;
        }

        if (question.getQuestionType() == QuestionType.SINGLE_CHOICE && !correctAnswers.isEmpty()) {
            question.setCorrectTextAnswer(correctAnswers.get(0));
            questions.add(question);
            questionTexts.add(question.getText());
        } else if (question.getQuestionType() == QuestionType.MULTIPLE_CHOICE && !correctAnswers.isEmpty()) {
            question.setCorrectTextAnswer(String.join(", ", correctAnswers));
            questions.add(question);
            questionTexts.add(question.getText());
        } else if (question.getQuestionType() == QuestionType.FREE_TEXT) {
            if (correctAnswers.isEmpty()) {
                question.setCorrectTextAnswer("Ответ вводится с клавиатуры");
            }
            questions.add(question);
            questionTexts.add(question.getText());
        } else if (question.getQuestionType() == QuestionType.MATCHING) {
            if (!matchingPairs.isEmpty()) {
                question.setMatchingPairs(matchingPairs);
                question.setCorrectTextAnswer(formatMatchingPairs(matchingPairs));
                questions.add(question);
                questionTexts.add(question.getText());
            } else {
                skippedQuestions.add(question.getText() + " (не найдены пары соответствий)");
            }
        } else if (question.getQuestionType() == QuestionType.ORDERING) {
            if (!orderedAnswers.isEmpty()) {
                question.setOrderedAnswers(orderedAnswers);
                question.setCorrectTextAnswer(String.join(" -> ", orderedAnswers));
                questions.add(question);
                questionTexts.add(question.getText());
            } else {
                skippedQuestions.add(question.getText() + " (не найдены варианты для упорядочивания)");
            }
        } else {
            skippedQuestions.add(question.getText() + " (не найден правильный ответ)");
        }
    }

    private static String formatMatchingPairs(List<MatchingPair> pairs) {
        List<String> formattedPairs = new ArrayList<>();
        for (MatchingPair pair : pairs) {
            formattedPairs.add(pair.getFixedPart() + " == " + pair.getMatchingAnswer());
        }
        return String.join("; ", formattedPairs);
    }
}

