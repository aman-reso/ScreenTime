package com.app.screentime.feature.discover

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Verified
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.app.screentime.core.model.ModelProfile
import com.telekom.odsystem.atoms.ODSBorder
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.ODSWrap
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.atoms.tagstatic.ODSTagStatic
import com.telekom.odsystem.atoms.tagstatic.ODSTagStaticProps
import com.telekom.odsystem.atoms.tagstatic.ODSTagStaticType
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.tokens.ODSTextStyles
import com.telekom.odsystem.tokens.tokens.ODSTheme

@Composable
fun ModelProfileInfoSection(
    model: ModelProfile,
    scheme: ODSTheme,
    modifier: Modifier = Modifier
) {
    ODSColumn(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        gap = 14.dp
    ) {
        // 1. Name & Verified Badge
        ODSColumn(gap = 4.dp) {
            ODSRow(
                verticalAlignment = Alignment.CenterVertically,
                gap = 8.dp
            ) {
                ODSText(
                    text = "${model.name}, ${model.age}",
                    style = ODSTextStyles.titleL,
                    color = scheme.basicText
                )
                ODSIcon(
                    iconModel = ODSIconModel(imageVector = Icons.Outlined.Verified),
                    tint = scheme.basicAccent.getColor(),
                    modifier = Modifier.size(22.dp)
                )
            }

            ODSRow(
                verticalAlignment = Alignment.CenterVertically,
                gap = 6.dp
            ) {
                ODSIcon(
                    iconModel = ODSIconModel(drawableRes = com.telekom.odsystem.R.drawable.location),
                    tint = scheme.basicTextRecessive.getColor(),
                    modifier = Modifier.size(16.dp)
                )
                ODSText(
                    text = model.location.ifBlank { "Mumbai, India" },
                    style = ODSTextStyles.microcopyRegular,
                    color = scheme.basicTextRecessive
                )
            }
        }

        // 2. Call & Chat Rate Badges
        ODSRow(
            modifier = Modifier.fillMaxWidth(),
            gap = 10.dp
        ) {
            // Voice Rate
            ODSBox(
                modifier = Modifier.weight(1f),
                background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard)),
                cornerRadius = ODSCorners(all = 12.dp),
                border = ODSBorder(
                    width = 1.dp,
                    colorList = listOf(ODSColorModel(hexColor = scheme.basicStrokeSubtle))
                ),
                padding = ODSPadding(horizontal = 12.dp, vertical = 10.dp)
            ) {
                ODSRow(
                    verticalAlignment = Alignment.CenterVertically,
                    gap = 8.dp
                ) {
                    ODSIcon(
                        iconModel = ODSIconModel(drawableRes = com.telekom.odsystem.R.drawable.call),
                        tint = scheme.basicAccent.getColor(),
                        width = 24.dp,
                        height = 24.dp
                    )
                    ODSColumn(gap = 2.dp) {
                        ODSText(
                            text = "${model.ratePerMinute} Coins/min",
                            style = ODSTextStyles.bodySBold,
                            color = scheme.basicText
                        )
                        ODSText(
                            text = "Voice & Video",
                            style = ODSTextStyles.microcopyRegular,
                            color = scheme.basicTextRecessive
                        )
                    }
                }
            }

            // Chat Rate
            ODSBox(
                modifier = Modifier.weight(1f),
                background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard)),
                cornerRadius = ODSCorners(all = 12.dp),
                border = ODSBorder(
                    width = 1.dp,
                    colorList = listOf(ODSColorModel(hexColor = scheme.basicStrokeSubtle))
                ),
                padding = ODSPadding(horizontal = 12.dp, vertical = 10.dp)
            ) {
                ODSRow(
                    verticalAlignment = Alignment.CenterVertically,
                    gap = 8.dp
                ) {
                    ODSIcon(
                        iconModel = ODSIconModel(drawableRes = com.telekom.odsystem.R.drawable.message),
                        tint = scheme.basicAccentSecondary.getColor(),
                        width = 24.dp,
                        height = 24.dp
                    )
                    ODSColumn(gap = 2.dp) {
                        ODSText(
                            text = "${model.chatRate} Coins/msg",
                            style = ODSTextStyles.bodySBold,
                            color = scheme.basicText
                        )
                        ODSText(
                            text = "Instant Chat",
                            style = ODSTextStyles.microcopyRegular,
                            color = scheme.basicTextRecessive
                        )
                    }
                }
            }
        }

        // 3. Bio / About Card
        ODSBox(
            modifier = Modifier.fillMaxWidth(),
            background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard)),
            cornerRadius = ODSCorners(all = 12.dp),
            border = ODSBorder(
                width = 1.dp,
                colorList = listOf(ODSColorModel(hexColor = scheme.basicStrokeSubtle))
            ),
            padding = ODSPadding(all = 14.dp)
        ) {
            ODSColumn(
                modifier = Modifier.fillMaxWidth(),
                gap = 6.dp
            ) {
                ODSText(
                    text = "About Me",
                    style = ODSTextStyles.bodySBold,
                    color = scheme.basicText
                )
                ODSText(
                    text = model.bio.ifBlank { "Hey there! I love deep conversations, fun chats, and connecting with awesome people. Feel free to call or text anytime!" },
                    style = ODSTextStyles.bodySRegular,
                    color = scheme.basicTextRecessive
                )
            }
        }

        // 4. Tags & Interests
        val tags = if (model.tags.isNotEmpty()) model.tags else listOf(
            "Travel",
            "Music",
            "Coffee",
            "Late Night Talks",
            "Gaming"
        )
        ODSColumn(gap = 6.dp) {
            ODSText(
                text = "Interests & Topics",
                style = ODSTextStyles.bodySBold,
                color = scheme.basicText
            )
            ODSWrap(
                horizontalGap = 8.dp,
                verticalGap = 8.dp
            ) {
                tags.forEach { tag ->
                    ODSTagStatic(
                        scheme = scheme,
                        props = ODSTagStaticProps(
                            label = "# $tag",
                            type = ODSTagStaticType.SUBTLE
                        )
                    )
                }
            }
        }
    }
}
