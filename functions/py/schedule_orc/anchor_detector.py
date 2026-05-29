import re
import numpy as np


def detect_anchors(result):

    anchors = []

    data = result[0]

    polys = data["dt_polys"]

    texts = data["rec_texts"]

    print("\n========== ANCHOR DETECTOR ==========")

    for i in range(len(texts)):

        text = str(texts[i])

        print(
            f"OCR TEXT: {text}"
        )

        poly = polys[i]

        match = re.search(

            r'(\d{2}:\d{2}).*?(\d{2}:\d{2})',

            text
        )

        if match:

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

            print(
                f"FOUND TIME: {text}"
            )

            anchor = {

                "startTime":
                    match.group(1),

                "endTime":
                    match.group(2),

                "centerX":
                    float(center_x),

                "centerY":
                    float(center_y)
            }

            anchors.append(
                anchor
            )

            print(
                f"FOUND ANCHOR: {anchor}"
            )

    print("\nANCHOR COUNT:")
    print(len(anchors))

    print("ANCHORS:")
    print(anchors)

    return anchors