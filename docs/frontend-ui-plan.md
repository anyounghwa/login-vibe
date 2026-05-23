# 프론트 UI 개선 계획

## 목표

Spring Boot + Thymeleaf + Bootstrap 기반 화면을 포트폴리오용으로 정리한다.

## 수정 대상 파일

```text
spring-boot-server/build.gradle
spring-boot-server/src/main/resources/templates/layout/navbar.html
spring-boot-server/src/main/resources/templates/index.html
spring-boot-server/src/main/resources/templates/predict/form.html
spring-boot-server/src/main/resources/templates/predict/result.html
spring-boot-server/src/main/resources/templates/member/login.html
spring-boot-server/src/main/resources/templates/member/signup.html
spring-boot-server/src/main/resources/templates/question/list.html
spring-boot-server/src/main/resources/templates/question/detail.html
spring-boot-server/src/main/resources/templates/question/form.html
spring-boot-server/src/main/resources/static/css/custom.css
```

## 개선 순서

1. 공통 네비게이션 바 작성
2. 메인 페이지 히어로 영역과 주요 기능 카드 구성
3. AI 예측 입력/결과 화면 개선
4. 로그인/회원가입 화면 스타일 통일
5. 게시판 목록/상세/폼 화면 스타일 통일
6. 최소한의 custom CSS 작성

## 유지할 기능

- `/`, `/login`, `/signup`, `/predict`, `/questions` URL 유지
- `th:href`, `th:action`, `th:field`, `th:if`, CSRF 토큰 유지
- 로그인 전/후 메뉴 분기
- 게시글 수정/삭제 권한 표시 변수 `canEdit` 유지

