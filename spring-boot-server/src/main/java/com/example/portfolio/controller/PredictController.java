package com.example.portfolio.controller;

import com.example.portfolio.dto.PredictRequestDto;
import com.example.portfolio.dto.PredictResponseDto;
import com.example.portfolio.service.PredictService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/predict")
public class PredictController {

    private final PredictService predictService;

    public PredictController(PredictService predictService) {
        this.predictService = predictService;
    }

    @GetMapping
    public String predictForm(Model model) {
        model.addAttribute("predictRequestDto", new PredictRequestDto());
        return "predict/form";
    }

    @PostMapping
    public String predict(
            @Valid @ModelAttribute PredictRequestDto predictRequestDto,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            return "predict/form";
        }

        try {
            PredictResponseDto responseDto = predictService.predict(predictRequestDto);
            model.addAttribute("request", predictRequestDto);
            model.addAttribute("response", responseDto);
            return "predict/result";
        } catch (IllegalStateException exception) {
            model.addAttribute("errorMessage", exception.getMessage());
            return "predict/form";
        }
    }
}

