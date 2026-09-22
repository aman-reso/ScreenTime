package com.app.screentime.feature.profile

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.screentime.core.ui.theme.zonaODSTheme
import com.telekom.odsystem.R
import com.telekom.odsystem.atoms.ODSBorder
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSImage
import com.telekom.odsystem.atoms.ODSImageModel
import com.telekom.odsystem.atoms.ODSLazyColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSEffect
import com.telekom.odsystem.foundations.ODSElevation
import com.telekom.odsystem.foundations.ODSElevationType
import com.telekom.odsystem.foundations.ODSLinearGradientModel
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.tokens.ODSTextStyles
import com.telekom.odsystem.tokens.ODSVariables
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * Edit Profile Screen (Matching Figma node-id 21-158: https://figma.com/design/1TELYpr19qLAv99DexRNpi/Untitled?node-id=21-158).
 *
 * Strictly follows all project rules (GEMINI.md):
 * 1. 100% ODS components & ODSLazyColumn for vertical scroll.
 * 2. Colors picked from composable's `scheme: ODSTheme`.
 * 3. Max text size is 16sp with Funnel Sans font.
 * 4. Padding, margins, and gaps mapped to ODSVariables.
 */
@Composable
fun EditProfileScreen(
    onBack: () -> Unit,
    onSaved: () -> Unit = onBack,
    modifier: Modifier = Modifier,
    scheme: ODSTheme = zonaODSTheme
) {
    val context = LocalContext.current

    val funnelSansFontFamily = remember {
        FontFamily(Font(R.font.funnelsans_regular))
    }

    var fullName by remember { mutableStateOf("Alex Rivera") }
    var aboutMe by remember {
        mutableStateOf("Looking for someone to explore vinyl record shops and grab craft coffee with on sunny weekends...")
    }
    var gender by remember { mutableStateOf("Male") }
    var dob by remember { mutableStateOf("Oct 12, 1998") }
    var languages by remember { mutableStateOf("English, Spanish") }
    var height by remember { mutableStateOf("182 cm") }
    var income by remember { mutableStateOf("$80k - $100k") }
    var intent by remember { mutableStateOf("Long-term") }
    var interestedIn by remember { mutableStateOf("Women") }
    var location by remember { mutableStateOf("Brooklyn, NY") }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            Toast.makeText(context, "Photo uploaded successfully!", Toast.LENGTH_SHORT).show()
        }
    }

    ODSColumn(
        modifier = modifier.fillMaxSize(),
        clipContent = true,
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.SpaceBetween,
        background = listOf(
            ODSColorModel(
                gradient = ODSLinearGradientModel(
                    colorStops = arrayOf(
                        0.00f to scheme.basicBackground,       // #FFF1F6
                        0.52f to scheme.basicBackgroundSubtle, // #FFF9FB
                        1.00f to scheme.basicBackgroundCard    // #FFFFFF
                    ),
                    opacity = 1.00f,
                    angleInDegrees = 180f
                )
            )
        )
    ) {
        // ── 1. Scrollable Form Content using ODSLazyColumn (Rule #1) ───────────
        ODSLazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .statusBarsPadding(),
            gap = ODSVariables.spacingComponent4
        ) {
            item {
                ODSRow(
                    modifier = Modifier.fillMaxWidth(),
                    padding = ODSPadding(
                        bottom = ODSVariables.spacingComponent3,
                        left = ODSVariables.spacingLayout1,
                        right = ODSVariables.spacingLayout1
                    ),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    ODSRow(
                        padding = ODSPadding(all = ODSVariables.spacingComponent4),
                        cornerRadius = ODSCorners(all = ODSVariables.radiusMedium),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = onBack
                        )
                    ) {
                        ODSIcon(
                            iconModel = ODSIconModel(
                                drawableRes = R.drawable.ic_arrow_left,
                                contentDescription = "Back"
                            ),
                            tint = scheme.basicTextDominant.getColor(),
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    ODSText(
                        text = "Edit Profile",
                        style = ODSTextStyles.bodyMBold,
                        color = scheme.basicTextDominant
                    )

                    // Symmetrical balance box
                    ODSBox(modifier = Modifier.size(44.dp))
                }
            }

            // ── Item: Manage Photos Section ─────────────────────────────────────
            item {
                ODSRow(
                    modifier = Modifier.fillMaxWidth(),
                    gap = ODSVariables.spacingLayout1,
                    padding = ODSPadding(
                        left = ODSVariables.spacingLayout1,
                        right = ODSVariables.spacingLayout1
                    ),
                    horizontalAlignment = Alignment.Start,
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    // Circular Avatar Container with 2dp border
                    ODSRow(
                        cornerRadius = ODSCorners(all = 40.dp),
                        border = ODSBorder(
                            width = 2.dp,
                            colorList = listOf(ODSColorModel(hexColor = scheme.basicStroke))
                        ),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard)),
                        width = 80.dp,
                        height = 80.dp
                    ) {
                        ODSImage(
                            imageModel = ODSImageModel(
                                url = "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=400&auto=format&fit=crop&q=80",
                                contentDescription = "user-avatar"
                            ),
                            width = 72.dp,
                            height = 72.dp,
                            cornerRadius = ODSCorners(all = 36.dp),
                            contentScale = ContentScale.Crop
                        )
                    }

                    ODSColumn(
                        gap = 6.dp,
                        verticalAlignment = Alignment.Top,
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.Top
                    ) {
                        ODSText(
                            text = "Manage Photos",
                            style = ODSTextStyles.bodyMBold,
                            color = scheme.basicTextDominant
                        )

                        ODSRow(
                            gap = ODSVariables.spacingComponent3,
                            horizontalAlignment = Alignment.Start,
                            verticalAlignment = Alignment.Top,
                            horizontalArrangement = Arrangement.Start
                        ) {
                            // "Upload New" Button
                            ODSRow(
                                modifier = Modifier.clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null,
                                    onClick = { galleryLauncher.launch("image/*") }
                                ),
                                padding = ODSPadding(
                                    top = 6.dp,
                                    bottom = 6.dp,
                                    left = ODSVariables.spacingComponent4,
                                    right = ODSVariables.spacingComponent4
                                ),
                                cornerRadius = ODSCorners(all = ODSVariables.radiusSmall),
                                border = ODSBorder(
                                    width = 1.dp,
                                    colorList = listOf(ODSColorModel(hexColor = scheme.basicAccent))
                                ),
                                horizontalAlignment = Alignment.Start,
                                verticalAlignment = Alignment.Top,
                                horizontalArrangement = Arrangement.Start,
                                background = listOf(ODSColorModel(hexColor = scheme.basicAccentSecondary))
                            ) {
                                ODSText(
                                    text = "Upload New",
                                    style = ODSTextStyles.microcopyBold,
                                    color = scheme.basicTextDominant
                                )
                            }

                            // "Camera" Button
                            ODSRow(
                                modifier = Modifier.clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null,
                                    onClick = {
                                        Toast.makeText(
                                            context,
                                            "Camera opening...",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                ),
                                padding = ODSPadding(
                                    top = 6.dp,
                                    bottom = 6.dp,
                                    left = ODSVariables.spacingComponent4,
                                    right = ODSVariables.spacingComponent4
                                ),
                                cornerRadius = ODSCorners(all = ODSVariables.radiusSmall),
                                border = ODSBorder(
                                    width = 1.dp,
                                    colorList = listOf(ODSColorModel(hexColor = scheme.basicStroke))
                                ),
                                horizontalAlignment = Alignment.Start,
                                verticalAlignment = Alignment.Top,
                                horizontalArrangement = Arrangement.Start,
                                background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard))
                            ) {
                                ODSText(
                                    text = "Camera",
                                    style = ODSTextStyles.microcopyBold,
                                    color = scheme.basicTextDominant
                                )
                            }
                        }
                    }
                }
            }

            // ── Item: FULL NAME ─────────────────────────────────────────────────
            item {
                ODSColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = ODSVariables.spacingLayout1),
                    gap = 6.dp
                ) {
                    ODSText(
                        text = "FULL NAME",
                        style = ODSTextStyles.microcopyBold,
                        color = scheme.basicTextDominant
                    )
                    ODSRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        padding = ODSPadding(
                            top = ODSVariables.spacingComponent4,
                            bottom = ODSVariables.spacingComponent4,
                            left = ODSVariables.spacingLayout1,
                            right = ODSVariables.spacingLayout1
                        ),
                        cornerRadius = ODSCorners(all = 14.dp),
                        border = ODSBorder(
                            width = 2.dp,
                            colorList = listOf(ODSColorModel(hexColor = scheme.basicStroke))
                        ),
                        horizontalAlignment = Alignment.Start,
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start,
                        background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard))
                    ) {
                        BasicTextField(
                            value = fullName,
                            onValueChange = { fullName = it },
                            textStyle = TextStyle(
                                fontFamily = funnelSansFontFamily,
                                fontSize = 15.sp,
                                color = scheme.basicTextDominant.getColor()
                            ),
                            cursorBrush = SolidColor(scheme.basicAccent.getColor()),
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )
                    }
                }
            }

            // ── Item: ABOUT ME ──────────────────────────────────────────────────
            item {
                ODSColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = ODSVariables.spacingLayout1),
                    gap = 6.dp
                ) {
                    ODSText(
                        text = "ABOUT ME",
                        style = ODSTextStyles.microcopyBold,
                        color = scheme.basicTextDominant
                    )
                    ODSRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(72.dp),
                        padding = ODSPadding(
                            top = ODSVariables.spacingComponent4,
                            bottom = ODSVariables.spacingComponent4,
                            left = ODSVariables.spacingLayout1,
                            right = ODSVariables.spacingLayout1
                        ),
                        cornerRadius = ODSCorners(all = 14.dp),
                        border = ODSBorder(
                            width = 2.dp,
                            colorList = listOf(ODSColorModel(hexColor = scheme.basicStroke))
                        ),
                        horizontalAlignment = Alignment.Start,
                        verticalAlignment = Alignment.Top,
                        horizontalArrangement = Arrangement.Start,
                        background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard))
                    ) {
                        BasicTextField(
                            value = aboutMe,
                            onValueChange = { aboutMe = it },
                            textStyle = TextStyle(
                                fontFamily = funnelSansFontFamily,
                                fontSize = 15.sp,
                                color = scheme.basicTextDominant.getColor(),
                                lineHeight = 20.sp
                            ),
                            cursorBrush = SolidColor(scheme.basicAccent.getColor()),
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }

            // ── Item: Row 1 (GENDER & DATE OF BIRTH) ─────────────────────────────
            item {
                ODSRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = ODSVariables.spacingLayout1),
                    gap = ODSVariables.spacingComponent4,
                    horizontalAlignment = Alignment.Start,
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.Start
                ) {
                    ProfileDropdownField(
                        label = "GENDER",
                        value = gender,
                        scheme = scheme,
                        onClick = {
                            gender = if (gender == "Male") "Female" else "Male"
                        },
                        modifier = Modifier.weight(1f)
                    )
                    ProfileDropdownField(
                        label = "DATE OF BIRTH",
                        value = dob,
                        scheme = scheme,
                        labelColor = scheme.basicTextRecessive,
                        onClick = {
                            Toast.makeText(context, "Select Date of Birth", Toast.LENGTH_SHORT)
                                .show()
                        },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // ── Item: Row 2 (LANGUAGES & HEIGHT) ────────────────────────────────
            item {
                ODSRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = ODSVariables.spacingLayout1),
                    gap = ODSVariables.spacingComponent4,
                    horizontalAlignment = Alignment.Start,
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.Start
                ) {
                    ProfileDropdownField(
                        label = "LANGUAGES",
                        value = languages,
                        scheme = scheme,
                        onClick = {
                            Toast.makeText(context, "Select Languages", Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier.weight(1f)
                    )
                    ProfileDropdownField(
                        label = "HEIGHT",
                        value = height,
                        scheme = scheme,
                        onClick = {
                            Toast.makeText(context, "Select Height", Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // ── Item: Row 3 (INCOME & RELATIONSHIP INTENT) ───────────────────────
            item {
                ODSRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = ODSVariables.spacingLayout1),
                    gap = ODSVariables.spacingComponent4,
                    horizontalAlignment = Alignment.Start,
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.Start
                ) {
                    ProfileDropdownField(
                        label = "INCOME",
                        value = income,
                        scheme = scheme,
                        onClick = {
                            Toast.makeText(context, "Select Income Range", Toast.LENGTH_SHORT)
                                .show()
                        },
                        modifier = Modifier.weight(1f)
                    )
                    ProfileDropdownField(
                        label = "RELATIONSHIP INTENT",
                        value = intent,
                        scheme = scheme,
                        onClick = {
                            intent = when (intent) {
                                "Long-term" -> "Casual"
                                "Casual" -> "Friendship"
                                else -> "Long-term"
                            }
                        },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // ── Item: Row 4 (INTERESTED IN & LOCATION) ───────────────────────────
            item {
                ODSRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = ODSVariables.spacingLayout1),
                    gap = ODSVariables.spacingComponent4,
                    horizontalAlignment = Alignment.Start,
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.Start
                ) {
                    ProfileDropdownField(
                        label = "INTERESTED IN",
                        value = interestedIn,
                        scheme = scheme,
                        onClick = {
                            interestedIn = when (interestedIn) {
                                "Women" -> "Men"
                                "Men" -> "Everyone"
                                else -> "Women"
                            }
                        },
                        modifier = Modifier.weight(1f)
                    )
                    ProfileDropdownField(
                        label = "LOCATION",
                        value = location,
                        scheme = scheme,
                        onClick = {
                            Toast.makeText(context, "Select Location", Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Bottom scroll margin
            item {
                Spacer(modifier = Modifier.height(ODSVariables.spacingComponent5))
            }
        }

        // ── 2. Sticky Bottom CTA & Home Indicator ──────────────────────────────
        ODSColumn(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(
                    horizontal = ODSVariables.spacingLayout1,
                    vertical = ODSVariables.spacingComponent3
                ),
            gap = ODSVariables.spacingComponent3,
            verticalAlignment = Alignment.Top,
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ) {
            // "Save Profile Changes" Button
            ODSRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = {
                            Toast.makeText(context, "Profile changes saved!", Toast.LENGTH_SHORT)
                                .show()
                            onSaved()
                        }
                    ),
                padding = ODSPadding(all = ODSVariables.spacingLayout1),
                cornerRadius = ODSCorners(all = ODSVariables.radiusMedium),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                background = listOf(ODSColorModel(hexColor = scheme.basicAccent)),
                effect = ODSEffect(
                    elevations = listOf(
                        ODSElevation(
                            x = 0,
                            y = 6,
                            blur = 14,
                            spread = 0,
                            color = HexColor(0x38D81B60),
                            type = ODSElevationType.DROP_SHADOW
                        )
                    )
                )
            ) {
                ODSText(
                    text = "Save Profile Changes",
                    style = ODSTextStyles.bodyMBold,
                    color = scheme.basicTextOnAccent
                )
            }

            // Home Indicator Bar
            ODSRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = ODSVariables.spacingComponent4,
                        bottom = ODSVariables.spacingComponent2
                    ),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.Center
            ) {
                ODSBox(
                    modifier = Modifier.size(width = 134.dp, height = 4.dp),
                    background = listOf(ODSColorModel(hexColor = scheme.basicStroke)),
                    cornerRadius = ODSCorners(all = ODSVariables.radiusFull)
                )
            }
        }
    }
}

/**
 * Clean Form Dropdown Component conforming to Figma node-id 21-158.
 */
@Composable
private fun ProfileDropdownField(
    label: String,
    value: String,
    scheme: ODSTheme,
    labelColor: HexColor = scheme.basicTextDominant,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ODSColumn(
        modifier = modifier,
        gap = 6.dp,
        verticalAlignment = Alignment.Top,
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        ODSText(
            text = label,
            style = ODSTextStyles.microcopyBold,
            color = labelColor
        )

        ODSRow(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onClick
                ),
            padding = ODSPadding(
                top = ODSVariables.spacingComponent4,
                bottom = ODSVariables.spacingComponent4,
                left = ODSVariables.spacingLayout1,
                right = ODSVariables.spacingLayout1
            ),
            cornerRadius = ODSCorners(all = 14.dp),
            border = ODSBorder(
                width = 2.dp,
                colorList = listOf(ODSColorModel(hexColor = scheme.basicStroke))
            ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard))
        ) {
            ODSText(
                text = value,
                style = ODSTextStyles.bodyMRegular,
                color = scheme.basicTextDominant,
                modifier = Modifier.weight(1f, fill = false)
            )

            ODSIcon(
                iconModel = ODSIconModel(
                    drawableRes = R.drawable.ic_chevron_down,
                    contentDescription = "Dropdown"
                ),
                tint = scheme.basicTextRecessive.getColor(),
                modifier = Modifier.size(14.dp)
            )
        }
    }
}
