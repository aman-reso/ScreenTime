package com.app.screentime.reward.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.app.screentime.reward.model.CoinHistoryItem
import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.molecules.listrowstandard.ODSListRowStandard
import com.telekom.odsystem.molecules.listrowstandard.ODSListRowStandardProps
import com.telekom.odsystem.molecules.listrowstandard.ODSListRowStandardVariant
import com.telekom.odsystem.tokens.tokens.ODSTheme
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

/**
 * Coin History Item Component
 * Displays a single coin transaction using ODSListRowStandard
 */
@Composable
fun CoinHistoryItem(
    coinHistoryItem: CoinHistoryItem,
    scheme: ODSTheme,
    modifier: Modifier = Modifier
) {
    val dateFormatter = DateTimeFormatter.ofPattern("d MMMM h:mm a")
        .withZone(ZoneId.systemDefault())

    val formattedDate = try {
        val instant = Instant.parse(coinHistoryItem.createdAt)
        dateFormatter.format(instant)
    } catch (e: Exception) {
        coinHistoryItem.createdAt
    }

    val amountText = if (coinHistoryItem.amount > 0) {
        "+ ₹${coinHistoryItem.amount}"
    } else {
        "- ₹${kotlin.math.abs(coinHistoryItem.amount)}"
    }

    val amountColor = if (coinHistoryItem.amount > 0) {
        scheme.functionalSuccessStandard
    } else {
        scheme.basicText
    }

    ODSRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
    ) {
        ODSListRowStandard(
            modifier = Modifier.weight(1f),
            scheme = scheme,
            props = ODSListRowStandardProps(
                variant = ODSListRowStandardVariant.STANDARD,
                labelText = coinHistoryItem.description,
                descriptionText = formattedDate,
                showDescriptionTitle = false
            )
        )

        // Amount on the right side
        ODSText(
            text = amountText,
            style = DSTextStyles.bodyMBold,
            color = amountColor
        )
    }
}

