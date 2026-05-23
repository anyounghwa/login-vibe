# 백엔드 기본 기능 구현 계획

## 기술 스택

- Spring Boot
- Spring Security
- Spring Data JPA
- MySQL
- Thymeleaf
- Bootstrap
- Gradle

## 패키지 구조

```text
com.example.portfolio
 ┣ config
 ┣ controller
 ┣ domain
 ┣ dto
 ┣ repository
 ┗ service
```

## 1단계 - 회원 기능 기본 구조

회원가입에 필요한 Entity, Repository, DTO, Service, Controller를 먼저 작성한다.

만들 파일:

```text
spring-boot-server/build.gradle
spring-boot-server/settings.gradle
spring-boot-server/src/main/java/com/example/portfolio/PortfolioApplication.java
spring-boot-server/src/main/java/com/example/portfolio/domain/Member.java
spring-boot-server/src/main/java/com/example/portfolio/domain/Role.java
spring-boot-server/src/main/java/com/example/portfolio/repository/MemberRepository.java
spring-boot-server/src/main/java/com/example/portfolio/dto/MemberFormDto.java
spring-boot-server/src/main/java/com/example/portfolio/service/MemberService.java
spring-boot-server/src/main/java/com/example/portfolio/controller/MemberController.java
spring-boot-server/src/main/java/com/example/portfolio/config/PasswordEncoderConfig.java
spring-boot-server/src/main/resources/application.yml
spring-boot-server/src/main/resources/templates/member/signup.html
```

구현 내용:

- 아이디, 비밀번호, 이름으로 회원가입
- 아이디 중복 검사
- BCryptPasswordEncoder로 비밀번호 암호화
- 기본 권한은 `ROLE_USER`

URL:

```text
GET /members/new
POST /members/new
```

## 2단계 - 로그인 및 Spring Security 인증

Spring Security formLogin 방식으로 로그인/로그아웃을 구현한다.

만들 파일:

```text
spring-boot-server/src/main/java/com/example/portfolio/config/SecurityConfig.java
spring-boot-server/src/main/java/com/example/portfolio/service/CustomUserDetailsService.java
spring-boot-server/src/main/resources/templates/member/login.html
```

구현 내용:

- 로그인 폼 페이지
- 로그인 성공 시 `/` 이동
- 로그아웃 처리
- 인증/인가 URL 규칙 적용

URL:

```text
GET /members/login
POST /members/login
POST /logout
```

권한 규칙:

```text
/                  permitAll
/prediction/**     permitAll
/members/new       permitAll
/members/login     permitAll
/boards/**         authenticated
/admin/members     hasRole("ADMIN")
```

## 3단계 - 메인 화면과 AI 예측 접근 경로

비회원도 접근 가능한 메인 화면과 AI 예측 화면 경로를 준비한다.

만들 파일:

```text
spring-boot-server/src/main/java/com/example/portfolio/controller/HomeController.java
spring-boot-server/src/main/java/com/example/portfolio/controller/PredictionPageController.java
spring-boot-server/src/main/resources/templates/index.html
spring-boot-server/src/main/resources/templates/prediction/form.html
```

URL:

```text
GET /
GET /prediction
```

## 4단계 - 게시판 CRUD

로그인한 사용자만 게시판을 사용할 수 있도록 게시글 CRUD를 구현한다.

만들 파일:

```text
spring-boot-server/src/main/java/com/example/portfolio/domain/Board.java
spring-boot-server/src/main/java/com/example/portfolio/repository/BoardRepository.java
spring-boot-server/src/main/java/com/example/portfolio/dto/BoardFormDto.java
spring-boot-server/src/main/java/com/example/portfolio/dto/BoardResponseDto.java
spring-boot-server/src/main/java/com/example/portfolio/service/BoardService.java
spring-boot-server/src/main/java/com/example/portfolio/controller/BoardController.java
spring-boot-server/src/main/resources/templates/board/list.html
spring-boot-server/src/main/resources/templates/board/detail.html
spring-boot-server/src/main/resources/templates/board/form.html
```

URL:

```text
GET /boards
GET /boards/{id}
GET /boards/new
POST /boards/new
GET /boards/{id}/edit
POST /boards/{id}/edit
POST /boards/{id}/delete
```

권한:

- 로그인한 사용자만 게시글 접근 가능
- 작성자 본인만 수정/삭제 가능

## 5단계 - 답글 기능

게시글 상세 화면에서 회원이 답글을 등록, 수정, 삭제할 수 있도록 구현한다.

만들 파일:

```text
spring-boot-server/src/main/java/com/example/portfolio/domain/Answer.java
spring-boot-server/src/main/java/com/example/portfolio/repository/AnswerRepository.java
spring-boot-server/src/main/java/com/example/portfolio/dto/AnswerFormDto.java
spring-boot-server/src/main/java/com/example/portfolio/service/AnswerService.java
spring-boot-server/src/main/java/com/example/portfolio/controller/AnswerController.java
```

URL:

```text
POST /boards/{boardId}/answers
POST /answers/{id}/edit
POST /answers/{id}/delete
```

권한:

- 로그인한 사용자만 답글 작성 가능
- 답글 작성자 본인만 수정/삭제 가능

## 6단계 - 관리자 회원 목록

관리자만 회원 목록을 조회할 수 있도록 구현한다.

만들 파일:

```text
spring-boot-server/src/main/java/com/example/portfolio/controller/AdminMemberController.java
spring-boot-server/src/main/resources/templates/admin/member-list.html
```

URL:

```text
GET /admin/members
```

권한:

```text
ROLE_ADMIN
```

## 7단계 - 예외 처리와 검증

공통 예외 처리와 입력값 검증 메시지를 정리한다.

만들 파일:

```text
spring-boot-server/src/main/java/com/example/portfolio/config/GlobalExceptionHandler.java
spring-boot-server/src/main/resources/templates/error/403.html
spring-boot-server/src/main/resources/templates/error/404.html
spring-boot-server/src/main/resources/templates/error/500.html
```

처리 내용:

- 회원 아이디 중복
- 로그인 실패
- 권한 없는 접근
- 게시글/답글 작성자 불일치
- 필수 입력값 누락

