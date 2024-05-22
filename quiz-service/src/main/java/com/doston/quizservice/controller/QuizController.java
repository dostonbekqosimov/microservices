package com.doston.quizservice.controller;


import com.doston.quizservice.entity.QuestionWrapper;
import com.doston.quizservice.entity.QuizDTO;
import com.doston.quizservice.entity.Response;
import com.doston.quizservice.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("quiz")
public class QuizController {

    @Autowired
    private QuizService quizService;

    @PostMapping("create")
    public ResponseEntity<String> createQuiz(@RequestBody QuizDTO quizDTO) {
        quizService.createQuiz(quizDTO.getCategoryName(), quizDTO.getNumOfQuestions(), quizDTO.getTitle());
        return ResponseEntity.ok("Created");
    }

    @GetMapping("get/{id}")
    public ResponseEntity<List<QuestionWrapper>> getQuiz(@PathVariable Integer id) {
        return quizService.getQuiz(id);
    }

    @PostMapping("submit/{id}")
    public ResponseEntity<Integer> calculateResult(@PathVariable Integer id, @RequestBody List<Response> responses) {
        return quizService.calculateResult(id, responses);
    }
}
