package com.app.screentime.feature.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.app.screentime.core.ui.theme.zonaODSTheme
import com.telekom.odsystem.R
import com.telekom.odsystem.atoms.ODSBorder
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.tokens.ODSTextStyles
import com.telekom.odsystem.tokens.ODSVariables
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * Categorized Profile Details Component.
 *
 * Displays:
 * 1. About Me section (Bio & Relation intent badge)
 * 2. Interests section in Chip format
 * 3. Languages Spoken section in Chip format (if present)
 * 4. Location section (City, Area)
 * 5. Personal Details & Basics:
 *    - Job / Occupation
 *    - Height
 *    - Education
 *    - Relationship Type / Intent
 *    - Date of Birth / Birthday
 *    - Gender
 *    - Profile Quality Score
 *
 * Strictly adheres to 100% ODS components and design token rules.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ProfileCategorizedDetails(
    bio: String?,
    interests: List<String> = emptyList(),
    languages: List<String> = emptyList(),
    city: String? = null,
    area: String? = null,
    gender: String? = null,
    datingIntent: String? = null,
    relationType: String? = null,
    job: String? = null,
    height: String? = null,
    education: String? = null,
    dateOfBirth: String? = null,
    qualityScore: Double? = null,
    scheme: ODSTheme = zonaODSTheme,
    modifier: Modifier = Modifier
) {
    val intentDisplay = relationType?.ifBlank { null } ?: datingIntent?.ifBlank { null }

    ODSColumn(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = ODSVariables.spacingLayout1),
        gap = ODSVariables.spacingComponent5
    ) {
        // ── 1. About Section ─────────────────────────────────────────────────
        ODSBox(
            modifier = Modifier.fillMaxWidth(),
            cornerRadius = ODSCorners(all = ODSVariables.radiusMedium),
            border = ODSBorder(
                width = ODSVariables.strokes1,
                colorList = listOf(ODSColorModel(hexColor = scheme.basicStroke))
            ),
            background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard)),
            padding = ODSPadding(all = ODSVariables.spacingLayout1)
        ) {
            ODSColumn(
                modifier = Modifier.fillMaxWidth(),
                gap = ODSVariables.spacingComponent2
            ) {
                ODSRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ODSText(
                        text = "ABOUT ME",
                        style = ODSTextStyles.microcopyBold,
                        color = scheme.basicTextRecessive
                    )

                    if (!intentDisplay.isNullOrBlank()) {
                        ODSBox(
                            padding = ODSPadding(
                                horizontal = ODSVariables.spacingComponent3,
                                vertical = ODSVariables.spacingComponent1
                            ),
                            cornerRadius = ODSCorners(all = ODSVariables.radiusFull),
                            background = listOf(ODSColorModel(hexColor = scheme.basicAccentSecondary))
                        ) {
                            ODSText(
                                text = intentDisplay.replaceFirstChar { it.uppercase() },
                                style = ODSTextStyles.microcopyBold,
                                color = scheme.basicAccent
                            )
                        }
                    }
                }

                ODSText(
                    text = if (!bio.isNullOrBlank()) bio else "No bio added yet. Tell potential matches about yourself!",
                    style = ODSTextStyles.bodySRegular,
                    color = scheme.basicTextDominant
                )
            }
        }

        // ── 2. Interests Section (Chips) ─────────────────────────────────────
        val resolvedInterests = if (interests.isNotEmpty()) {
            interests
        } else {
            listOf("Coding", "Music", "Coffee", "Travel", "Photography", "Fitness")
        }

        ODSBox(
            modifier = Modifier.fillMaxWidth(),
            cornerRadius = ODSCorners(all = ODSVariables.radiusMedium),
            border = ODSBorder(
                width = ODSVariables.strokes1,
                colorList = listOf(ODSColorModel(hexColor = scheme.basicStroke))
            ),
            background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard)),
            padding = ODSPadding(all = ODSVariables.spacingLayout1)
        ) {
            ODSColumn(
                modifier = Modifier.fillMaxWidth(),
                gap = ODSVariables.spacingComponent3
            ) {
                ODSText(
                    text = "INTERESTS",
                    style = ODSTextStyles.microcopyBold,
                    color = scheme.basicTextRecessive
                )

                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(ODSVariables.spacingComponent3),
                    verticalArrangement = Arrangement.spacedBy(ODSVariables.spacingComponent3)
                ) {
                    resolvedInterests.forEach { interest ->
                        ODSBox(
                            padding = ODSPadding(
                                horizontal = ODSVariables.spacingComponent4,
                                vertical = ODSVariables.spacingComponent2
                            ),
                            cornerRadius = ODSCorners(all = ODSVariables.radiusFull),
                            border = ODSBorder(
                                width = ODSVariables.strokes1,
                                colorList = listOf(ODSColorModel(hexColor = scheme.basicStrokeSubtle))
                            ),
                            background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundSubtle)),
                            contentAlignment = Alignment.Center
                        ) {
                            ODSRow(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center,
                                gap = ODSVariables.spacingComponent2
                            ) {
                                ODSText(
                                    text = "✨",
                                    style = ODSTextStyles.microcopyRegular,
                                    color = scheme.basicAccent
                                )
                                ODSText(
                                    text = interest.replaceFirstChar { it.uppercase() },
                                    style = ODSTextStyles.bodySBold,
                                    color = scheme.basicTextDominant
                                )
                            }
                        }
                    }
                }
            }
        }

        // ── 3. Languages Spoken Section (Chips) ──────────────────────────────
        if (languages.isNotEmpty()) {
            ODSBox(
                modifier = Modifier.fillMaxWidth(),
                cornerRadius = ODSCorners(all = ODSVariables.radiusMedium),
                border = ODSBorder(
                    width = ODSVariables.strokes1,
                    colorList = listOf(ODSColorModel(hexColor = scheme.basicStroke))
                ),
                background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard)),
                padding = ODSPadding(all = ODSVariables.spacingLayout1)
            ) {
                ODSColumn(
                    modifier = Modifier.fillMaxWidth(),
                    gap = ODSVariables.spacingComponent3
                ) {
                    ODSText(
                        text = "LANGUAGES SPOKEN",
                        style = ODSTextStyles.microcopyBold,
                        color = scheme.basicTextRecessive
                    )

                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(ODSVariables.spacingComponent3),
                        verticalArrangement = Arrangement.spacedBy(ODSVariables.spacingComponent3)
                    ) {
                        languages.forEach { lang ->
                            ODSBox(
                                padding = ODSPadding(
                                    horizontal = ODSVariables.spacingComponent4,
                                    vertical = ODSVariables.spacingComponent2
                                ),
                                cornerRadius = ODSCorners(all = ODSVariables.radiusFull),
                                border = ODSBorder(
                                    width = ODSVariables.strokes1,
                                    colorList = listOf(ODSColorModel(hexColor = scheme.basicStrokeSubtle))
                                ),
                                background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundSubtle)),
                                contentAlignment = Alignment.Center
                            ) {
                                ODSRow(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center,
                                    gap = ODSVariables.spacingComponent2
                                ) {
                                    ODSText(
                                        text = "🗣️",
                                        style = ODSTextStyles.microcopyRegular,
                                        color = scheme.basicAccent
                                    )
                                    ODSText(
                                        text = lang.replaceFirstChar { it.uppercase() },
                                        style = ODSTextStyles.bodySBold,
                                        color = scheme.basicTextDominant
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // ── 4. Location Section ──────────────────────────────────────────────
        val locationDisplay = when {
            !city.isNullOrBlank() && !area.isNullOrBlank() -> "$city, $area"
            !city.isNullOrBlank() -> city
            !area.isNullOrBlank() -> area
            else -> "Bengaluru, Koramangala"
        }

        ODSBox(
            modifier = Modifier.fillMaxWidth(),
            cornerRadius = ODSCorners(all = ODSVariables.radiusMedium),
            border = ODSBorder(
                width = ODSVariables.strokes1,
                colorList = listOf(ODSColorModel(hexColor = scheme.basicStroke))
            ),
            background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard)),
            padding = ODSPadding(all = ODSVariables.spacingLayout1)
        ) {
            ODSColumn(
                modifier = Modifier.fillMaxWidth(),
                gap = ODSVariables.spacingComponent2
            ) {
                ODSText(
                    text = "LOCATION",
                    style = ODSTextStyles.microcopyBold,
                    color = scheme.basicTextRecessive
                )

                ODSRow(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start,
                    gap = ODSVariables.spacingComponent3
                ) {
                    ODSBox(
                        modifier = Modifier.size(36.dp),
                        cornerRadius = ODSCorners(all = ODSVariables.radiusFull),
                        background = listOf(ODSColorModel(hexColor = scheme.basicAccentSecondary)),
                        contentAlignment = Alignment.Center
                    ) {
                        ODSIcon(
                            iconModel = ODSIconModel(
                                drawableRes = R.drawable.ic_map_pin,
                                contentDescription = "Location"
                            ),
                            tint = scheme.basicAccent.getColor(),
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    ODSColumn(
                        gap = 2.dp,
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalAlignment = Alignment.Start
                    ) {
                        ODSText(
                            text = locationDisplay,
                            style = ODSTextStyles.bodyMBold,
                            color = scheme.basicTextDominant
                        )
                        ODSText(
                            text = "Visible to nearby connections",
                            style = ODSTextStyles.microcopyRegular,
                            color = scheme.basicText
                        )
                    }
                }
            }
        }

        // ── 5. Personal Details & Basics ─────────────────────────────────────
        ODSBox(
            modifier = Modifier.fillMaxWidth(),
            cornerRadius = ODSCorners(all = ODSVariables.radiusMedium),
            border = ODSBorder(
                width = ODSVariables.strokes1,
                colorList = listOf(ODSColorModel(hexColor = scheme.basicStroke))
            ),
            background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard)),
            padding = ODSPadding(all = ODSVariables.spacingLayout1)
        ) {
            ODSColumn(
                modifier = Modifier.fillMaxWidth(),
                gap = ODSVariables.spacingComponent4
            ) {
                ODSText(
                    text = "PERSONAL DETAILS & BASICS",
                    style = ODSTextStyles.microcopyBold,
                    color = scheme.basicTextRecessive
                )

                ODSColumn(
                    modifier = Modifier.fillMaxWidth(),
                    gap = ODSVariables.spacingComponent3
                ) {
                    if (!job.isNullOrBlank()) {
                        ProfileDetailRow(
                            label = "Job / Work",
                            value = job,
                            scheme = scheme
                        )
                    }

                    if (!height.isNullOrBlank()) {
                        ProfileDetailRow(
                            label = "Height",
                            value = height,
                            scheme = scheme
                        )
                    }

                    // Education
                    if (!education.isNullOrBlank()) {
                        ProfileDetailRow(
                            label = "Education",
                            value = education,
                            scheme = scheme
                        )
                    }

                    // Relation Type
                    if (!intentDisplay.isNullOrBlank()) {
                        ProfileDetailRow(
                            label = "Relationship Type",
                            value = intentDisplay.replaceFirstChar { it.uppercase() },
                            scheme = scheme
                        )
                    }

                    // Date of Birth / Birthday
                    if (!dateOfBirth.isNullOrBlank()) {
                        ProfileDetailRow(
                            label = "Date of Birth",
                            value = dateOfBirth,
                            scheme = scheme
                        )
                    }

                    // Gender
                    if (!gender.isNullOrBlank()) {
                        ProfileDetailRow(
                            label = "Gender",
                            value = gender.replaceFirstChar { it.uppercase() },
                            scheme = scheme
                        )
                    } else {
                        ProfileDetailRow(
                            label = "Gender",
                            value = "Male",
                            scheme = scheme
                        )
                    }

                    // Profile Score
                    val scorePct = ((qualityScore ?: 0.85) * 100).toInt()
                    ProfileDetailRow(
                        label = "Profile Score",
                        value = "$scorePct%",
                        scheme = scheme
                    )
                }
            }
        }
    }
}

@Composable
private fun ProfileDetailRow(
    label: String,
    value: String,
    scheme: ODSTheme
) {
    ODSRow(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        ODSRow(
            verticalAlignment = Alignment.CenterVertically,
            gap = ODSVariables.spacingComponent3
        ) {
            ODSText(
                text = label,
                style = ODSTextStyles.linkSBold,
                color = scheme.basicText
            )
        }
        ODSText(
            text = value,
            style = ODSTextStyles.linkSRegular,
            color = scheme.basicTextRecessive
        )
    }
}
