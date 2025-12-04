package com.app.screentime.landing.component

import androidx.compose.animation.core.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.stringResource
import com.app.screentime.R
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.atoms.icon.ODSIconModel

import com.telekom.odsystem.tokens.tokens.ODSTheme
import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSLinearGradientModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.R.*
import com.telekom.odsystem.atoms.button.ODSButton
import com.telekom.odsystem.atoms.button.ODSButtonButtonType
import com.telekom.odsystem.atoms.button.ODSButtonProps
import com.telekom.odsystem.atoms.button.ODSButtonSize
import com.telekom.odsystem.atoms.button.ODSButtonVariant
import com.telekom.odsystem.neutralScheme
import java.util.Calendar

@Preview(showBackground = true)
@Composable
fun GreetingUi(
    username: String? = null,
    onLeaderboardClick: (() -> Unit)? = null,
    scheme: ODSTheme = neutralScheme
) {
    ODSRow(
        modifier = Modifier.fillMaxWidth()
    ) {
        ODSColumn {
            ODSText(
                text = getGreetingBasedOnTime(),
                style = DSTextStyles.bodyMRegular,
                color = scheme.basicTextRecessive
            )
            ODSText(
                text = username ?: "User",
                style = DSTextStyles.bodyMBold,
                color = scheme.basicText
            )
        }
        Spacer(modifier = Modifier.weight(1f))

        val successColor = scheme.functionalSuccessStandard
        // Animated pulsing effect
        val infiniteTransition = rememberInfiniteTransition(label = "pulse")
        val scale by infiniteTransition.animateFloat(
            initialValue = 1f,
            targetValue = 1.1f,
            animationSpec = infiniteRepeatable(
                animation = tween(1000, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "scale"
        )

        ODSButton(
            scheme = scheme,
            props = ODSButtonProps(
                buttonIcon = ODSIconModel(
                    drawableRes = drawable.achievement_type_standard_size_standard,
                    tint = scheme.functionalDestructiveStandard
                ),
                buttonType = ODSButtonButtonType.ICON_ONLY,
                variant = ODSButtonVariant.OUTLINE,
                size = ODSButtonSize.SMALL
            ),
            onClick = { onLeaderboardClick?.invoke() }
        )
    }
}

@Composable
fun getGreetingBasedOnTime(): String {
    val hour = remember { Calendar.getInstance().get(Calendar.HOUR_OF_DAY) }

    return when (hour) {
        in 5..11 -> stringResource(R.string.good_morning)
        in 12..16 -> stringResource(R.string.good_afternoon)
        in 17..20 -> stringResource(R.string.good_evening)
        else -> stringResource(R.string.good_night)
    }
}
