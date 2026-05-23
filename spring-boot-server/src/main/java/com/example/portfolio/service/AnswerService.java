package com.example.portfolio.service;

import com.example.portfolio.domain.Answer;
import com.example.portfolio.domain.Member;
import com.example.portfolio.domain.Question;
import com.example.portfolio.repository.AnswerRepository;
import com.example.portfolio.repository.QuestionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AnswerService {

    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;

    public AnswerService(AnswerRepository answerRepository, QuestionRepository questionRepository) {
        this.answerRepository = answerRepository;
        this.questionRepository = questionRepository;
    }

    @Transactional
    public Long create(Long questionId, String content, Member author) {
        if (content == null || content.isBlank()) {
            throw new IllegalArgumentException("답글 내용을 입력해 주세요.");
        }

        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        Answer answer = new Answer(content.trim(), author, question);
        return answerRepository.save(answer).getId();
    }
}

