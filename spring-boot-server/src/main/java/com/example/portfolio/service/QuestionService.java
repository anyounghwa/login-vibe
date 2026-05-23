package com.example.portfolio.service;

import com.example.portfolio.domain.Member;
import com.example.portfolio.domain.Question;
import com.example.portfolio.domain.Role;
import com.example.portfolio.dto.QuestionFormDto;
import com.example.portfolio.repository.QuestionRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class QuestionService {

    private final QuestionRepository questionRepository;

    public QuestionService(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    @Transactional(readOnly = true)
    public List<Question> findAll() {
        return questionRepository.findAllByOrderByIdDesc();
    }

    @Transactional(readOnly = true)
    public Question findById(Long id) {
        return getQuestion(id);
    }

    @Transactional
    public Long create(QuestionFormDto questionFormDto, Member writer) {
        Question question = new Question(
                questionFormDto.getTitle(),
                questionFormDto.getContent(),
                writer
        );
        return questionRepository.save(question).getId();
    }

    @Transactional
    public void update(Long id, QuestionFormDto questionFormDto, Member currentMember) {
        Question question = getQuestion(id);
        validateEditable(question, currentMember);
        question.update(questionFormDto.getTitle(), questionFormDto.getContent());
    }

    @Transactional
    public void delete(Long id, Member currentMember) {
        Question question = getQuestion(id);
        validateEditable(question, currentMember);
        questionRepository.delete(question);
    }

    public boolean canEdit(Question question, Member currentMember) {
        if (currentMember == null) {
            return false;
        }
        if (question.hasWriter()) {
            return question.isWrittenBy(currentMember);
        }
        return currentMember.getRole() == Role.ADMIN;
    }

    private Question getQuestion(Long id) {
        return questionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));
    }

    private void validateEditable(Question question, Member currentMember) {
        if (!canEdit(question, currentMember)) {
            throw new IllegalArgumentException("게시글을 수정하거나 삭제할 권한이 없습니다.");
        }
    }
}

