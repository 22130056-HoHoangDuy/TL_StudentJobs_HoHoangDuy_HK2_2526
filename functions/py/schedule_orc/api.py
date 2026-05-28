from flask import Flask
from flask import request
from flask import jsonify

import cv2
import paddle

from paddleocr import PaddleOCR

from schedule_orc.column_detector import detect_columns
from schedule_orc.anchor_detector import detect_anchors
from schedule_orc.subject_extractor import extract_subjects
from schedule_orc.busy_slot_builder import build_busy_slots

# ==========================================
# FLASK
# ==========================================

app = Flask(__name__)

# ==========================================
# PADDLE
# ==========================================

paddle.set_flags({

    'FLAGS_enable_pir_api': 0
})

ocr = PaddleOCR(

    use_textline_orientation=True,

    lang='en',

    enable_mkldnn=False
)

# ==========================================
# OCR ENDPOINT
# ==========================================

@app.route("/ocr", methods=["POST"])

def process_ocr():

    try:

        image_path = request.json.get(
            "imagePath"
        )

        result = ocr.ocr(image_path)

        columns = detect_columns(
            result
        )

        anchors = detect_anchors(
            result
        )

        subjects = extract_subjects(

            result,
            anchors
        )

        busy_slots = build_busy_slots(

            anchors,
            subjects,
            columns
        )

        days = list(set([

            item["dayOfWeek"]

            for item in busy_slots
        ]))

        return jsonify({

            "success": True,

            "busySlots":
                busy_slots,

            "daysWithSchedule":
                days
        })

    except Exception as e:

        return jsonify({

            "success": False,

            "error": str(e)
        })


# ==========================================
# RUN
# ==========================================

if __name__ == "__main__":

    app.run(

        host="0.0.0.0",

        port=5000,

        debug=True
    )