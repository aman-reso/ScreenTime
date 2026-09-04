package com.app.screentime.feature.discover

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.screentime.core.model.ModelProfile
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme

@Composable
fun ModelProfileScreen(
    modelId: String,
    modelName: String = "Model",
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    viewModel: ModelProfileViewModel = hiltViewModel(),
    onBackClick: () -> Unit = {},
    onStartChat: (String, String) -> Unit = { _, _ -> },
    onStartVoiceCall: (String, String) -> Unit = { _, _ -> },
    onStartVideoCall: (String, String) -> Unit = { _, _ -> }
) {
    LaunchedEffect(modelId) {
        viewModel.loadModel(modelId, modelName)
    }

    val uiState by viewModel.uiState.collectAsState()
    val model = uiState.model ?: ModelProfile(id = modelId, name = modelName)

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(scheme.basicBackground.getColor())
    ) {
        ODSColumn(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            background = listOf(ODSColorModel(hexColor = scheme.basicBackground))
        ) {
            // 1. Hero Image Header (Full width, bottom tags, dual gradient scrims)
            ModelProfileHeader(
                model = model,
                scheme = scheme
            )

            Spacer(Modifier.height(12.dp))

            // 2. Profile Details & Rates (Name, Bio, Call/Chat Rate pills, Tags)
            ModelProfileInfoSection(
                model = model,
                scheme = scheme
            )

            Spacer(Modifier.height(110.dp))
        }

        // 3. Floating Top Bar (Back & Favorite buttons, securely below status bar)
        ModelProfileTopBar(
            isFavorite = uiState.isFavorite,
            scheme = scheme,
            onBackClick = onBackClick,
            onFavoriteToggle = { viewModel.toggleFavorite() },
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        )

        // 4. Floating Bottom Actions (Chat, Video Call, and Voice Call Buttons)
        ModelProfileBottomBar(
            scheme = scheme,
            onStartChat = { onStartChat(model.id, model.name) },
            onStartVoiceCall = { onStartVoiceCall(model.id, model.name) },
            onStartVideoCall = { onStartVideoCall(model.id, model.name) },
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}
