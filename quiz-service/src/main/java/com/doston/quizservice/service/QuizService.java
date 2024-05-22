package com.doston.quizservice.service;

import com.doston.quizservice.dao.QuizRepository;
import com.doston.quizservice.entity.QuestionWrapper;
import com.doston.quizservice.entity.Quiz;
import com.doston.quizservice.entity.Response;
import com.doston.quizservice.feign.QuizInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class QuizService {

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private QuizInterface quizInterface;



    public ResponseEntity<String> createQuiz(String category, int numOfQuestions, String title) {

        List<Integer> questions = quizInterface.getQuestionsForQuiz(category,numOfQuestions).getBody();


        Quiz quiz = new Quiz();
        quiz.setTitle(title);
        quiz.setQuestionIds(questions);
        quizRepository.save(quiz);
        return new ResponseEntity<>("Success", HttpStatus.CREATED);
    }

    public ResponseEntity<List<QuestionWrapper>> getQuiz(Integer id) {
        Optional<Quiz> quiz = quizRepository.findById(id);


        List<Integer> questionIds = quiz.get().getQuestionIds();
        ResponseEntity<List<QuestionWrapper>> questions = quizInterface.getQuestionsFromId(questionIds);
        return questions;

    }

    // Bu faqat quiz with id 2 ni scoreni beryabdi.
    public ResponseEntity<Integer> calculateResult(Integer id, List<Response> responses) {
        Optional<Quiz> quiz = quizRepository.findById(id);

        if (quiz.isPresent()) {
            // Call the question-service to get the score using Feign client
            ResponseEntity<Integer> score = quizInterface.getScore(responses);
            return score;
        } else {
            // Handle the case where the quiz is not found
            return ResponseEntity.notFound().build();
        }
    }

    public void calculateResultsForMultipleQuizzes(List<Integer> quizIds, List<Response> responses) {
        for (Integer id : quizIds) {
            ResponseEntity<Integer> score = calculateResult(id, responses);
            System.out.println("Quiz ID: " + id + ", Score: " + score.getBody());
        }
    }
}
