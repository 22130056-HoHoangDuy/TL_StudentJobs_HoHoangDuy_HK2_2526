package com.studentjobs.app.feature.job.create.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.studentjobs.app.data.model.job.ShiftEntity
import com.studentjobs.app.feature.job.create.formatMinute
import com.studentjobs.app.feature.job.create.getDayName

@Composable
fun ShiftItemCard(

    shift: ShiftEntity,

    onDelete: () -> Unit

) {

    Card(

        modifier =
            Modifier.fillMaxWidth()

    ) {

        Row(

            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(12.dp),

            horizontalArrangement =
                Arrangement.SpaceBetween

        ) {

            Column {

                Text(

                    text =
                        getDayName(
                            shift.dayOfWeek
                        ),

                    style =
                        MaterialTheme
                            .typography
                            .titleMedium
                )

                Text(

                    text =
                        "${formatMinute(shift.startMinute)} - ${
                            formatMinute(
                                shift.endMinute
                            )
                        }"
                )

                Text(

                    text =
                        "👥 ${shift.slots} slots"
                )
            }

            IconButton(

                onClick =
                    onDelete

            ) {

                Icon(

                    imageVector =
                        Icons.Default.Delete,

                    contentDescription =
                        null
                )
            }
        }
    }
}