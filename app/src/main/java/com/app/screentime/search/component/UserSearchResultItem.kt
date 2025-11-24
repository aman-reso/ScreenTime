package com.app.screentime.search.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.app.screentime.network.model.UserSearchResult
import com.app.screentime.ui.atom.AppText
import com.app.screentime.ui.atom.AppTextStyle
import com.app.screentime.ui.theme.LocalAppColors

@Composable
fun UserSearchResultItem(
    user: UserSearchResult,
    onClick: () -> Unit = {}
) {
    val colors = LocalAppColors.current ?: return
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = !user.username.isNullOrBlank(), onClick = onClick)
    ) {
        AppText(
            text = user.username ?: "Unknown User",
            style = AppTextStyle.Body,
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 16.dp)
        )
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            thickness = 1.dp,
            color = colors.border
        )
    }
}
