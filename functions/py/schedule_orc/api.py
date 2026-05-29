from flask import Flask
from flask import request
from flask import jsonify

import requests
import tempfile

import cv2
import paddle

from paddleocr import PaddleOCR

from column_detector import detect_columns
from anchor_detector import detect_anchors
from subject_extractor import extract_subjects
from busy_slot_builder import build_busy_slots

# ==========================================
# FLASK
# ==========================================

app = Flask(__name__)

# ==========================================
# EASY OCR
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

    print("\n========================")
    print("START OCR REQUEST")
    print(request.json)
    print("========================\n")

    try:

        image_url = request.json.get("imagePath")

        print("IMAGE URL:")
        print(image_url)

        # ======================================
        # DOWNLOAD IMAGE
        # ======================================

        print("\nDOWNLOADING IMAGE...")

        response = requests.get(
            image_url,
            timeout=30
        )

        print("STATUS CODE:")
        print(response.status_code)

        print("CONTENT TYPE:")
        print(
            response.headers.get(
                "content-type"
            )
        )

        print("CONTENT LENGTH:")
        print(
            len(response.content)
        )

        temp_file = tempfile.NamedTemporaryFile(
            delete=False,
            suffix=".jpg"
        )

        temp_file.write(
            response.content
        )

        temp_file.close()

        print("TEMP FILE:")
        print(temp_file.name)

        # TEST 1
        print("\nDOWNLOAD SUCCESS")

        # ======================================
        # OCR
        # ======================================

        print("\nRUNNING PADDLE OCR...")
        result = ocr.ocr(
            temp_file.name
        )

        print("\nOCR FINISHED")

        print("OCR RESULT TYPE:")
        print(type(result))
        print("AFTER OCR")
        print("\nCOLUMN COUNT:")

        # ======================================
        # DETECT COLUMNS
        # ======================================

        print("\nDETECT COLUMNS...")

        columns = detect_columns(
            result
        )
        print("\nCOLUMN COUNT:")
        print(len(columns))
        print("AFTER COLUMNS")

        print("COLUMNS:")
        print(columns)

        # ======================================
        # DETECT ANCHORS
        # ======================================

        print("\nDETECT ANCHORS...")

        anchors = detect_anchors(
            result
        )
        print("\nANCHOR COUNT:")
        print(len(anchors))

        print("ANCHORS:")
        print(anchors)

        # ======================================
        # EXTRACT SUBJECTS
        # ======================================

        print("\nEXTRACT SUBJECTS...")

        subjects = extract_subjects(

            result,
            anchors
        )
        print("\nSUBJECT COUNT:")
        print(len(subjects))

        print("SUBJECTS:")
        print(subjects)

        # ======================================
        # BUILD BUSY SLOTS
        # ======================================

        print("\nBUILD BUSY SLOTS...")

        busy_slots = build_busy_slots(

            anchors,
            subjects,
            columns
        )

        print("BUSY SLOTS:")
        print(busy_slots)

        days = list(set([

            item["dayOfWeek"]

            for item in busy_slots
        ]))

        print("DAYS:")
        print(days)

        return jsonify({

            "success": True,

            "busySlots":
                busy_slots,

            "daysWithSchedule":
                days
        })

    except Exception as e:

        print("\nOCR ERROR:")
        print(str(e))

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

        debug=False
    )
