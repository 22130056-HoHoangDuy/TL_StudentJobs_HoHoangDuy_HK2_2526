function convertTimeToMinute(time) {

    const [hour, minute] =
        time.split(":").map(Number);

    return (hour * 60) + minute;
}

function isTimeOverlap(

    startA,
    endA,
    startB,
    endB

) {

    return (

        startA < endB &&
        startB < endA
    );
}

module.exports = {

    convertTimeToMinute,

    isTimeOverlap
};