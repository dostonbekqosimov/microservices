package com.doston.questionservice.dao;



import com.doston.questionservice.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Integer> {
    List<Question> findByCategory(String category);

    void deleteByCategory(String category);

    boolean existsByCategory(String category);

    @Query(value = "SELECT q.id FROM question q WHERE q.category = :category ORDER BY RANDOM() LIMIT :numOfQuestions", nativeQuery = true)
    List<Integer> findRandomQuestionByCategory(String category, int numOfQuestions);
}
