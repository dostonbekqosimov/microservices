package com.doston.quizservice.dao;


import com.doston.quizservice.entity.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizRepository extends JpaRepository<Quiz, Integer> {
}
