# Spring Boot - Flask 서버 연동 구현 계획

## 목표

사용자가 Spring Boot 화면에서 키와 몸무게를 입력하면 Spring Boot 서버가 Flask AI 서버의 `/predict` API를 호출하고, 반환된 예측 결과를 Thymeleaf 화면에 출력한다.

이번 단계에서는 예측 결과를 DB에 저장하지 않는다.

## Flask API 정보

```text
URL: http://localhost:5000/predict
Method: POST
Content-Type: application/json
```

요청:

```json
{
  "height": 170,
  "weight": 70
}
```

응답:

```json
{
  "result": "정상"
}
```

## 파일 단위 구현 계획

## 1. DTO

만들 파일:

```text
spring-boot-server/src/main/java/com/example/portfolio/dto/PredictRequestDto.java
spring-boot-server/src/main/java/com/example/portfolio/dto/PredictResponseDto.java
```

역할:

- `PredictRequestDto`: 화면 입력값과 Flask 요청 JSON 관리
- `PredictResponseDto`: Flask 응답 JSON 관리

## 2. RestTemplate 설정

만들 파일:

```text
spring-boot-server/src/main/java/com/example/portfolio/config/RestTemplateConfig.java
```

역할:

- Spring Service에서 주입받아 사용할 `RestTemplate` Bean 등록

## 3. Service

만들 파일:

```text
spring-boot-server/src/main/java/com/example/portfolio/service/PredictService.java
```

역할:

- Flask `/predict` API로 POST 요청
- JSON 응답을 `PredictResponseDto`로 변환
- Flask 서버 오류 시 사용자에게 보여줄 예외 메시지 처리

## 4. Controller

만들 파일:

```text
spring-boot-server/src/main/java/com/example/portfolio/controller/PredictController.java
```

역할:

- `GET /predict`: 예측 입력 화면 출력
- `POST /predict`: 입력값 검증 후 Flask 예측 요청
- 결과 화면에 예측 결과 전달

## 5. Thymeleaf 화면

만들 파일:

```text
spring-boot-server/src/main/resources/templates/predict/form.html
spring-boot-server/src/main/resources/templates/predict/result.html
```

역할:

- 키와 몸무게 입력
- 예측 결과 출력

## 6. SecurityConfig

수정 파일:

```text
spring-boot-server/src/main/java/com/example/portfolio/config/SecurityConfig.java
```

역할:

- `/predict`, `/predict/**`를 비회원도 접근 가능하게 허용

현재 이미 허용되어 있으므로 규칙을 확인하고 유지한다.

## 7. application.yml

수정 파일:

```text
spring-boot-server/src/main/resources/application.yml
```

역할:

- Flask 서버 URL을 설정값으로 분리

