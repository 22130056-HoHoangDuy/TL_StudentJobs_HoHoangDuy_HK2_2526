package com.studentjobs.app.feature.profile.shared.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.HourglassBottom
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.studentjobs.app.data.model.status.VerificationStatus

@Composable
fun VerificationCard(

    title: String,

    description: String,

    status: VerificationStatus,

    enabled: Boolean = true,

    onClick: () -> Unit

) {

    val (icon, color, statusText) = when (status) {

        VerificationStatus.VERIFIED -> Triple(

            Icons.Default.CheckCircle,

            MaterialTheme.colorScheme.primary,

            "Verified"
        )

        VerificationStatus.UNVERIFIED -> Triple(

            Icons.Default.Warning,

            MaterialTheme.colorScheme.error,

            "Not verified"
        )

        VerificationStatus.PENDING -> Triple(

            Icons.Default.HourglassBottom,

            MaterialTheme.colorScheme.tertiary,

            "Pending"
        )

        VerificationStatus.REJECTED -> Triple(

            Icons.Default.Warning,

            MaterialTheme.colorScheme.error,

            "Rejected"
        )
    }

    Card(

        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable(

                enabled =

                    enabled &&

                            status != VerificationStatus.PENDING

            ) {

                onClick()
            },

        shape = RoundedCornerShape(16.dp),

        elevation = CardDefaults.cardElevation(2.dp)

    ) {

        Row(

            modifier = Modifier.padding(16.dp),

            verticalAlignment = Alignment.CenterVertically

        ) {

            Icon(

                imageVector = icon,

                contentDescription = null,

                tint = color,

                modifier = Modifier.size(32.dp)
            )

            Spacer(
                modifier = Modifier.width(12.dp)
            )

            Column(

                modifier = Modifier.weight(1f)

            ) {

                Text(

                    text = title,

                    style = MaterialTheme.typography.titleMedium
                )

                Text(

                    text = description,

                    style = MaterialTheme.typography.bodySmall,

                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(
                    modifier = Modifier.height(4.dp)
                )

                Text(

                    text = statusText,

                    color = color,

                    style = MaterialTheme.typography.labelMedium
                )
            }

            Button(

                onClick = onClick,

                enabled =

                    enabled &&

                            status != VerificationStatus.PENDING,

                shape = RoundedCornerShape(12.dp)

            ) {

                Text(

                    text = when (status) {

                        VerificationStatus.VERIFIED -> "View"

                        VerificationStatus.UNVERIFIED -> "Verify"

                        VerificationStatus.PENDING -> "Pending"

                        VerificationStatus.REJECTED -> "Retry"
                    }
                )
            }
        }
    }
}