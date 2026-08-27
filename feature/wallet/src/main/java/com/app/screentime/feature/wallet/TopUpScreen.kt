package com.app.screentime.feature.wallet

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme

@Composable
fun TopUpScreen(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    onBackClick: () -> Unit = {},
    onTopUpSuccess: () -> Unit = {},
    viewModel: WalletViewModel = hiltViewModel()
) {
    WalletPacksBottomSheet(
        onDismissRequest = onBackClick,
        modifier = modifier,
        scheme = scheme,
        onRechargeSuccess = onTopUpSuccess,
        viewModel = viewModel
    )
}
