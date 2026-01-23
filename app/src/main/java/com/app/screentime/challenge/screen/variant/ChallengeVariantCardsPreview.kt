package com.app.screentime.challenge.screen.variant

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * Combined preview showing all 6 challenge variant cards
 */
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AllChallengeVariantCardsPreview() {
    val scheme: ODSTheme = neutralScheme

    ODSColumn(
        padding = ODSPadding(all = DSVariables.spacingComponent4),
        modifier = Modifier
            .fillMaxWidth(),
        gap = DSVariables.spacingComponent4
    ) {
        // Variant 1
        ODSText(
            text = "Variant 1: Bubble In the air",
            style = DSTextStyles.bodySBold,
            color = scheme.basicText
        )

        CurvedCryptoCardPreview()


        // Variant 3
        ODSText(
            text = "Variant 3: Movies",
            style = DSTextStyles.bodySBold,
            color = scheme.basicText
        )
        ChallengeVariantCard3(
            title = "Movies",
            description = "From timeless classics to the latest releases, test your knowledge of the silver screen.",
            startTime = "09:00 AM",
            endTime = "10:00 PM",
            participantCount = 1234,
            reward = "+15",
            imageUrl = "https://fastly.picsum.photos/id/866/200/300.jpg?hmac=rcadCENKh4rD6MAp6V_ma-AyWv641M4iiOpe1RyFHeI",
            onClick = {}
        )

        // Variant 4
        ODSText(
            text = "Variant 4: 7x4 CHALLENGE",
            style = DSTextStyles.bodySBold,
            color = scheme.basicText
        )
        ChallengeVariantCard4(
            tag = "NEW",
            challengeType = "7x4 CHALLENGE",
            title = "FULL BODY WORKOUT",
            startTime = "09:00 AM",
            endTime = "10:00 PM",
            participantCount = 567,
            reward = "+15 Points",
            imageUrl = "https://fastly.picsum.photos/id/866/200/300.jpg?hmac=rcadCENKh4rD6MAp6V_ma-AyWv641M4iiOpe1RyFHeI",
            onClick = {}
        )

        // Variant 5
        ODSText(
            text = "Variant 5: FAT BURNING HIIT",
            style = DSTextStyles.bodySBold,
            color = scheme.basicText
        )
        ChallengeVariantCard5(
            title = "FAT BURNING HIIT",
            startTime = "09:00 AM",
            endTime = "10:00 PM",
            participantCount = 890,
            reward = "+15 Points",
            imageUrl = "https://fastly.picsum.photos/id/866/200/300.jpg?hmac=rcadCENKh4rD6MAp6V_ma-AyWv641M4iiOpe1RyFHeI",
            onClick = {}
        )

        // Variant 6
        ODSText(
            text = "Variant 6: Enhanced Card",
            style = DSTextStyles.bodySBold,
            color = scheme.basicText
        )
        ChallengeVariantCard6(
            title = "Challenge Title",
            startTime = "09:00 AM",
            endTime = "10:00 PM",
            imageUrl = "https://fastly.picsum.photos/id/866/200/300.jpg?hmac=rcadCENKh4rD6MAp6V_ma-AyWv641M4iiOpe1RyFHeI",
            onClick = {}
        )
    }
}

