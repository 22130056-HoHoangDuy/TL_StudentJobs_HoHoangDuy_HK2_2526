from flask import Flask
from flask import request
from flask import jsonify

import requests
import tempfile

import cv2
import easyocr

from schedule_orc.column_detector import detect_columns
from schedule_orc.anchor_detector import detect_anchors
from schedule_orc.subject_extractor import extract_subjects
from schedule_orc.busy_slot_builder import build_busy_slots

# ==========================================
# FLASK
# ==========================================

app = Flask(__name__)

# ==========================================
# EASY OCR
# ==========================================

reader = easyocr.Reader(

    ['en'],

    gpu=False
)

# ==========================================
# OCR ENDPOINT
# ==========================================

@app.route("/ocr", methods=["POST"])

def process_ocr():

    try:

        print("\n========================")
        print("START OCR REQUEST")
        print("========================")

        image_url = request.json.get(
            "imagePath"
        )

        print("IMAGE URL:")
        print(image_url)

        # ======================================
        # DOWNLOAD IMAGE
        # ======================================

        print("\nDOWNLOADING IMAGE...")

        response = requests.get(image_url)

        print("STATUS CODE:")
        print(response.status_code)

        print("CONTENT TYPE:")
        print(

            response.headers.get(
                "content-type"
            )
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

        # ======================================
        # OCR
        # ======================================

        print("\nRUNNING EASY OCR...")

        ocr_result = reader.readtext(

            temp_file.name
        )

        print("RAW OCR RESULT:")
        print(ocr_result)

        # ======================================
        # CONVERT FORMAT
        # ======================================

        lines = []

        for item in ocr_result:

            box = item[0]

            text = item[1]

            confidence = item[2]

            lines.append([

                box,

                (

                    text,
                    confidence
                )
            ])

        result = [lines]

        # ======================================
        # DETECT COLUMNS
        # ======================================

        print("\nDETECT COLUMNS...")

        columns = detect_columns(
            result
        )

        print("COLUMNS:")
        print(columns)

        # ======================================
        # DETECT ANCHORS
        # ======================================

        print("\nDETECT ANCHORS...")

        anchors = detect_anchors(
            result
        )

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

        print("SUBJECTS:")
        print(subjects)

        # ======================================
        # SAFE CHECK
        # ======================================

        if len(columns) == 0:

            return jsonify({

                "success": False,

                "error":
                    "No columns detected"
            })

        if len(anchors) == 0:

            return jsonify({

                "success": False,

                "error":
                    "No anchors detected"
            })

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

        # ======================================
        # DAYS
        # ======================================

        days = list(set([

            item["dayOfWeek"]

            for item in busy_slots
        ]))

        print("\nDAYS WITH SCHEDULE:")
        print(days)

        print("\nOCR SUCCESS")

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

        debug=True
    )
