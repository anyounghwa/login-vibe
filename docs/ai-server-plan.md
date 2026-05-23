# AI 서버 구현 계획

## 목표

Spring Boot 서버와 분리된 Flask AI 서버를 구축하여 건강검진 데이터 기반 비만 여부를 예측한다.

Spring Boot는 사용자 요청과 화면 처리를 담당하고, Flask 서버는 머신러닝 모델 예측을 담당한다.

## 추천 폴더 구조

현재 프로젝트에서는 기존 `flask-server` 폴더를 AI 서버 루트로 사용한다.

```text
flask-server/
 ┣ app.py
 ┣ train_model.py
 ┣ requirements.txt
 ┣ data/
 ┃ ┣ README.md
 ┃ ┗ health_check.csv
 ┗ models/
   ┗ obesity_model.pkl
```

## 파일 역할

### train_model.py

- 건강검진 데이터 로드
- 키와 몸무게 컬럼 전처리
- BMI 계산
- 비만 여부 라벨 생성 또는 기존 라벨 사용
- 학습/테스트 데이터 분리
- Scikit-learn 모델 학습
- Joblib으로 모델 저장

### app.py

- Flask 서버 실행
- `/predict` POST API 제공
- JSON 요청 데이터 검증
- 저장된 모델 로드
- 예측 결과 JSON 반환
- Spring Boot에서 REST API로 호출 가능하도록 CORS 허용

### requirements.txt

- Flask
- flask-cors
- pandas
- scikit-learn
- joblib

### data/health_check.csv

건강검진 원본 또는 전처리용 CSV 파일이다.

최소 필요 컬럼:

```text
height
weight
```

선택 컬럼:

```text
obesity
```

`obesity` 컬럼이 없으면 BMI 기준으로 비만 여부 라벨을 생성한다.

## 개발 순서

## 1단계 - 데이터 전처리 및 모델 학습

만들 파일:

```text
flask-server/train_model.py
flask-server/requirements.txt
flask-server/data/README.md
```

구현 내용:

- CSV 데이터 로드
- 키와 몸무게 숫자형 변환
- 결측치 제거
- BMI 계산
- 비만 여부 라벨 생성
- 학습/테스트 데이터 분리
- RandomForestClassifier 모델 학습
- 모델 평가 정확도 출력
- `models/obesity_model.pkl` 저장

## 2단계 - Flask 서버 구현

만들 파일:

```text
flask-server/app.py
```

구현 내용:

- Flask 앱 생성
- CORS 적용
- `/health` 상태 확인 API
- `/predict` POST API
- JSON 요청/응답 처리

## 3단계 - Spring Boot 연동 API 형태 확정

요청 예시:

```json
{
  "height": 170,
  "weight": 75
}
```

응답 예시:

```json
{
  "obesity": true,
  "result": "비만",
  "bmi": 25.95,
  "message": "BMI 기준 비만으로 예측되었습니다."
}
```

