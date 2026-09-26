package com.app.screentime.feature.preferences

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import com.app.screentime.core.ui.theme.FunnelSansFontFamily
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.telekom.odsystem.R
import com.telekom.odsystem.atoms.ODSBorder
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.divider.ODSDivider
import com.telekom.odsystem.atoms.divider.ODSDividerProps
import com.telekom.odsystem.atoms.divider.ODSDividerVariant
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.tokens.ODSTextStyles
import com.telekom.odsystem.tokens.ODSVariables
import com.telekom.odsystem.tokens.tokens.ODSTheme
import java.time.LocalDate
import java.time.Period

/**
 * Step 1: Personal Basics (Name, Gender with Vertical Radio Buttons, Date of Birth, Height in cm + feet/inches).
 * Conforms strictly to Telekom ODS guidelines and project design rules.
 */
@Composable
fun PreferencesStepOne(
    name: String,
    onNameChange: (String) -> Unit,
    nameError: String? = null,
    gender: String,
    onGenderChange: (String) -> Unit,
    genderError: String? = null,
    day: String,
    month: String,
    year: String,
    onDayChange: (String) -> Unit,
    onMonthChange: (String) -> Unit,
    onYearChange: (String) -> Unit,
    dobError: String? = null,
    heightCm: String,
    onHeightChange: (String) -> Unit,
    heightError: String? = null,
    scheme: ODSTheme,
    onSubmit: () -> Unit,
    modifier: Modifier = Modifier
) {
    val calculatedAge = remember(day, month, year) {
        try {
            val y = year.toIntOrNull() ?: 1999
            val m = month.toIntOrNull() ?: 1
            val d = day.toIntOrNull() ?: 1
            val currentYear = LocalDate.now().year
            val birthDate = LocalDate.of(
                y.coerceIn(1920, currentYear),
                m.coerceIn(1, 12),
                d.coerceIn(1, 31)
            )
            Period.between(birthDate, LocalDate.now()).years
        } catch (_: Exception) {
            24
        }
    }

    val funnelFontSemiBold = FunnelSansFontFamily

    // Gender options in standard display order
    val genders = listOf("Man", "Woman", "Non-binary")

    // Height in feet & inches calculation
    val heightNum = heightCm.toIntOrNull() ?: 168
    val totalInches = kotlin.math.round(heightNum / 2.54).toInt()
    val feet = totalInches / 12
    val inches = totalInches % 12
    val heightFeetFormatted = "$feet'$inches\" ($feet ft $inches in)"

    ODSColumn(
        modifier = modifier.fillMaxWidth(),
        gap = ODSVariables.spacingComponent2
    ) {
        // ── Heading & Subtitle ─────────────────────────────────────────────
        ODSColumn(
            modifier = Modifier.fillMaxWidth(),
            gap = ODSVariables.spacingComponent2
        ) {
            ODSText(
                modifier = Modifier.fillMaxWidth(),
                text = "Tell us about yourself 🩷",
                style = ODSTextStyles.bodyMBold,
                color = scheme.basicTextDominant
            )

            ODSText(
                modifier = Modifier.fillMaxWidth(),
                text = "Let's get the absolute basics sorted out first.",
                style = ODSTextStyles.bodyMRegular,
                color = scheme.basicTextRecessive
            )
        }

        // ── 1. First Name Card ──────────────────────────────────────────────
        ODSColumn(
            modifier = Modifier.fillMaxWidth(),
            gap = 4.dp
        ) {
            ODSRow(
                modifier = Modifier.fillMaxWidth(),
                gap = ODSVariables.spacingComponent4,
                padding = ODSPadding(all = 14.dp),
                cornerRadius = ODSCorners(all = 20.dp),
                border = ODSBorder(
                    width = ODSVariables.strokes2,
                    colorList = listOf(
                        ODSColorModel(
                            hexColor = if (nameError != null) scheme.functionalDestructiveStandard else scheme.basicStroke
                        )
                    )
                ),
                verticalAlignment = Alignment.CenterVertically,
                background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard))
            ) {
                ODSColumn(
                    cornerRadius = ODSCorners(all = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    background = listOf(ODSColorModel(hexColor = scheme.basicAccentSecondary)),
                    width = 32.dp,
                    height = 32.dp
                ) {
                    ODSIcon(
                        iconModel = ODSIconModel(
                            drawableRes = R.drawable.ic_user,
                            contentDescription = "User"
                        ),
                        tint = scheme.basicAccent.getColor(),
                        modifier = Modifier.size(16.dp)
                    )
                }

                ODSColumn(
                    modifier = Modifier.weight(1f),
                    gap = ODSVariables.spacingComponent1,
                    verticalAlignment = Alignment.Top,
                    horizontalAlignment = Alignment.Start
                ) {
                    ODSText(
                        text = "YOUR FIRST NAME",
                        style = ODSTextStyles.microcopyBold,
                        color = scheme.basicTextRecessive
                    )
                    BasicTextField(
                        value = name,
                        onValueChange = onNameChange,
                        textStyle = TextStyle(
                            color = scheme.basicTextDominant.getColor(),
                            fontSize = 16.sp,
                            fontFamily = funnelFontSemiBold,
                            fontWeight = FontWeight.SemiBold
                        ),
                        cursorBrush = SolidColor(scheme.basicAccent.getColor()),
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.Words,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(onDone = { onSubmit() }),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            if (nameError != null) {
                ODSText(
                    text = nameError,
                    style = ODSTextStyles.microcopyRegular,
                    color = scheme.functionalDestructiveStandard,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }

        // Divider
        ODSDivider(
            scheme = scheme,
            props = ODSDividerProps(variant = ODSDividerVariant.HORIZONTAL),
            modifier = Modifier.padding(vertical = 12.dp)
        )

        // ── 2. Gender Selection (Vertical Radio Button List) ─────────────────
        ODSColumn(
            modifier = Modifier.fillMaxWidth(),
            gap = 6.dp
        ) {
            ODSRow(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                gap = ODSVariables.spacingComponent2
            ) {
                ODSIcon(
                    iconModel = ODSIconModel(
                        drawableRes = R.drawable.ic_globe,
                        contentDescription = "Gender"
                    ),
                    tint = scheme.basicAccent.getColor(),
                    modifier = Modifier.size(16.dp)
                )
                ODSText(
                    text = "GENDER",
                    style = ODSTextStyles.microcopyBold,
                    color = scheme.basicTextRecessive
                )
            }

            // Vertical list of Radio Options
            ODSColumn(
                modifier = Modifier.fillMaxWidth(),
                gap = 8.dp
            ) {
                genders.forEach { g ->
                    val isSelected = gender.equals(g, ignoreCase = true)
                    ODSRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                                onClick = { onGenderChange(g) }
                            ),
                        padding = ODSPadding(
                            horizontal = 16.dp,
                            vertical = 14.dp
                        ),
                        cornerRadius = ODSCorners(all = 16.dp),
                        border = ODSBorder(
                            width = if (isSelected) 2.dp else 1.dp,
                            colorList = listOf(
                                ODSColorModel(
                                    hexColor = if (isSelected) scheme.basicAccent else scheme.basicStroke
                                )
                            )
                        ),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        background = listOf(
                            ODSColorModel(
                                hexColor = if (isSelected) scheme.basicAccentSecondary else scheme.basicBackgroundCard
                            )
                        )
                    ) {
                        ODSText(
                            text = g,
                            style = ODSTextStyles.bodySBold,
                            color = if (isSelected) scheme.basicAccent else scheme.basicTextDominant
                        )

                        // Custom ODS Radio Indicator
                        ODSBox(
                            modifier = Modifier.size(22.dp),
                            cornerRadius = ODSCorners(all = 11.dp),
                            border = ODSBorder(
                                width = 2.dp,
                                colorList = listOf(
                                    ODSColorModel(
                                        hexColor = if (isSelected) scheme.basicAccent else scheme.basicStroke
                                    )
                                )
                            ),
                            background = listOf(
                                ODSColorModel(
                                    hexColor = if (isSelected) scheme.basicBackgroundCard else scheme.basicBackgroundCard
                                )
                            ),
                            contentAlignment = Alignment.Center
                        ) {
                            if (isSelected) {
                                ODSBox(
                                    modifier = Modifier.size(12.dp),
                                    cornerRadius = ODSCorners(all = 6.dp),
                                    background = listOf(ODSColorModel(hexColor = scheme.basicAccent))
                                )
                            }
                        }
                    }
                }
            }

            if (genderError != null) {
                ODSText(
                    text = genderError,
                    style = ODSTextStyles.microcopyRegular,
                    color = scheme.functionalDestructiveStandard,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }

        // Divider
        ODSDivider(
            scheme = scheme,
            props = ODSDividerProps(variant = ODSDividerVariant.HORIZONTAL),
            modifier = Modifier.padding(vertical = 12.dp)
        )

        // ── 3. Date of Birth Section ────────────────────────────────────────
        ODSColumn(
            modifier = Modifier.fillMaxWidth(),
            gap = ODSVariables.spacingComponent3
        ) {
            ODSRow(
                modifier = Modifier.fillMaxWidth(),
                gap = ODSVariables.spacingComponent3,
                horizontalAlignment = Alignment.Start,
                verticalAlignment = Alignment.Top
            ) {
                // Day Card
                ODSColumn(
                    modifier = Modifier.weight(1f),
                    padding = ODSPadding(all = ODSVariables.spacingComponent3),
                    cornerRadius = ODSCorners(all = ODSVariables.radiusMedium),
                    border = ODSBorder(
                        width = ODSVariables.strokes2,
                        colorList = listOf(
                            ODSColorModel(
                                hexColor = if (dobError != null) scheme.functionalDestructiveStandard else scheme.basicStroke
                            )
                        )
                    ),
                    background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard))
                ) {
                    ODSText(
                        text = "Day",
                        style = ODSTextStyles.microcopyBold,
                        color = scheme.basicTextRecessive
                    )
                    BasicTextField(
                        value = day,
                        onValueChange = { if (it.length <= 2) onDayChange(it) },
                        textStyle = TextStyle(
                            color = scheme.basicTextDominant.getColor(),
                            fontSize = 16.sp,
                            fontFamily = funnelFontSemiBold,
                            fontWeight = FontWeight.SemiBold
                        ),
                        cursorBrush = SolidColor(scheme.basicAccent.getColor()),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                // Month Card
                ODSColumn(
                    modifier = Modifier.weight(1f),
                    padding = ODSPadding(all = ODSVariables.spacingComponent3),
                    cornerRadius = ODSCorners(all = ODSVariables.radiusMedium),
                    border = ODSBorder(
                        width = ODSVariables.strokes2,
                        colorList = listOf(
                            ODSColorModel(
                                hexColor = if (dobError != null) scheme.functionalDestructiveStandard else scheme.basicStroke
                            )
                        )
                    ),
                    background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard))
                ) {
                    ODSText(
                        text = "Month",
                        style = ODSTextStyles.microcopyBold,
                        color = scheme.basicTextRecessive
                    )
                    BasicTextField(
                        value = month,
                        onValueChange = { if (it.length <= 2) onMonthChange(it) },
                        textStyle = TextStyle(
                            color = scheme.basicTextDominant.getColor(),
                            fontSize = 16.sp,
                            fontFamily = funnelFontSemiBold,
                            fontWeight = FontWeight.SemiBold
                        ),
                        cursorBrush = SolidColor(scheme.basicAccent.getColor()),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                // Year Card
                ODSColumn(
                    modifier = Modifier.weight(1f),
                    padding = ODSPadding(all = ODSVariables.spacingComponent3),
                    cornerRadius = ODSCorners(all = ODSVariables.radiusMedium),
                    border = ODSBorder(
                        width = ODSVariables.strokes2,
                        colorList = listOf(
                            ODSColorModel(
                                hexColor = if (dobError != null) scheme.functionalDestructiveStandard else scheme.basicStroke
                            )
                        )
                    ),
                    background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard))
                ) {
                    ODSText(
                        text = "Year",
                        style = ODSTextStyles.microcopyBold,
                        color = scheme.basicTextRecessive
                    )
                    BasicTextField(
                        value = year,
                        onValueChange = { if (it.length <= 4) onYearChange(it) },
                        textStyle = TextStyle(
                            color = scheme.basicTextDominant.getColor(),
                            fontSize = 16.sp,
                            fontFamily = funnelFontSemiBold,
                            fontWeight = FontWeight.SemiBold
                        ),
                        cursorBrush = SolidColor(scheme.basicAccent.getColor()),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            if (dobError != null) {
                ODSText(
                    text = dobError,
                    style = ODSTextStyles.microcopyRegular,
                    color = scheme.functionalDestructiveStandard,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }

            // Age Indicator Card
            ODSRow(
                modifier = Modifier.fillMaxWidth(),
                gap = ODSVariables.spacingComponent3,
                padding = ODSPadding(
                    top = ODSVariables.spacingComponent3,
                    bottom = ODSVariables.spacingComponent3,
                    left = ODSVariables.spacingLayout1,
                    right = ODSVariables.spacingLayout1
                ),
                cornerRadius = ODSCorners(all = ODSVariables.radiusMedium),
                border = ODSBorder(
                    width = ODSVariables.strokes2,
                    colorList = listOf(ODSColorModel(hexColor = scheme.basicStroke))
                ),
                horizontalAlignment = Alignment.Start,
                verticalAlignment = Alignment.CenterVertically,
                background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard))
            ) {
                ODSBox(
                    clipContent = true,
                    contentAlignment = Alignment.Center,
                    width = 20.dp,
                    height = 20.dp
                ) {
                    ODSIcon(
                        iconModel = ODSIconModel(
                            drawableRes = R.drawable.ic_cake,
                            contentDescription = "Cake"
                        ),
                        tint = scheme.basicAccent.getColor(),
                        modifier = Modifier.size(18.dp)
                    )
                }

                ODSText(
                    text = if (calculatedAge >= 18) "You will be shown as $calculatedAge years old" else "Age: $calculatedAge (Must be 18+)",
                    style = ODSTextStyles.bodySBold,
                    color = if (calculatedAge >= 18) scheme.basicTextDominant else scheme.functionalDestructiveStandard
                )
            }
        }

        // Divider
        ODSDivider(
            scheme = scheme,
            props = ODSDividerProps(variant = ODSDividerVariant.HORIZONTAL),
            modifier = Modifier.padding(vertical = 12.dp)
        )

        // ── 4. Height Card & Slider (cm + feet/inches) ──────────────────────
        ODSColumn(
            modifier = Modifier.fillMaxWidth(),
            gap = 4.dp
        ) {
            ODSRow(
                modifier = Modifier.fillMaxWidth(),
                gap = ODSVariables.spacingComponent4,
                padding = ODSPadding(all = 14.dp),
                cornerRadius = ODSCorners(all = 20.dp),
                border = ODSBorder(
                    width = ODSVariables.strokes2,
                    colorList = listOf(
                        ODSColorModel(
                            hexColor = if (heightError != null) scheme.functionalDestructiveStandard else scheme.basicStroke
                        )
                    )
                ),
                verticalAlignment = Alignment.CenterVertically,
                background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard))
            ) {
                ODSColumn(
                    cornerRadius = ODSCorners(all = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    background = listOf(ODSColorModel(hexColor = scheme.basicAccentSecondary)),
                    width = 32.dp,
                    height = 32.dp
                ) {
                    ODSIcon(
                        iconModel = ODSIconModel(
                            drawableRes = R.drawable.filter,
                            contentDescription = "Height"
                        ),
                        tint = scheme.basicAccent.getColor(),
                        modifier = Modifier.size(16.dp)
                    )
                }

                ODSColumn(
                    modifier = Modifier.weight(1f),
                    gap = ODSVariables.spacingComponent2,
                    verticalAlignment = Alignment.Top,
                    horizontalAlignment = Alignment.Start
                ) {
                    ODSRow(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        ODSText(
                            text = "HEIGHT",
                            style = ODSTextStyles.microcopyBold,
                            color = scheme.basicTextRecessive
                        )
                        ODSText(
                            text = "$heightCm cm • $heightFeetFormatted",
                            style = ODSTextStyles.bodySBold,
                            color = scheme.basicAccent
                        )
                    }

                    Slider(
                        value = heightCm.toFloatOrNull() ?: 168f,
                        onValueChange = { onHeightChange(it.toInt().toString()) },
                        valueRange = 140f..210f,
                        colors = SliderDefaults.colors(
                            thumbColor = scheme.basicAccent.getColor(),
                            activeTrackColor = scheme.basicAccent.getColor(),
                            inactiveTrackColor = scheme.basicAccentSecondary.getColor()
                        ),
                        modifier = Modifier.fillMaxWidth().height(24.dp)
                    )
                }
            }

            if (heightError != null) {
                ODSText(
                    text = heightError,
                    style = ODSTextStyles.microcopyRegular,
                    color = scheme.functionalDestructiveStandard,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
    }
}
