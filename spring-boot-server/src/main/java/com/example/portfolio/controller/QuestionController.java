package com.example.portfolio.controller;

import com.example.portfolio.domain.Member;
import com.example.portfolio.domain.Question;
import com.example.portfolio.dto.QuestionFormDto;
import com.example.portfolio.service.MemberService;
import com.example.portfolio.service.QuestionService;
import jakarta.validation.Valid;
import java.security.Principal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/questions")
public class QuestionController {

    private final QuestionService questionService;
    private final MemberService memberService;

    public QuestionController(QuestionService questionService, MemberService memberService) {
        this.questionService = questionService;
        this.memberService = memberService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("questions", questionService.findAll());
        return "question/list";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Principal principal, Model model) {
        Question question = questionService.findById(id);
        Member currentMember = getCurrentMember(principal);

        model.addAttribute("question", question);
        model.addAttribute("canEdit", questionService.canEdit(question, currentMember));
        return "question/detail";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("questionFormDto", new QuestionFormDto());
        model.addAttribute("formTitle", "게시글 등록");
        model.addAttribute("actionUrl", "/questions/new");
        return "question/form";
    }

    @PostMapping("/new")
    public String create(
            @Valid @ModelAttribute QuestionFormDto questionFormDto,
            BindingResult bindingResult,
            Principal principal,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("formTitle", "게시글 등록");
            model.addAttribute("actionUrl", "/questions/new");
            return "question/form";
        }

        Member writer = memberService.findByLoginId(principal.getName());
        Long questionId = questionService.create(questionFormDto, writer);
        return "redirect:/questions/" + questionId;
    }

    @GetMapping("/{id}/edit")
    public String updateForm(@PathVariable Long id, Principal principal, Model model) {
        Question question = questionService.findById(id);
        Member currentMember = memberService.findByLoginId(principal.getName());

        if (!questionService.canEdit(question, currentMember)) {
            throw new IllegalArgumentException("게시글을 수정할 권한이 없습니다.");
        }

        QuestionFormDto questionFormDto = new QuestionFormDto();
        questionFormDto.setTitle(question.getTitle());
        questionFormDto.setContent(question.getContent());

        model.addAttribute("questionFormDto", questionFormDto);
        model.addAttribute("formTitle", "게시글 수정");
        model.addAttribute("actionUrl", "/questions/" + id + "/edit");
        return "question/form";
    }

    @PostMapping("/{id}/edit")
    public String update(
            @PathVariable Long id,
            @Valid @ModelAttribute QuestionFormDto questionFormDto,
            BindingResult bindingResult,
            Principal principal,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("formTitle", "게시글 수정");
            model.addAttribute("actionUrl", "/questions/" + id + "/edit");
            return "question/form";
        }

        Member currentMember = memberService.findByLoginId(principal.getName());
        questionService.update(id, questionFormDto, currentMember);
        return "redirect:/questions/" + id;
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, Principal principal) {
        Member currentMember = memberService.findByLoginId(principal.getName());
        questionService.delete(id, currentMember);
        return "redirect:/questions";
    }

    private Member getCurrentMember(Principal principal) {
        if (principal == null) {
            return null;
        }
        return memberService.findByLoginId(principal.getName());
    }
}

