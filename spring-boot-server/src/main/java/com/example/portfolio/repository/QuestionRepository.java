package com.example.portfolio.repository;

import com.example.portfolio.domain.Question;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    @EntityGraph(attributePaths = "writer")
    List<Question> findAllByOrderByIdDesc();

    @Override
    @EntityGraph(attributePaths = {"writer", "answers", "answers.author"})
    Optional<Question> findById(Long id);
}
