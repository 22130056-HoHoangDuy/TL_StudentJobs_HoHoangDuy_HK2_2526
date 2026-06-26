package com.studentjobs.app.feature.notification

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationScreen(

    viewModel: NotificationViewModel,

    onBack: () -> Unit

) {

    val notifications by
    viewModel.notifications.collectAsState()

    val loading by
    viewModel.isLoading.collectAsState()

    Scaffold(

        topBar = {

            TopAppBar(

                title = {

                    Text("Thông báo")

                },

                navigationIcon = {

                    IconButton(

                        onClick = onBack

                    ) {

                        Icon(

                            Icons.Default.ArrowBack,

                            contentDescription = null

                        )
                    }
                },

                colors = TopAppBarDefaults.topAppBarColors(

                    containerColor = Color.White

                )
            )
        }

    ) { padding ->

        if (loading) {

            Box(

                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),

                contentAlignment = Alignment.Center

            ) {

                CircularProgressIndicator()
            }

        } else {

            LazyColumn(

                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFF8FAFC))
                    .padding(padding),

                contentPadding = PaddingValues(16.dp),

                verticalArrangement = Arrangement.spacedBy(12.dp)

            ) {

                items(

                    notifications,

                    key = {

                        it.notificationId

                    }

                ) { notification ->

                    Card(

                        modifier = Modifier

                            .fillMaxWidth()

                            .clickable {

                                if (!notification.isRead) {

                                    viewModel.markAsRead(

                                        notification.notificationId

                                    )
                                }
                            },

                        colors = CardDefaults.cardColors(

                            containerColor =

                                if (notification.isRead)

                                    Color.White
                                else

                                    Color(0xFFEAF4FF)

                        )

                    ) {

                        Column(

                            modifier = Modifier.padding(16.dp)

                        ) {

                            Text(

                                text = notification.title,

                                style = MaterialTheme.typography.titleMedium,

                                fontWeight = FontWeight.Bold

                            )

                            Text(

                                text = notification.message,

                                modifier = Modifier.padding(top = 8.dp)

                            )

                            Text(

                                text =

                                    notification.createdAt
                                        ?.toString()
                                        ?: "",

                                modifier = Modifier.padding(top = 12.dp),

                                style = MaterialTheme.typography.bodySmall,

                                color = Color.Gray

                            )
                        }
                    }
                }
            }
        }
    }
}