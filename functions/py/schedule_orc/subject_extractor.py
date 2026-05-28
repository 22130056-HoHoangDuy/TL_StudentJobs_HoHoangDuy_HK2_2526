from schedule_orc.subject_extractor import re
from schedule_orc.subject_extractor import numpy as np


# ==========================================
# NORMALIZE SUBJECT NAME
# ==========================================

def normalize_subject_name(name):

    corrections = {

        "Chuyn dê":
            "Chuyên đề",

        "Chuyn dè":
            "Chuyên đề",

        "Chuyên dè":
            "Chuyên đề",

        "Nhâp môn":
            "Nhập môn",

        "Nhp mn":
            "Nhập môn",

        "Lâp trinh":
            "Lập trình",

        "Thuong mai din tu":
            "Thương mại điện tử",

        "Do án chuyn ngành":
            "Đồ án chuyên ngành"
    }

    result = name

    for wrong, right in corrections.items():

        result = re.sub(

            re.escape(wrong),

            right,

            result,

            flags=re.IGNORECASE
        )

    # ======================================
    # REMOVE TRASH TEXT
    # ======================================

    trash_patterns = [

        r'K\.CNTT',

        r'GV:.*',

        r'Phòng:.*',

        r'Nhóm:.*'
    ]

    for pattern in trash_patterns:

        result = re.sub(

            pattern,

            '',

            result,

            flags=re.IGNORECASE
        ).strip()

    return result.strip()


# ==========================================
# EXTRACT SUBJECTS
# ==========================================

def extract_subjects(

        result,
        anchors
):

    subjects = []

    data = result[0]

    polys = data["dt_polys"]

    texts = data["rec_texts"]

    for anchor in anchors:

        anchor_x = anchor["centerX"]

        anchor_y = anchor["centerY"]

        candidates = []

        for i in range(len(texts)):

            text = texts[i]

            box = polys[i]

            coords = np.array(

                box,

                dtype=np.float32
            ).flatten()

            center_x = np.mean(
                coords[0::2]
            )

            center_y = np.mean(
                coords[1::2]
            )

            # ==================================
            # SAME COLUMN
            # ==================================

            same_column = (

                    abs(center_x - anchor_x)
                    < 160
            )

            # ==================================
            # ABOVE ANCHOR
            # ==================================

            upper_area = (

                    center_y < anchor_y
            )

            # ==================================
            # NEAR ANCHOR
            # ==================================

            near_anchor = (

                    abs(anchor_y - center_y)
                    < 250
            )

            if (

                    same_column
                    and upper_area
                    and near_anchor
            ):

                # ==================================
                # SUBJECT CANDIDATE
                # ==================================

                if "(" in text and "Thứ" not in text:

                    candidates.append({

                        "text": text,

                        "centerY":
                            center_y
                    })

        # ======================================
        # PICK TOPMOST
        # ======================================

        if candidates:

            candidates = sorted(

                candidates,

                key=lambda x: x["centerY"]
            )

            subject = candidates[0]["text"]

            subject = normalize_subject_name(
                subject
            )

            subjects.append(subject)

        else:

            subjects.append("N/A")

    return subjects