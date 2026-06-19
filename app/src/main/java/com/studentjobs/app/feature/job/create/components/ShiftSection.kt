package com.studentjobs.app.feature.job.create.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.studentjobs.app.data.model.job.ShiftEntity

@Composable
fun ShiftSection(

    shifts: List<ShiftEntity>,

    selectedDay: Int,

    startMinute: String,

    endMinute: String,

    slots: String,

    onDaySelected: (Int) -> Unit,

    onStartMinuteChange: (String) -> Unit,

    onEndMinuteChange: (String) -> Unit,

    onAddShift: () -> Unit,

    onDeleteShift: (String) -> Unit

) {

    val days = mapOf(

        1 to "Thứ 2",
        2 to "Thứ 3",
        3 to "Thứ 4",
        4 to "Thứ 5",
        5 to "Thứ 6",
        6 to "Thứ 7",
        7 to "Chủ nhật"
    )

    Card(

        modifier =
            Modifier.fillMaxWidth(),

        elevation =
            CardDefaults.cardElevation(
                defaultElevation = 4.dp
            )

    ) {

        Column(

            modifier =
                Modifier.padding(16.dp),

            verticalArrangement =
                Arrangement.spacedBy(12.dp)

        ) {

            Text(

                text = "🕒 Lịch làm việc",

                style =
                    MaterialTheme.typography.titleMedium
            )

            Text(
                text = "Thêm một hoặc nhiều lịch làm việc"
            )

            // =========================
            // DAY SELECTOR
            // =========================

            FlowRow(

                horizontalArrangement =
                    Arrangement.spacedBy(8.dp)

            ) {

                days.forEach { (day, label) ->

                    FilterChip(

                        selected =
                            selectedDay == day,

                        onClick = {

                            onDaySelected(day)
                        },

                        label = {

                            Text(label)
                        }
                    )
                }
            }

            // =========================
            // TIME
            // =========================

            OutlinedTextField(

                value =
                    startMinute,

                onValueChange =
                    onStartMinuteChange,

                modifier =
                    Modifier.fillMaxWidth(),

                label = {

                    Text(
                        "Giờ bắt đầu"
                    )
                },

                placeholder = {

                    Text(
                        "13:00"
                    )
                }
            )

            OutlinedTextField(

                value =
                    endMinute,

                onValueChange =
                    onEndMinuteChange,

                modifier =
                    Modifier.fillMaxWidth(),

                label = {

                    Text(
                        "Giờ kết thúc"
                    )
                },

                placeholder = {

                    Text(
                        "20:00"
                    )
                }
            )

            AssistChip(

                onClick =
                    onAddShift,

                label = {

                    Text(
                        "➕ Thêm ca làm"
                    )
                }
            )

            // =========================
            // SHIFT LIST
            // =========================

            if (shifts.isNotEmpty()) {

                Column(

                    verticalArrangement =
                        Arrangement.spacedBy(8.dp)

                ) {

                    shifts.forEach { shift ->

                        ShiftItemCard(

                            shift = shift,

                            onDelete = {

                                onDeleteShift(
                                    shift.shiftId
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}