package com.studentjobs.app.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun NotificationTopBar(

    onNotificationClick: () -> Unit

) {

    TopAppBar(

        title = {

            Text(

                "StudentJobs"

            )
        },

        actions = {

            IconButton(

                onClick = onNotificationClick

            ) {

                Icon(

                    Icons.Outlined.Notifications,

                    contentDescription = "Notification"

                )
            }
        }
    )
}