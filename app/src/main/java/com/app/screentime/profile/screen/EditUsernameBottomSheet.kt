package com.app.screentime.profile.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.screentime.R
import com.app.screentime.profile.viewmodel.ProfileViewModel
import com.app.screentime.search.component.GlassSearchBar
import com.app.screentime.ui.atom.AppPrimaryButton
import com.app.screentime.ui.atom.AppSecondaryButton
import com.app.screentime.ui.atom.AppText
import com.app.screentime.ui.atom.AppTextStyle
import com.app.screentime.ui.atom.glassBottomSheetBackground
import com.app.screentime.ui.theme.LocalAppColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditUsernameBottomSheetContent(
    currentUsername: String?,
    onDismiss: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val colors = LocalAppColors.current ?: return
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)
    var usernameText by remember { mutableStateOf(currentUsername ?: "") }
    val internalState = viewModel.getInternalState()
    val isUpdating = internalState.isUpdating
    val error = internalState.error

    ModalBottomSheet(
        containerColor = colors.background,
        sheetState = sheetState,
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .glassBottomSheetBackground()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                AppText(
                    text = stringResource(R.string.edit_username),
                    style = AppTextStyle.SubTitle
                )
                IconButton(onClick = onDismiss) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = stringResource(R.string.close),
                        tint = colors.tint
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Username input
            AppText(
                text = stringResource(R.string.username),
                style = AppTextStyle.Label,
                color = colors.textSecondary
            )
            Spacer(modifier = Modifier.height(8.dp))
            GlassSearchBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                query = usernameText,
                onQueryChange = { usernameText = it },
                placeholder = stringResource(R.string.enter_username),
                enabled = !isUpdating
            )

            // Error message
            if (error != null) {
                Spacer(modifier = Modifier.height(8.dp))
                AppText(
                    text = error,
                    style = AppTextStyle.Label,
                    color = colors.error
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Save button
            AppPrimaryButton(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.save),
                enabled = !isUpdating && usernameText.isNotBlank() && usernameText != currentUsername,
                onClick = {
                    viewModel.updateUsername(usernameText.trim()) {
                        // On success, dismiss the bottom sheet
                        onDismiss()
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

