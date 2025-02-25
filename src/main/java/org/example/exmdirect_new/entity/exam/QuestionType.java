package org.example.exmdirect_new.entity.exam;


public enum QuestionType {
    SINGLE_CHOICE,    // Вопрос с одним правильным ответом (*)
    MULTIPLE_CHOICE,  // Вопрос с несколькими правильными ответами (#)
    FREE_TEXT,        // Вопрос со свободным вводом ([ответ])
    MATCHING,         // Вопрос на соответствие (==, --, =)
    ORDERING          // Вопрос с упорядочиванием (список без спецсимволов)
}
