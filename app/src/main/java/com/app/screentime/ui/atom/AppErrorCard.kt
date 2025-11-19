package com.app.screentime.ui.atom

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.stringResource
import com.app.screentime.R
import com.app.screentime.ui.theme.LocalAppColors

@Preview(showBackground = true)
@Composable
fun AppErrorCard(
    modifier: Modifier = Modifier,
    text: String? = "Something went wrong",
    subTitleText: String? = "Something went wrong please try again",
    callback: () -> Unit = {}
) {
    val colors = LocalAppColors.current ?: return
    Box(
        modifier
            .background(color = colors.background)
            .padding(horizontal = 16.dp, vertical = 16.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .wrapContentWidth()
                    .wrapContentHeight()
                    .border(width = 1.dp, color = colors.error, shape = CircleShape)
                    .align(Alignment.CenterHorizontally)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Close,
                    contentDescription = "",
                    tint = colors.error,
                    modifier = Modifier
                        .size(30.dp)
                        .padding(4.dp)

                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            text?.let {
                AppText(
                    text = it,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                    style = AppTextStyle.SubTitle,
                    color = colors.error
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            subTitleText?.let {
                AppText(
                    text = it,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                    style = AppTextStyle.Label,
                    color = colors.textLight
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            AppSecondaryButton(
                text = stringResource(R.string.try_again),
                modifier = Modifier
                    .wrapContentWidth()
                    .align(Alignment.CenterHorizontally)
            ) {

            }
        }
    }
}