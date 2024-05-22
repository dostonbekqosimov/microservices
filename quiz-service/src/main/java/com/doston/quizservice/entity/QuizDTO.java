package com.doston.quizservice.entity;


import lombok.Data;

@Data
public class QuizDTO {
    String categoryName;
    Integer numOfQuestions;
    String title;
}
