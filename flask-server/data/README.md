# 건강검진 데이터 폴더

이 폴더에는 모델 학습에 사용할 건강검진 CSV 파일을 둔다.

기본 파일명:

```text
health_check.csv
```

최소 필요 컬럼:

```text
height, weight
```

예시:

```csv
height,weight
170,75
160,52
180,95
```

선택적으로 `obesity` 라벨 컬럼을 포함할 수 있다.

```csv
height,weight,obesity
170,75,0
160,52,0
180,95,1
```

`obesity` 컬럼이 없으면 `train_model.py`가 BMI 25 이상을 비만으로 판단하여 라벨을 생성한다.

