package com.studentjobs.app.feature.trust.components

import androidx.compose.runtime.Composable

@Composable
fun TrustFilterDropdown(

    selectedFilter: TrustFilter,

    onFilterChange: (TrustFilter) -> Unit

) {

    var expanded by remember {

        mutableStateOf(false)
    }

    ExposedDropdownMenuBox(

        expanded = expanded,

        onExpandedChange = {

            expanded = !expanded
        }
    ) {

        OutlinedTextField(

            value = when(selectedFilter){

                TrustFilter.ALL ->
                    "Tất cả"

                TrustFilter.POSITIVE ->
                    "Điểm cộng"

                TrustFilter.NEGATIVE ->
                    "Điểm trừ"
            },

            onValueChange = {},

            readOnly = true,

            modifier =
                Modifier.menuAnchor()
        )

        ExposedDropdownMenu(

            expanded = expanded,

            onDismissRequest = {

                expanded = false
            }
        ) {

            DropdownMenuItem(

                text = {
                    Text("Tất cả")
                },

                onClick = {

                    onFilterChange(
                        TrustFilter.ALL
                    )

                    expanded = false
                }
            )

            DropdownMenuItem(

                text = {
                    Text("Điểm cộng")
                },

                onClick = {

                    onFilterChange(
                        TrustFilter.POSITIVE
                    )

                    expanded = false
                }
            )

            DropdownMenuItem(

                text = {
                    Text("Điểm trừ")
                },

                onClick = {

                    onFilterChange(
                        TrustFilter.NEGATIVE
                    )

                    expanded = false
                }
            )
        }
    }
}