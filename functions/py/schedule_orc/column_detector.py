import re
import numpy as np


def detect_columns(result):

    raw_cols = []

    data = result[0]

    polys = data["dt_polys"]

    texts = data["rec_texts"]

    print("\n========== COLUMN DETECTOR ==========")

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

        print(
            f"TEXT: {text} | X={center_x}"
        )

        if re.search(
                r'^(?:Thứ|Thú|Thu|CN)',
                text.strip(),
                re.IGNORECASE
        ):

            raw_cols.append({

                "text": text,

                "centerX": center_x
            })

    print("\nRAW COLS:")
    print(raw_cols)

    raw_cols = sorted(

        raw_cols,

        key=lambda x: x["centerX"]
    )

    unique_columns = []

    if raw_cols:

        unique_columns.append(
            raw_cols[0]
        )

        for i in range(

                1,
                len(raw_cols)
        ):

            dist = abs(

                raw_cols[i]["centerX"]
                -
                unique_columns[-1]["centerX"]
            )

            if dist > 120:

                unique_columns.append(
                    raw_cols[i]
                )

    final_columns = []

    for i, col in enumerate(
            unique_columns
    ):

        final_columns.append({

            "dayOfWeek": i + 2,

            "centerX": float(
                col["centerX"]
            )
        })

    print("\nFINAL COLUMNS:")
    print(final_columns)

    return final_columns