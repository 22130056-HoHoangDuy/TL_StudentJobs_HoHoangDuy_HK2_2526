package com.studentjobs.app.feature.verification.gate

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.VerifiedUser
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun VerificationGateScreen(

    onCompleteProfile: () -> Unit,

    onLogout: () -> Unit

) {

    Column(

        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),

        verticalArrangement = Arrangement.Center,

        horizontalAlignment = Alignment.CenterHorizontally

    ) {

        Icon(

            imageVector = Icons.Outlined.VerifiedUser,

            contentDescription = null,

            tint = MaterialTheme.colorScheme.primary
        )

        Text(

            text = "Hoàn tất xác thực",

            style = MaterialTheme.typography.headlineSmall,

            fontWeight = FontWeight.Bold,

            modifier = Modifier.padding(top = 16.dp)
        )

        Text(

            text =
                "Bạn cần hoàn tất xác thực tài khoản trước khi sử dụng StudentJobs.",

            style = MaterialTheme.typography.bodyMedium,

            modifier = Modifier.padding(
                top = 12.dp,
                bottom = 32.dp
            )
        )

        Button(

            onClick = onCompleteProfile

        ) {

            Text("Hoàn tất ngay")
        }

        TextButton(

            onClick = onLogout

        ) {

            Text("Đăng xuất")
        }
    }
}