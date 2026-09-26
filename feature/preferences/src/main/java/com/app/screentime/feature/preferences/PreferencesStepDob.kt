package com.app.screentime.feature.preferences

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.telekom.odsystem.R
import com.telekom.odsystem.atoms.ODSBorder
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.tokens.ODSTextStyles
import com.telekom.odsystem.tokens.tokens.ODSTheme
import java.time.LocalDate

private val MONTHS = listOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")

private enum class PickerType {
    NONE, DAY, MONTH, YEAR
}

/**
 * Step 3: Date of Birth Preference.
 * Matches Figma mockup (Frame 3):
 * - "When were you born?"
 * - "Your profile shows your age, not your birthday."
 * - Three dropdown cards: [ Day v ] [ Month v ] [ Year v ]
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PreferencesStepDob(
    day: String,
    month: String,
    year: String,
    onDayChange: (String) -> Unit,
    onMonthChange: (String) -> Unit,
    onYearChange: (String) -> Unit,
    scheme: ODSTheme,
    modifier: Modifier = Modifier,
    error: String? = null
) {
    val primaryTextColor = scheme.basicTextDominant
    val secondaryTextColor = scheme.basicTextRecessive
    val accentColor = scheme.basicAccent
    val inactiveBorderColor = scheme.basicStrokeSubtle

    var activePicker by remember { mutableStateOf(PickerType.NONE) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val currentYear = remember { LocalDate.now().year }
    val daysList = remember { (1..31).map { it.toString() } }
    val yearsList = remember { ((currentYear - 80)..(currentYear - 18)).reversed().map { it.toString() } }

    val formattedMonth = remember(month) {
        val mNum = month.toIntOrNull()
        if (mNum != null && mNum in 1..12) {
            MONTHS[mNum - 1]
        } else if (MONTHS.contains(month)) {
            month
        } else {
            "Jan"
        }
    }

    ODSColumn(
        modifier = modifier.fillMaxWidth(),
        gap = 28.dp,
        verticalAlignment = Alignment.Top,
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        // ── 1. Heading & Subtitle ──────────────────────────────────────────
        ODSColumn(
            modifier = Modifier.fillMaxWidth(),
            gap = 8.dp,
            verticalAlignment = Alignment.Top,
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ) {
            ODSText(
                modifier = Modifier.fillMaxWidth(),
                text = "When were you born?",
                style = ODSTextStyles.titleL,
                color = primaryTextColor
            )
            ODSText(
                modifier = Modifier.fillMaxWidth(),
                text = "Your profile shows your age, not your birthday.",
                style = ODSTextStyles.bodyMRegular,
                color = secondaryTextColor
            )
        }

        // ── 2. Dropdown Pickers Row ────────────────────────────────────────
        ODSColumn(
            modifier = Modifier.fillMaxWidth(),
            gap = 6.dp
        ) {
            ODSRow(
                modifier = Modifier.fillMaxWidth(),
                gap = 12.dp,
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Day Picker Card
                DobPickerCard(
                    modifier = Modifier.weight(1f),
                    value = day.ifBlank { "15" },
                    isActive = activePicker == PickerType.DAY,
                    accentColor = accentColor,
                    inactiveBorderColor = inactiveBorderColor,
                    primaryTextColor = primaryTextColor,
                    secondaryTextColor = secondaryTextColor,
                    cardBackground = scheme.basicBackgroundCard,
                    onClick = { activePicker = PickerType.DAY }
                )

                // Month Picker Card (Highlighted by default or when active)
                DobPickerCard(
                    modifier = Modifier.weight(1.2f),
                    value = formattedMonth,
                    isActive = activePicker == PickerType.MONTH || activePicker == PickerType.NONE,
                    accentColor = accentColor,
                    inactiveBorderColor = inactiveBorderColor,
                    primaryTextColor = primaryTextColor,
                    secondaryTextColor = secondaryTextColor,
                    cardBackground = scheme.basicBackgroundCard,
                    onClick = { activePicker = PickerType.MONTH }
                )

                // Year Picker Card
                DobPickerCard(
                    modifier = Modifier.weight(1.3f),
                    value = year.ifBlank { "1998" },
                    isActive = activePicker == PickerType.YEAR,
                    accentColor = accentColor,
                    inactiveBorderColor = inactiveBorderColor,
                    primaryTextColor = primaryTextColor,
                    secondaryTextColor = secondaryTextColor,
                    cardBackground = scheme.basicBackgroundCard,
                    onClick = { activePicker = PickerType.YEAR }
                )
            }

            if (error != null) {
                ODSText(
                    text = error,
                    style = ODSTextStyles.microcopyRegular,
                    color = scheme.functionalDestructiveStandard
                )
            }
        }
    }

    // ── Bottom Sheet Selection Modal ───────────────────────────────────────
    if (activePicker != PickerType.NONE) {
        ModalBottomSheet(
            onDismissRequest = { activePicker = PickerType.NONE },
            sheetState = sheetState,
            containerColor = scheme.basicBackgroundCard.getColor()
        ) {
            val title = when (activePicker) {
                PickerType.DAY -> "Select Day"
                PickerType.MONTH -> "Select Month"
                PickerType.YEAR -> "Select Year"
                PickerType.NONE -> ""
            }
            val itemsList = when (activePicker) {
                PickerType.DAY -> daysList
                PickerType.MONTH -> MONTHS
                PickerType.YEAR -> yearsList
                PickerType.NONE -> emptyList()
            }
            val selectedValue = when (activePicker) {
                PickerType.DAY -> day
                PickerType.MONTH -> formattedMonth
                PickerType.YEAR -> year
                PickerType.NONE -> ""
            }

            ODSColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp, top = 8.dp),
                gap = 12.dp
            ) {
                ODSText(
                    modifier = Modifier.padding(horizontal = 24.dp),
                    text = title,
                    style = ODSTextStyles.bodyMBold,
                    color = primaryTextColor
                )

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 280.dp)
                ) {
                    items(itemsList) { item ->
                        val isItemSel = item.equals(selectedValue, ignoreCase = true)
                        ODSRow(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    when (activePicker) {
                                        PickerType.DAY -> onDayChange(item)
                                        PickerType.MONTH -> onMonthChange(item)
                                        PickerType.YEAR -> onYearChange(item)
                                        PickerType.NONE -> {}
                                    }
                                    activePicker = PickerType.NONE
                                }
                                .padding(horizontal = 24.dp, vertical = 14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            ODSText(
                                text = item,
                                style = if (isItemSel) ODSTextStyles.bodyMBold else ODSTextStyles.bodyMRegular,
                                color = if (isItemSel) accentColor else primaryTextColor
                            )
                            if (isItemSel) {
                                ODSIcon(
                                    iconModel = ODSIconModel(
                                        drawableRes = R.drawable.ic_check,
                                        contentDescription = "Selected"
                                    ),
                                    tint = accentColor.getColor(),
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DobPickerCard(
    value: String,
    isActive: Boolean,
    accentColor: HexColor,
    inactiveBorderColor: HexColor,
    primaryTextColor: HexColor,
    secondaryTextColor: HexColor,
    cardBackground: HexColor,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ODSRow(
        modifier = modifier
            .height(54.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            ),
        cornerRadius = ODSCorners(all = 16.dp),
        border = ODSBorder(
            width = if (isActive) 2.dp else 1.dp,
            colorList = listOf(
                ODSColorModel(
                    hexColor = if (isActive) accentColor else inactiveBorderColor
                )
            )
        ),
        padding = ODSPadding(horizontal = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        background = listOf(ODSColorModel(hexColor = cardBackground))
    ) {
        ODSText(
            text = value,
            style = ODSTextStyles.bodyMBold,
            color = if (isActive) accentColor else primaryTextColor
        )
        ODSIcon(
            iconModel = ODSIconModel(
                drawableRes = R.drawable.ic_chevron_down,
                contentDescription = "Select"
            ),
            tint = (if (isActive) accentColor else secondaryTextColor).getColor(),
            modifier = Modifier.size(16.dp)
        )
    }
}
