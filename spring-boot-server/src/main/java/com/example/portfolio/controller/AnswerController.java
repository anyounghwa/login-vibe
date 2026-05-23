package com.example.portfolio.controller;

import com.example.portfolio.domain.Member;
import com.example.portfolio.service.AnswerService;
import com.example.portfolio.service.MemberService;
import java.security.Principal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/answer")
public class AnswerController {

    private final AnswerService answerService;
    private final MemberService memberService;

    public AnswerController(AnswerService answerService, MemberService memberService) {
        this.answerService = answerService;
        this.memberService = memberService;
    }

    @PostMapping("/create/{questionId}")
    public String create(
            @PathVariable Long questionId,
            @RequestParam String content,
            Principal principal,
            RedirectAttributes redirectAttributes
    ) {
        Member author = memberService.findByLoginId(principal.getName());

        try {
            answerService.create(questionId, content, author);
        } catch (IllegalArgumentException exception) {
            redirectAttributes.addFlashAttribute("answerErrorMessage", exception.getMessage());
        }

        return "redirect:/questions/" + questionId;
    }
}

