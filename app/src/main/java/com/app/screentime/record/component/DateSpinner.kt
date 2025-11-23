package com.app.screentime.record.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.screentime.ui.atom.AppText
import com.app.screentime.ui.atom.AppTextStyle
import com.app.screentime.ui.theme.LocalAppColors
import com.app.screentime.utils.DateUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateSpinner(
    onDateSelected: (String) -> Unit = {}
) {
    val colors = LocalAppColors.current ?: return

    val today = DateUtils.today()
    val dates = (0..3).map { today.minusDays(it) }

    val dateDisplayMap = dates.associate {
        it.toString("yyyy-MM-dd") to it.toString("d MMM yyyy")
    }

    var expanded by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf(today.toString("yyyy-MM-dd")) }
    var displayDate by remember { mutableStateOf(dateDisplayMap[selectedDate]!!) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {

        Box(
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .border(
                    width = 1.dp, color = colors.border, shape = RoundedCornerShape(12.dp)
                )
                .background(colors.card)
                .padding(horizontal = 14.dp, vertical = 8.dp)
                .clickable { expanded = true }   // opens dropdown
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                // ⭐ Actual Text Field Content
                BasicTextField(
                    value = displayDate, readOnly = true, onValueChange = {}, textStyle = TextStyle(
                        fontSize = 16.sp, color = colors.textPrimary
                    ), modifier = Modifier.weight(1f)
                )

                Icon(
                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp
                    else Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    tint = colors.textSecondary
                )
            }
        }

        // ⭐ Dropdown menu
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(colors.card, RoundedCornerShape(12.dp))
        ) {
            dates.forEach { date ->

                val apiDate = date.toString("yyyy-MM-dd")
                val displayString = dateDisplayMap[apiDate]!!

                DropdownMenuItem(text = {
                    AppText(
                        text = displayString,
                        style = AppTextStyle.Body,
                        color = colors.textPrimary
                    )
                }, onClick = {
                    selectedDate = apiDate
                    displayDate = displayString
                    expanded = false
                    onDateSelected(apiDate)
                })
            }
        }
    }
}

