package com.app.screentime.reward.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import com.app.screentime.reward.model.RewardTransaction
import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.molecules.listrowstandard.ODSListRowStandard
import com.telekom.odsystem.molecules.listrowstandard.ODSListRowStandardProps
import com.telekom.odsystem.molecules.listrowstandard.ODSListRowStandardVariant
import com.telekom.odsystem.tokens.tokens.ODSTheme
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

/**
 * Reward Transaction Item Component
 * Displays a single reward transaction using ODSListRowStandard
 */
@Composable
fun RewardTransactionItem(
    transaction: RewardTransaction,
    scheme: ODSTheme,
    modifier: Modifier = Modifier
) {
    val dateFormatter = DateTimeFormatter.ofPattern("d MMM yyyy, h:mm a")
        .withZone(ZoneId.systemDefault())

    val formattedDate = try {
        val instant = Instant.parse(transaction.createdAt)
        dateFormatter.format(instant)
    } catch (e: Exception) {
        transaction.createdAt
    }

    // Status color based on transaction status
    val statusColor = when (transaction.status.uppercase()) {
        "DELIVERED" -> scheme.functionalSuccessStandard
        "PROCESSING" -> scheme.functionalWarningStandard
        "PENDING" -> scheme.basicTextRecessive
        "CANCELLED" -> scheme.functionalDestructiveStandard
        else -> scheme.basicText
    }

    // Build description text with transaction number and date
    val descriptionText = buildString {
        append("${transaction.transactionNumber} • $formattedDate")
        if (transaction.trackingNumber != null) {
            append(" • Tracking: ${transaction.trackingNumber}")
        }
    }

    ODSRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        ODSListRowStandard(
            modifier = Modifier.weight(1f),
            scheme = scheme,
            props = ODSListRowStandardProps(
                variant = ODSListRowStandardVariant.STANDARD,
                labelText = transaction.rewardTitle,
                descriptionText = descriptionText,
                showDescriptionTitle = false
            )
        )

        // Coin price and status on the right side
        ODSColumn(
            horizontalAlignment = Alignment.End,
            gap = DSVariables.spacingComponent1
        ) {
            ODSText(
                text = "-${transaction.coinPrice} coins",
                style = DSTextStyles.bodyMBold,
                color = scheme.functionalDestructiveStandard
            )
            ODSText(
                text = transaction.status,
                style = DSTextStyles.bodySRegular,
                color = statusColor
            )
        }
    }
}

