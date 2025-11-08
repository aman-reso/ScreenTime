package com.app.screentime.profile.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ShowChart
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.app.screentime.R
import com.app.screentime.profile.model.ProfileSettingsUi
import com.app.screentime.ui.atom.AppText
import com.app.screentime.ui.atom.AppGlassyCard
import com.app.screentime.ui.theme.TitleTextColor
import com.app.screentime.ui.theme.lightTextColor

@Composable
fun SettingsItemCard(data: ProfileSettingsUi, onClick: () -> Unit = {}) {
    AppGlassyCard(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .clickable {
                    onClick.invoke()
                }
                .padding(horizontal = 16.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ShowChart,
                    contentDescription = null,
                    tint = TitleTextColor,
                    modifier = Modifier
                        .height(21.dp)
                        .width(18.dp)
                )

                Spacer(Modifier.width(14.dp))

                AppText(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    text = data.text,
                    color = TitleTextColor,
                    fontWeight = FontWeight.Bold
                )

                Icon(
                    painter = painterResource(R.drawable.chevron_right),
                    contentDescription = null,
                    tint = lightTextColor,
                    modifier = Modifier
                        .width(10.dp)
                        .height(16.dp)
                )
            }
        }
    }
}