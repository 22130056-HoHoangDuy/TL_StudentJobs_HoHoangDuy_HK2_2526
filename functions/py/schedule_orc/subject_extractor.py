import re
import numpy as np


def normalize_subject_name(text):

    if text is None:
        return "N/A"

    text = text.strip()

    corrections = {

        "Chuyn dê":
            "Chuyên đề",

        "Chuyn de":
            "Chuyên đề",

        "Chuyên dè":
            "Chuyên đề",

        "Låp trinh":
            "Lập trình",

        "Lap trinh":
            "Lập trình",

        "Thuong mai dien tu":
            "Thương mại điện tử"
    }

    for wrong, right in corrections.items():

        text = re.sub(

            re.escape(wrong),

            right,

            text,

            flags=re.IGNORECASE
        )

    return text


def is_header_text(text):

    return re.search(

        r'(?:Thứ|Thú|Thu|CN)',

        text,

        re.IGNORECASE

    ) is not None


def extract_subjects(
        result,
        anchors
):

    subjects = []

    data = result[0]

    polys = data["dt_polys"]

    texts = data["rec_texts"]

    print("\n========== SUBJECT DETECTOR ==========")

    for anchor in anchors:

        anchor_x = anchor["centerX"]

        anchor_y = anchor["centerY"]

        candidates = []

        print("\n--------------------------------")
        print("ANCHOR:")
        print(anchor)

        for i in range(len(texts)):

            text = str(texts[i])

            poly = polys[i]

            coords = np.array(
                poly,
                dtype=np.float32
            ).flatten()

            center_x = np.mean(
                coords[0::2]
            )

            center_y = np.mean(
                coords[1::2]
            )

            same_column = (

                    abs(center_x - anchor_x)
                    < 140
            )

            upper_area = (

                    center_y < anchor_y
            )

            near_anchor = (

                    abs(anchor_y - center_y)
                    < 180
            )

            if not (
                    same_column
                    and upper_area
                    and near_anchor
            ):
                continue

            # bỏ header ngày

            if is_header_text(text):
                continue

            # bỏ giờ học

            if re.search(
                    r'\d{2}:\d{2}',
                    text):
                continue

            # bỏ text rác

            if re.search(
                    r'Nhóm',
                    text,
                    re.IGNORECASE
            ):
                continue

            if re.search(
                    r'Phòng',
                    text,
                    re.IGNORECASE
            ):
                continue

            if re.search(
                    r'GV',
                    text,
                    re.IGNORECASE
            ):
                continue

            if re.search(
                    r'K\.CNTT',
                    text,
                    re.IGNORECASE
            ):
                continue

            candidates.append({

                "text": text,

                "centerY": center_y
            })

        print("CANDIDATES:")
        print(candidates)

        if candidates:

            candidates.sort(

                key=lambda x: x["centerY"]
            )

            subject_lines = []

            for item in candidates[:2]:

                txt = item["text"].strip()

                if txt:

                    subject_lines.append(
                        txt
                    )

            subject = " ".join(
                subject_lines
            )

            subject = normalize_subject_name(
                subject
            )

            print("SUBJECT:")
            print(subject)

            subjects.append(
                subject
            )

        else:

            print("SUBJECT: N/A")

            subjects.append(
                "N/A"
            )

    print("\nFINAL SUBJECTS:")
    print(subjects)

    return subjects