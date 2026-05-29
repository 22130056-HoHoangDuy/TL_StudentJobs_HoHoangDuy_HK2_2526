import json
import cv2
import paddle

from paddleocr import PaddleOCR

from column_detector import detect_columns
from anchor_detector import detect_anchors
from subject_extractor import extract_subjects
from busy_slot_builder import build_busy_slots
from visualization import draw_result


# ==========================================
# FIX PADDLE
# ==========================================

paddle.set_flags({

    'FLAGS_enable_pir_api': 0
})

# ==========================================
# OCR
# ==========================================

ocr = PaddleOCR(

    use_textline_orientation=True,

    lang='en',

    enable_mkldnn=False
)

# ==========================================
# IMAGE
# ==========================================

IMAGE_PATH = "schedule_orc/sample_images/tkb_orcFlow.png"

image = cv2.imread(IMAGE_PATH)

# ==========================================
# OCR RESULT
# ==========================================

result = ocr.ocr(IMAGE_PATH)

#==
import pprint

print("\n===== RAW OCR RESULT =====\n")

pprint.pprint(result)

# ==========================================
# DETECT COLUMNS
# ==========================================

columns = detect_columns(result)

print("\n===== COLUMNS =====")

print(

    json.dumps(

        columns,

        ensure_ascii=False,

        indent=2
    )
)

# ==========================================
# DETECT ANCHORS
# ==========================================

anchors = detect_anchors(result)

print("\n===== ANCHORS =====")

print(

    json.dumps(

        anchors,

        ensure_ascii=False,

        indent=2
    )
)

# ==========================================
# EXTRACT SUBJECTS
# ==========================================

subjects = extract_subjects(

    result,
    anchors
)

print("\n===== SUBJECTS =====")

print(

    json.dumps(

        subjects,

        ensure_ascii=False,

        indent=2
    )
)

# ==========================================
# BUILD BUSY SLOTS
# ==========================================

busy_slots = build_busy_slots(

    anchors,
    subjects,
    columns
)

print("\n===== BUSY SLOTS =====")

print(

    json.dumps(

        busy_slots,

        ensure_ascii=False,

        indent=2
    )
)

# ==========================================
# VISUALIZATION
# ==========================================

draw_result(

    image=image,

    anchors=anchors,

    columns=columns
)