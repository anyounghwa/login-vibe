from pathlib import Path

import joblib
import pandas as pd
from flask import Flask, jsonify, request
from flask_cors import CORS


BASE_DIR = Path(__file__).resolve().parent
MODEL_PATH = BASE_DIR / "models" / "obesity_model.pkl"

app = Flask(__name__)
CORS(app)


def load_model():
    if not MODEL_PATH.exists():
        raise FileNotFoundError(
            f"Model file not found. Run train_model.py first: {MODEL_PATH}"
        )
    return joblib.load(MODEL_PATH)


model = load_model()


def validate_number(data, key):
    if key not in data:
        raise ValueError(f"{key} is required.")

    try:
        value = float(data[key])
    except (TypeError, ValueError) as exc:
        raise ValueError(f"{key} must be a number.") from exc

    if value <= 0:
        raise ValueError(f"{key} must be greater than 0.")

    return value


@app.get("/health")
def health():
    return jsonify({"status": "ok"})


@app.post("/predict")
def predict():
    try:
        data = request.get_json(silent=True)
        if data is None:
            raise ValueError("JSON request body is required.")

        height = validate_number(data, "height")
        weight = validate_number(data, "weight")
        bmi = weight / ((height / 100) ** 2)

        input_data = pd.DataFrame(
            [
                {
                    "height": height,
                    "weight": weight,
                    "bmi": bmi,
                }
            ]
        )

        prediction = model.predict(input_data)[0]
        result = "\ube44\ub9cc" if int(prediction) == 1 else "\uc815\uc0c1"

        return jsonify({"result": result})
    except ValueError as exc:
        return jsonify({"error": str(exc)}), 400
    except Exception as exc:
        return jsonify({"error": f"Prediction failed: {exc}"}), 500


if __name__ == "__main__":
    app.run(host="localhost", port=5000, debug=True)

