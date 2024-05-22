package com.doston.questionservice.controller;

import com.doston.questionservice.entity.Question;

import com.doston.questionservice.entity.QuestionWrapper;
import com.doston.questionservice.entity.Response;
import com.doston.questionservice.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("question")
public class QuestionController {

    @Autowired
    Environment environment;

    @Autowired
    private QuestionService questionService;

    @GetMapping("/allQuestions")
    public ResponseEntity<List<Question>> getAllQuestions() {
        return questionService.getAllQuestions();
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<Question>> getQuestionsByCategory(@PathVariable("category") String category) {
        return questionService.getQuestionsByCategory(category);
    }

    @PostMapping("/add")
    public void addQuestion(@RequestBody Question question) {
        questionService.addQuestion(question);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Question> updateQuestion(
            @PathVariable Integer id,
            @RequestBody Question question) {
        Question updatedQuestion = questionService.updateQuestionInDatabase(id, question);
        return ResponseEntity.ok(updatedQuestion);
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteQuestionById(@PathVariable Integer id) {
        questionService.deleteQuestionById(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/category/{category}")
    public ResponseEntity<Void> deleteQuestionsByCategory(@PathVariable String category) {
        questionService.deleteQuestionsByCategory(category);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("generate")
    public ResponseEntity<List<Integer>> getQuestionsForQuiz(@RequestParam String categoryName, @RequestParam Integer numQuestions) {
        return questionService.getQuestionsForQuiz(categoryName, numQuestions);
    }

    @PostMapping("getQuestions")
    public ResponseEntity<List<QuestionWrapper>> getQuestionsFromId(@RequestBody List<Integer> questionIds) {
        System.out.println(environment.getProperty("local.server.port"));
        return questionService.getQuestionsFromId(questionIds);
    }

    @PostMapping("getScore")
    public ResponseEntity<Integer> getScore(@RequestBody List<Response> responses){
        return questionService.getScore(responses);
    }

}
