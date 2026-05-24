package com.studentjobs.app.feature.subscription

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Stars

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

import androidx.lifecycle.viewmodel.compose.viewModel

import com.studentjobs.app.data.model.user.SubscriptionPlan
import com.studentjobs.app.data.model.user.UserRole

import com.studentjobs.app.feature.subscription.components.AlreadyPlusBadge
import com.studentjobs.app.feature.subscription.components.SubscriptionHeader
import com.studentjobs.app.feature.subscription.components.SubscriptionTopBar

@Composable
fun SubscriptionScreen(

    role: com.studentjobs.app.data.model.user.UserRole,

    currentPlan: SubscriptionPlan,

    onUpgradeClick: () -> Unit,

    onBackClick: () -> Unit,

    viewModel: SubscriptionViewModel =
        viewModel()

) {

    val features = remember(role) {

        when (role) {

            UserRole.STUDENT -> listOf(

                FeatureItem(
                    title = "OCR Thẻ sinh viên",
                    description =
                        "Xác minh danh tính tự động",
                    isAvailableInFree = true
                ),

                FeatureItem(
                    title = "OCR Thời khóa biểu",
                    description =
                        "Phát hiện trùng lịch học"
                ),

                FeatureItem(
                    title = "Smart Auto Apply",
                    description =
                        "Tự động ứng tuyển công việc"
                ),

                FeatureItem(
                    title = "Conflict Detection",
                    description =
                        "Cảnh báo ca làm trùng lịch"
                )
            )

            UserRole.EMPLOYER -> listOf(

                FeatureItem(
                    title = "Đăng job thủ công",
                    description =
                        "Quản lý tuyển dụng cơ bản",
                    isAvailableInFree = true
                ),

                FeatureItem(
                    title = "Auto Recruitment",
                    description =
                        "Tự động tuyển ứng viên"
                ),

                FeatureItem(
                    title = "Priority Recommendation",
                    description =
                        "Ưu tiên recommendation"
                )
            )
        }
    }

    Scaffold(

        topBar = {

            SubscriptionTopBar(
                onBackClick
            )
        }

    ) { padding ->

        LazyColumn(

            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F5F5))
                .padding(padding),

            contentPadding =
                PaddingValues(16.dp),

            horizontalAlignment =
                Alignment.CenterHorizontally
        ) {

            item {

                SubscriptionHeader(

                    currentPlan = currentPlan,

                    role = role
                )

                Spacer(
                    modifier =
                        Modifier.height(24.dp)
                )
            }

            items(features) { feature ->

                Card(

                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),

                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    )
                ) {

                    Row(

                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),

                        verticalAlignment =
                            Alignment.CenterVertically
                    ) {

                        Column(
                            modifier = Modifier.weight(1f)
                        ) {

                            Text(

                                text = feature.title,

                                fontWeight =
                                    FontWeight.Bold
                            )

                            Spacer(
                                modifier =
                                    Modifier.height(4.dp)
                            )

                            Text(
                                text = feature.description
                            )
                        }

                        Icon(

                            imageVector =
                                if (feature.isAvailableInFree)
                                    Icons.Default.CheckCircle
                                else
                                    Icons.Default.Cancel,

                            contentDescription = null,

                            tint =
                                if (feature.isAvailableInFree)
                                    Color(0xFF4CAF50)
                                else
                                    Color.LightGray
                        )

                        Spacer(
                            modifier =
                                Modifier.width(12.dp)
                        )

                        Icon(

                            imageVector =
                                Icons.Default.Stars,

                            contentDescription = null,

                            tint = Color(0xFFFFB300)
                        )
                    }
                }
            }

            item {

                Spacer(
                    modifier =
                        Modifier.height(32.dp)
                )

                if (
                    currentPlan ==
                    SubscriptionPlan.FREE
                ) {

                    Button(

                        onClick = onUpgradeClick,

                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),

                        colors =
                            ButtonDefaults.buttonColors(

                                containerColor =
                                    Color(0xFFFFB300)
                            )
                    ) {

                        Text(

                            text =
                                "Nâng cấp PLUS",

                            color = Color.White,

                            fontWeight =
                                FontWeight.Bold
                        )
                    }

                } else {

                    AlreadyPlusBadge()
                }
            }
        }
    }
}