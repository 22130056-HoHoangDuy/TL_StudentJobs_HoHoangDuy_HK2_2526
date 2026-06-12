package com.studentjobs.app.feature.trust.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.studentjobs.app.data.model.trust.TrustLog
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun TrustLogItem(
    log: TrustLog
) {

    val pointColor =

        if (log.changeAmount >= 0)
            Color(0xFF22C55E)
        else
            Color.Red

    Card(
        modifier =
            Modifier.fillMaxWidth()
    ) {

        Column(
            modifier =
                Modifier.padding(16.dp)
        ) {

            Text(
                text =
                    formatAction(
                        log.actionType
                    ),

                style =
                    MaterialTheme.typography.titleSmall,

                fontWeight =
                    FontWeight.Bold
            )

            Spacer(
                modifier =
                    Modifier.height(6.dp)
            )

            Text(
                text =
                    log.description
                        ?: ""
            )

            Spacer(
                modifier =
                    Modifier.height(6.dp)
            )

            Text(
                text =
                    if (log.changeAmount >= 0)
                        "+${log.changeAmount}"
                    else
                        log.changeAmount.toString(),

                color =
                    pointColor,

                fontWeight =
                    FontWeight.Bold
            )

            Spacer(
                modifier =
                    Modifier.height(6.dp)
            )

            Text(
                text =
                    SimpleDateFormat(
                        "dd/MM/yyyy",
                        Locale.getDefault()
                    ).format(
                        Date(log.createdAt)
                    ),

                style =
                    MaterialTheme.typography.bodySmall
            )
        }
    }
}

private fun formatAction(
    action: String
): String {

    return when (action) {

        "EMAIL_VERIFIED" ->
            "Xác thực Email"

        "PHONE_VERIFIED" ->
            "Xác thực số điện thoại"

        "STUDENT_VERIFIED" ->
            "Xác thực sinh viên"

        "EMPLOYER_VERIFIED" ->
            "Xác thực doanh nghiệp"

        "JOB_COMPLETED" ->
            "Hoàn thành công việc"

        "JOB_CANCELLED" ->
            "Hủy công việc"

        else ->
            action
    }
}