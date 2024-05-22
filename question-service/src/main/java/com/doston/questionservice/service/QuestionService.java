package com.doston.questionservice.service;


import com.doston.questionservice.entity.Question;
import com.doston.questionservice.dao.QuestionRepository;
import com.doston.questionservice.entity.QuestionWrapper;
import com.doston.questionservice.entity.Response;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    public ResponseEntity<List<Question>> getAllQuestions() {


        try {
            return new ResponseEntity<>(questionRepository.findAll(), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);

    }

    public ResponseEntity<List<Question>> getQuestionsByCategory(String category) {

        if (questionRepository.findByCategory(category).isEmpty()) {
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.NOT_FOUND);
        }
        try {
            return new ResponseEntity<>(questionRepository.findByCategory(category), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.NOT_FOUND);


    }

    public ResponseEntity<String> addQuestion(Question question) {

        questionRepository.save(question);
        return new ResponseEntity<>("Added", HttpStatus.CREATED);


    }


    public Question updateQuestionInDatabase(Integer id, Question newQuestionData) {
        try {
            Optional<Question> optionalQuestion = questionRepository.findById(id);

            if (optionalQuestion.isPresent()) {
                Question existingQuestion = optionalQuestion.get();
                updateQuestion(existingQuestion, newQuestionData);
                return questionRepository.save(existingQuestion);
            } else {
                throw new EntityNotFoundException("Question with ID " + id + " not found");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to update the question", e);
        }
    }

    private void updateQuestion(Question existingQuestion, Question newQuestionData) {


        if (newQuestionData.getQuestionTitle() != null) {
            existingQuestion.setQuestionTitle(newQuestionData.getQuestionTitle());
        }
        if (newQuestionData.getOption1() != null) {
            existingQuestion.setOption1(newQuestionData.getOption1());
        }
        if (newQuestionData.getOption2() != null) {
            existingQuestion.setOption2(newQuestionData.getOption2());
        }
        if (newQuestionData.getOption3() != null) {
            existingQuestion.setOption3(newQuestionData.getOption3());
        }
        if (newQuestionData.getOption4() != null) {
            existingQuestion.setOption4(newQuestionData.getOption4());
        }
        if (newQuestionData.getCorrectOne() != null) {
            existingQuestion.setCorrectOne(newQuestionData.getCorrectOne());
        }
        if (newQuestionData.getDifficultLevel() != null) {
            existingQuestion.setDifficultLevel(newQuestionData.getDifficultLevel());
        }
        if (newQuestionData.getCategory() != null) {
            existingQuestion.setCategory(newQuestionData.getCategory());
        }
    }

    public void deleteQuestionById(Integer id) {
        try {
            if (questionRepository.existsById(id)) {
                questionRepository.deleteById(id);
            } else {
                throw new EntityNotFoundException("Question with ID " + id + " not found");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to delete the question", e);
        }
    }

    public void deleteQuestionsByCategory(String category) {
        try {
            if (!questionRepository.existsByCategory(category)) {
                throw new EntityNotFoundException("No questions found for category " + category);
            }
            questionRepository.deleteByCategory(category);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to delete questions by category", e);
        }
    }

    public ResponseEntity<List<Integer>> getQuestionsForQuiz(String categoryName, Integer numQuestions) {
        List<Integer> questions = questionRepository.findRandomQuestionByCategory(categoryName, numQuestions);
        return new ResponseEntity<>(questions, HttpStatus.OK);
    }

    public ResponseEntity<List<QuestionWrapper>> getQuestionsFromId(List<Integer> questionIds) {
        List<QuestionWrapper> wrappers = new ArrayList<>();
        List<Question> questions = new ArrayList<>();

        for (Integer id : questionIds) {
            questions.add(questionRepository.findById(id).get());
        }

        for (Question question : questions) {
            QuestionWrapper wrapper = new QuestionWrapper();
            wrapper.setId(question.getId());
            wrapper.setQuestionTitle(question.getQuestionTitle());
            wrapper.setOption1(question.getOption1());
            wrapper.setOption2(question.getOption2());
            wrapper.setOption3(question.getOption3());
            wrapper.setOption4(question.getOption4());
            wrappers.add(wrapper);
        }
        return new ResponseEntity<>(wrappers, HttpStatus.OK);
    }

    public ResponseEntity<Integer> getScore(List<Response> responses) {

        int correct = 0;


        for (Response response : responses) {
            Question question = questionRepository.findById(response.getId()).get();

            //noinspection OptionalGetWithoutIsPresent
            if (response.getResponse().equals(question.getCorrectOne())) {
                correct++;
            }

        }

        return new ResponseEntity<>(correct, HttpStatus.OK);
    }
}
