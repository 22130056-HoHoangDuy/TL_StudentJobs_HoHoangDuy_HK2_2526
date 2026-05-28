from schedule_orc.parser_utils import re


def time_to_minute(t):

    hour, minute = map(
        int,
        t.split(":")
    )

    return (hour * 60) + minute


def normalize_text(text):

    return re.sub(
        r"\s+",
        " ",
        text
    ).strip()