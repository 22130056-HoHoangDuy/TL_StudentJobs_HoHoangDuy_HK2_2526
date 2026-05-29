import cv2
import matplotlib.pyplot as plt


def draw_result(

        image,
        anchors,
        columns
):

    preview = image.copy()

    # ======================================
    # DRAW COLUMNS
    # ======================================

    for col in columns:

        x = int(col["centerX"])

        cv2.line(

            preview,

            (x, 0),

            (x, preview.shape[0]),

            (0, 255, 0),

            2
        )

    # ======================================
    # DRAW ANCHORS
    # ======================================

    for anchor in anchors:

        x = int(anchor["centerX"])

        y = int(anchor["centerY"])

        cv2.circle(

            preview,

            (x, y),

            10,

            (255, 0, 0),

            -1
        )

    # ======================================
    # SHOW
    # ======================================

    preview_rgb = cv2.cvtColor(

        preview,
        cv2.COLOR_BGR2RGB
    )

    plt.figure(figsize=(18, 10))

    plt.imshow(preview_rgb)

    plt.axis("off")

    plt.show()