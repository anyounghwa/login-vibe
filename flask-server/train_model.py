from pathlib import Path

import joblib
import pandas as pd
from sklearn.ensemble import RandomForestClassifier
from sklearn.metrics import accuracy_score, classification_report
from sklearn.model_selection import train_test_split
from sklearn.pipeline import Pipeline
from sklearn.preprocessing import StandardScaler


BASE_DIR = Path(__file__).resolve().parent
DATA_PATH = BASE_DIR / "data" / "health_check.csv"
MODEL_DIR = BASE_DIR / "models"
MODEL_PATH = MODEL_DIR / "obesity_model.pkl"


def load_data() -> pd.DataFrame:
    if DATA_PATH.exists():
        return pd.read_csv(DATA_PATH)

    # 실제 건강검진 CSV가 준비되기 전까지 학습 흐름을 확인하기 위한 샘플 데이터다.
    sample_data = [
        {"height": 150, "weight": 45},
        {"height": 155, "weight": 52},
        {"height": 160, "weight": 58},
        {"height": 165, "weight": 61},
        {"height": 170, "weight": 68},
        {"height": 172, "weight": 76},
        {"height": 175, "weight": 82},
        {"height": 178, "weight": 88},
        {"height": 180, "weight": 94},
        {"height": 183, "weight": 102},
        {"height": 158, "weight": 72},
        {"height": 162, "weight": 80},
        {"height": 168, "weight": 90},
        {"height": 174, "weight": 96},
        {"height": 181, "weight": 110},
    ]
    return pd.DataFrame(sample_data)


def preprocess_data(df: pd.DataFrame) -> pd.DataFrame:
    required_columns = {"height", "weight"}
    missing_columns = required_columns - set(df.columns)
    if missing_columns:
        raise ValueError(f"필수 컬럼이 없습니다: {', '.join(sorted(missing_columns))}")

    processed = df.copy()
    processed["height"] = pd.to_numeric(processed["height"], errors="coerce")
    processed["weight"] = pd.to_numeric(processed["weight"], errors="coerce")
    processed = processed.dropna(subset=["height", "weight"])

    processed = processed[(processed["height"] > 0) & (processed["weight"] > 0)]
    processed["bmi"] = processed["weight"] / ((processed["height"] / 100) ** 2)

    if "obesity" not in processed.columns:
        processed["obesity"] = (processed["bmi"] >= 25).astype(int)
    else:
        processed["obesity"] = pd.to_numeric(processed["obesity"], errors="coerce")
        processed = processed.dropna(subset=["obesity"])
        processed["obesity"] = processed["obesity"].astype(int)

    if processed.empty:
        raise ValueError("학습 가능한 데이터가 없습니다.")

    return processed


def train_model(df: pd.DataFrame) -> Pipeline:
    x = df[["height", "weight", "bmi"]]
    y = df["obesity"]

    stratify = y if y.nunique() > 1 and len(y) >= 4 else None
    x_train, x_test, y_train, y_test = train_test_split(
        x,
        y,
        test_size=0.2,
        random_state=42,
        stratify=stratify,
    )

    model = Pipeline(
        steps=[
            ("scaler", StandardScaler()),
            ("classifier", RandomForestClassifier(n_estimators=100, random_state=42)),
        ]
    )
    model.fit(x_train, y_train)

    predictions = model.predict(x_test)
    accuracy = accuracy_score(y_test, predictions)
    print(f"accuracy: {accuracy:.4f}")
    print(classification_report(y_test, predictions, zero_division=0))

    return model


def save_model(model: Pipeline) -> None:
    MODEL_DIR.mkdir(parents=True, exist_ok=True)
    joblib.dump(model, MODEL_PATH)
    print(f"model saved: {MODEL_PATH}")


def main() -> None:
    raw_data = load_data()
    processed_data = preprocess_data(raw_data)
    model = train_model(processed_data)
    save_model(model)


if __name__ == "__main__":
    main()

