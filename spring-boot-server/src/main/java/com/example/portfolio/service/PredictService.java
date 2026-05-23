package com.example.portfolio.service;

import com.example.portfolio.dto.PredictRequestDto;
import com.example.portfolio.dto.PredictResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class PredictService {

    private final RestTemplate restTemplate;
    private final String predictUrl;

    public PredictService(RestTemplate restTemplate, @Value("${flask.predict-url}") String predictUrl) {
        this.restTemplate = restTemplate;
        this.predictUrl = predictUrl;
    }

    public PredictResponseDto predict(PredictRequestDto requestDto) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<PredictRequestDto> requestEntity = new HttpEntity<>(requestDto, headers);

        try {
            PredictResponseDto response = restTemplate.postForObject(
                    predictUrl,
                    requestEntity,
                    PredictResponseDto.class
            );

            if (response == null || response.getResult() == null || response.getResult().isBlank()) {
                throw new IllegalStateException("Flask 서버 예측 결과가 비어 있습니다.");
            }

            return response;
        } catch (RestClientException exception) {
            throw new IllegalStateException("Flask 예측 서버와 통신할 수 없습니다.", exception);
        }
    }
}

