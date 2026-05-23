package com.example.portfolio.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;

public class PredictRequestDto {

    @DecimalMin(value = "50.0", message = "키는 50cm 이상 입력해 주세요.")
    @DecimalMax(value = "250.0", message = "키는 250cm 이하로 입력해 주세요.")
    private double height;

    @DecimalMin(value = "10.0", message = "몸무게는 10kg 이상 입력해 주세요.")
    @DecimalMax(value = "300.0", message = "몸무게는 300kg 이하로 입력해 주세요.")
    private double weight;

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }
}

