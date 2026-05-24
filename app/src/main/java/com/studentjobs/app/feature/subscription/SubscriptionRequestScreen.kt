package com.studentjobs.app.feature.subscription

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.google.firebase.auth.FirebaseAuth
import com.studentjobs.app.data.model.subscription.SubscriptionRequest
import com.studentjobs.app.data.model.user.UserRole

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubscriptionRequestScreen(

    role: UserRole,

    onBackClick: () -> Unit,

    onRequestSuccess: () -> Unit,

    viewModel: SubscriptionViewModel =
        viewModel()

) {

    val state by viewModel.uiState.collectAsState()

    val snackbarHostState =
        remember { SnackbarHostState() }

    var selectedDuration by remember {

        mutableIntStateOf(30)
    }

    // payment information
    // BIDV - 3144423183 - HO HOANG DUY
    // STUDENT: 9K/month
    // EMPLOYER: 29K/month

    val monthlyPrice = when (role) {

        UserRole.STUDENT -> 9000

        UserRole.EMPLOYER -> 29000
    }

    val amount = when (selectedDuration) {

        30 -> monthlyPrice

        90 -> monthlyPrice * 3

        else -> monthlyPrice
    }

    val uid = FirebaseAuth
        .getInstance()
        .currentUser
        ?.uid
        ?: "unknown"

    val paymentContent =
        "SJPLUS_${uid.take(6)}_${selectedDuration}D"

    val qrUrl =

        "https://img.vietqr.io/image/" +

                "BIDV-3144423183-compact2.png" +

                "?amount=$amount" +

                "&addInfo=$paymentContent" +

                "&accountName=HO%20HOANG%20DUY"

    LaunchedEffect(state.successMessage) {

        state.successMessage?.let {

            snackbarHostState.showSnackbar(it)

            onRequestSuccess()
        }
    }

    LaunchedEffect(state.errorMessage) {

        state.errorMessage?.let {

            snackbarHostState.showSnackbar(it)
        }
    }

    Scaffold(

        snackbarHost = {

            SnackbarHost(
                hostState = snackbarHostState
            )
        },

        topBar = {

            TopAppBar(

                title = {

                    Text(
                        text = "Upgrade to PLUS"
                    )
                },

                colors =
                    TopAppBarDefaults.topAppBarColors(

                        containerColor =
                            Color.White
                    )
            )
        }

    ) { padding ->

        Column(

            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F5F5))
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(
                    rememberScrollState()
                ),

            verticalArrangement =
                Arrangement.spacedBy(16.dp)
        ) {

            // ====================================
            // HEADER CARD
            // ====================================

            Card(

                shape = RoundedCornerShape(28.dp),

                colors = CardDefaults.cardColors(
                    containerColor =
                        Color.Transparent
                )
            ) {

                Column(

                    modifier = Modifier
                        .background(

                            Brush.horizontalGradient(

                                listOf(

                                    Color(0xFF6A11CB),

                                    Color(0xFF2575FC)
                                )
                            )
                        )
                        .padding(24.dp)
                ) {

                    Icon(

                        imageVector =
                            Icons.Default.AutoAwesome,

                        contentDescription = null,

                        tint = Color(0xFFFFD54F)
                    )

                    Spacer(
                        modifier =
                            Modifier.height(12.dp)
                    )

                    Card(

                        modifier = Modifier
                            .fillMaxWidth(),

                        shape = RoundedCornerShape(28.dp),

                        colors = CardDefaults.cardColors(

                            containerColor = Color.White
                        )
                    ) {

                        Column(

                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),

                            horizontalAlignment =
                                Alignment.CenterHorizontally
                        ) {

                            Text(

                                text = "Scan QR to Upgrade",

                                style =
                                    MaterialTheme.typography.titleMedium,

                                fontWeight = FontWeight.Bold
                            )

                            Spacer(
                                modifier =
                                    Modifier.height(18.dp)
                            )

                            AsyncImage(

                                model = qrUrl,

                                contentDescription = null,

                                modifier = Modifier
                                    .size(240.dp)
                                    .clip(
                                        RoundedCornerShape(24.dp)
                                    ),

                                contentScale =
                                    ContentScale.Crop
                            )

                            Spacer(
                                modifier =
                                    Modifier.height(18.dp)
                            )

                            Text(

                                text = "Amount",

                                color = Color.Gray
                            )

                            Text(

                                text =
                                    "${amount} VNĐ",

                                style =
                                    MaterialTheme.typography.headlineSmall,

                                fontWeight = FontWeight.Bold,

                                color = Color(0xFFFF9800)
                            )

                            Spacer(
                                modifier =
                                    Modifier.height(16.dp)
                            )

                            HorizontalDivider()

                            Spacer(
                                modifier =
                                    Modifier.height(16.dp)
                            )

                            Text(

                                text = "Transfer Content",

                                color = Color.Gray
                            )

                            Spacer(
                                modifier =
                                    Modifier.height(8.dp)
                            )

                            Text(

                                text = paymentContent,

                                fontWeight = FontWeight.Bold,

                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    Text(

                        text = "StudentJobs PLUS",

                        style =
                            MaterialTheme.typography.headlineSmall,

                        color = Color.White,

                        fontWeight = FontWeight.Bold
                    )

                    Spacer(
                        modifier =
                            Modifier.height(8.dp)
                    )

                    Text(

                        text = if (
                            role == UserRole.STUDENT
                        ) {
                            "Smart Auto Apply • OCR Timetable • Conflict Detection"
                        } else {
                            "Auto Recruitment • Smart Filtering • Priority Recommendation"
                        },

                        color =
                            Color.White.copy(alpha = 0.9f)
                    )
                }
            }

            // ====================================
            // PLAN OPTIONS
            // ====================================

            Text(

                text = "Choose Duration",

                style =
                    MaterialTheme.typography.titleMedium,

                fontWeight = FontWeight.Bold
            )

            listOf(30, 90).forEach { days ->

                Card(

                    modifier = Modifier
                        .fillMaxWidth(),

                    shape =
                        RoundedCornerShape(20.dp),

                    colors =
                        CardDefaults.cardColors(

                            containerColor =
                                Color.White
                        )
                ) {

                    androidx.compose.foundation.layout.Row(

                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),

                        horizontalArrangement =
                            Arrangement.SpaceBetween
                    ) {

                        Column {

                            Text(

                                text =
                                    "$days Days PLUS",

                                fontWeight =
                                    FontWeight.Bold
                            )

                            Spacer(
                                modifier =
                                    Modifier.height(4.dp)
                            )

                            Text(

                                text =
                                    if (days == 30)
                                        "Starter subscription"
                                    else
                                        "Best value plan"
                            )
                        }

                        RadioButton(

                            selected =
                                selectedDuration == days,

                            onClick = {

                                selectedDuration = days
                            }
                        )
                    }
                }
            }

            Spacer(
                modifier =
                    Modifier.height(12.dp)
            )

            // ====================================
            // SUBMIT BUTTON
            // ====================================

            Button(

                onClick = {

                    val uid = FirebaseAuth
                        .getInstance()
                        .currentUser
                        ?.uid

                    if (uid != null) {

                        viewModel
                            .createSubscriptionRequest(

                                SubscriptionRequest(

                                    userUid = uid,

                                    durationDays =
                                        selectedDuration,

                                    paymentMethod = "QR",

                                    paymentAmount = amount,

                                    paymentContent = paymentContent
                                )
                            )
                    }
                },

                modifier = Modifier
                    .fillMaxWidth()
                    .height(58.dp),

                shape =
                    RoundedCornerShape(18.dp),

                colors =
                    ButtonDefaults.buttonColors(

                        containerColor =
                            Color(0xFFFFB300)
                    )
            ) {

                if (state.isLoading) {

                    CircularProgressIndicator(
                        color = Color.White
                    )

                } else {

                    Text(

                        text =
                            "Submit Upgrade Request",

                        color = Color.White,

                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}