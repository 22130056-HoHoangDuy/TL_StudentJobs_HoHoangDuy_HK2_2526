package com.studentjobs.app.feature.profile.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.studentjobs.app.feature.profile.verification.VerificationTaskItem

@Composable
fun ProfileCompletionSection(
    isStudentVerified: Boolean,
    isPhoneVerified: Boolean,
    isEmailVerified: Boolean,
    onStudentClick: () -> Unit,
    onPhoneClick: () -> Unit,
    onEmailClick: () -> Unit
) {

    val total = 3
    val completed = listOf(
        isStudentVerified,
        isPhoneVerified,
        isEmailVerified
    ).count { it }

    val progress = completed / total.toFloat()

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Text(
                text = "Complete your profile",
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                text = "Complete all steps to increase trust",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(Modifier.height(12.dp))

            LinearProgressIndicator(
                progress = progress,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
            )

            Spacer(Modifier.height(16.dp))

            VerificationTaskItem(
                title = "Verify student card",
                isDone = isStudentVerified,
                onClick = onStudentClick
            )

            VerificationTaskItem(
                title = "Verify phone number",
                isDone = isPhoneVerified,
                onClick = onPhoneClick
            )

            VerificationTaskItem(
                title = "Verify email",
                isDone = isEmailVerified,
                onClick = onEmailClick
            )
        }
    }
}