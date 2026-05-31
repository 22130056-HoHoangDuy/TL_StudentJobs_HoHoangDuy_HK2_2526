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

    onSlotsChange: (String) -> Unit,

    onAddShift: () -> Unit,

    onDeleteShift: (String) -> Unit

) {

    val days = mapOf(

        1 to "Mon",
        2 to "Tue",
        3 to "Wed",
        4 to "Thu",
        5 to "Fri",
        6 to "Sat",
        7 to "Sun"
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

                text = "🕒 Working Shifts",

                style =
                    MaterialTheme.typography.titleMedium
            )

            Text(
                text = "Add one or more working shifts"
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
                        "Start Time"
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
                        "End Time"
                    )
                },

                placeholder = {

                    Text(
                        "20:00"
                    )
                }
            )

            OutlinedTextField(

                value =
                    slots,

                onValueChange =
                    onSlotsChange,

                modifier =
                    Modifier.fillMaxWidth(),

                label = {

                    Text(
                        "Slots"
                    )
                }
            )

            AssistChip(

                onClick =
                    onAddShift,

                label = {

                    Text(
                        "➕ Add Shift"
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