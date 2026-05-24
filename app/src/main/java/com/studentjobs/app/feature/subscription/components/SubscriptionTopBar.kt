package com.studentjobs.app.feature.subscription.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubscriptionTopBar(

    onBackClick: () -> Unit

) {

    TopAppBar(

        title = {

            Text(
                text = "StudentJobs PLUS"
            )
        },

        navigationIcon = {

            IconButton(
                onClick = onBackClick
            ) {

                Icon(
                    imageVector =
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