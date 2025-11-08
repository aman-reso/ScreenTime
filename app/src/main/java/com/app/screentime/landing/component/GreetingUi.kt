package com.app.screentime.landing.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.stringResource
import com.app.screentime.R
import com.app.screentime.ui.atom.AppText
import com.app.screentime.ui.atom.AppTextStyle
import java.util.Calendar

@Preview(showBackground = true)
@Composable
fun GreetingUi(username: String? = null) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column {
            AppText(text = getGreetingBasedOnTime())
            Spacer(modifier = Modifier.height(4.dp))
            AppText(
                text = username ?: "User",
                style = AppTextStyle.SubTitle
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Icon(
            imageVector = Icons.Default.Notifications,
            contentDescription = "Notification",
            modifier = Modifier
                .size(22.dp)
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
