package com.studentjobs.app.feature.trust.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun TrustGaugeCard(
    trustScore: Int,
    trustLevel: String
) {

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),

            horizontalAlignment =
                Alignment.CenterHorizontally
        ) {

            Text(
                text = "Trust Score",
                style =
                    MaterialTheme.typography.titleMedium
            )

            Spacer(
                modifier =
                    Modifier.height(20.dp)
            )

            Box(
                contentAlignment =
                    Alignment.Center
            ) {

                CircularProgressIndicator(

                    progress = {
                        trustScore / 100f
                    },

                    strokeWidth = 10.dp,

                    modifier =
                        Modifier.size(180.dp)
                )

                Column(
                    horizontalAlignment =
                        Alignment.CenterHorizontally
                ) {

                    Text(
                        text = trustScore.toString(),

                        style =
                            MaterialTheme
                                .typography
                                .displayMedium,

                        fontWeight =
                            FontWeight.Bold
                    )

                    Text(
                        text = trustLevel
                    )
                }
            }
        }
    }
}