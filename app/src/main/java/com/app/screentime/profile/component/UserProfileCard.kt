package com.app.screentime.profile.component

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.app.screentime.network.model.UserProfile
import com.app.screentime.ui.atom.AppIcon
import com.app.screentime.ui.atom.AppText
import com.app.screentime.ui.atom.AppTextStyle
import com.app.screentime.ui.theme.borderColor
import com.app.screentime.ui.theme.lightTextColor

@Composable
fun UserProfileCard(
    username: String? = null,
    userId: String? = null,
    onUsernameClick: (() -> Unit)? = null
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Spacer(modifier = Modifier.height(24.dp))
        Box(
            modifier = Modifier
                .size(100.dp)
                .border(3.dp, color = borderColor, shape = CircleShape)
                .align(Alignment.CenterHorizontally)
        ) {
            Icon(
                imageVector = Icons.Outlined.Person,
                contentDescription = "Avatar",
                modifier = Modifier
                    .padding(8.dp)
                    .matchParentSize()
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        val displayName = username ?: userId ?: "Random Generated"
        AppText(
            text = displayName,
            style = AppTextStyle.SubTitle,
            modifier = Modifier
                .fillMaxWidth()
                .then(
                    if (onUsernameClick != null && displayName != "Random Generated") {
                        Modifier.clickable { onUsernameClick() }
                    } else {
                        Modifier
                    }
                ),
            textAlign = TextAlign.Center
        )
        AppText(
            text = "Joined On Nov 10, 2024",
            style = AppTextStyle.Label,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            color = lightTextColor
        )
        Spacer(modifier = Modifier.height(24.dp))
    }
}