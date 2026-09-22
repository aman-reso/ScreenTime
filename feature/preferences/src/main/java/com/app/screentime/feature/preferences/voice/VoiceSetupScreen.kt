package com.app.screentime.feature.preferences.voice

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.app.screentime.core.ui.theme.zonaODSTheme
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSLazyColumn
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSLinearGradientModel
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.tokens.ODSVariables
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * Voice-first onboarding for the first six dating-profile questions.
 * Built as reusable ODS pieces so other flows can share the dock, cards, and progress.
 */
@Composable
fun VoiceSetupScreen(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = zonaODSTheme,
    onBack: () -> Unit = {},
    onComplete: () -> Unit = {}
) {
    val questions = voiceSetupQuestions
    val context = LocalContext.current
    var questionIndex by remember { mutableIntStateOf(0) }
    var isKeyboardMode by remember { mutableStateOf(false) }
    var isListening by remember { mutableStateOf(false) }
    var hearingText by remember { mutableStateOf("") }
    var statusMessage by remember { mutableStateOf<String?>(null) }
    val answers = remember {
        mutableStateMapOf<VoiceSetupField, String>()
    }

    val question = questions[questionIndex]
    val captured = questions.take(questionIndex).mapNotNull { item ->
        val value = answers[item.field]?.takeIf { it.isNotBlank() } ?: return@mapNotNull null
        Triple(item, value, questions.indexOf(item))
    }

    val dictation = remember {
        VoiceDictationClient(
            context = context,
            onPartial = { spoken ->
                hearingText = spoken
            },
            onFinal = { spoken ->
                hearingText = spoken
                isListening = false
            },
            onError = { message ->
                statusMessage = message
                isListening = false
            }
        )
    }

    DisposableEffect(Unit) {
        onDispose { dictation.destroy() }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            statusMessage = null
            isListening = true
            dictation.start()
        } else {
            statusMessage = "Microphone permission is needed for voice setup."
            isKeyboardMode = true
        }
    }

    fun stopListening() {
        isListening = false
        dictation.stop()
    }

    fun startListening() {
        isKeyboardMode = false
        val granted = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED
        if (!granted) {
            permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
            return
        }
        statusMessage = null
        isListening = true
        dictation.start()
    }

    fun commitCurrentAnswer(): Boolean {
        val value = hearingText.trim()
        if (value.isBlank()) {
            statusMessage = "Add an answer with your voice or the keyboard first."
            return false
        }
        answers[question.field] = question.normalizeSpoken(value)
        hearingText = ""
        statusMessage = null
        stopListening()
        return true
    }

    fun goToQuestion(index: Int) {
        stopListening()
        questionIndex = index.coerceIn(0, questions.lastIndex)
        hearingText = answers[questions[questionIndex].field].orEmpty()
        statusMessage = null
    }

    fun confirmAndAdvance() {
        if (!commitCurrentAnswer()) return
        if (questionIndex >= questions.lastIndex) {
            onComplete()
        } else {
            goToQuestion(questionIndex + 1)
        }
    }

    ODSColumn(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding(),
        verticalArrangement = Arrangement.SpaceBetween,
        background = listOf(
            ODSColorModel(
                gradient = ODSLinearGradientModel(
                    colorStops = arrayOf(
                        0.00f to scheme.basicBackground,
                        0.52f to scheme.basicBackgroundSubtle,
                        1.00f to scheme.basicBackgroundCard
                    ),
                    opacity = 1.00f,
                    angleInDegrees = 180f
                )
            )
        )
    ) {
        VoiceSetupTopBar(
            title = "Voice Setup",
            keyboardLabel = if (isKeyboardMode) "Use Voice" else "Use Keyboard",
            onBack = {
                if (questionIndex > 0) goToQuestion(questionIndex - 1) else onBack()
            },
            onKeyboardClick = {
                stopListening()
                isKeyboardMode = !isKeyboardMode
            },
            scheme = scheme
        )

        VoiceSetupProgress(
            currentQuestion = questionIndex + 1,
            totalQuestions = questions.size,
            topic = question.topic,
            scheme = scheme
        )

        ODSLazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            padding = ODSPadding(
                horizontal = ODSVariables.spacingLayout1,
                top = ODSVariables.spacingComponent4,
                bottom = ODSVariables.spacingComponent5
            ),
            gap = ODSVariables.spacingLayout1
        ) {
            item {
                VoiceAssistantHeader(
                    name = "Zona Assistant",
                    isActive = isListening,
                    scheme = scheme
                )
            }
            item {
                VoicePromptCard(
                    prompt = question.prompt,
                    scheme = scheme
                )
            }
            item {
                VoiceHearingCard(
                    transcript = hearingText,
                    placeholder = statusMessage ?: question.placeholder,
                    isKeyboardMode = isKeyboardMode,
                    isListening = isListening,
                    scheme = scheme,
                    onTranscriptChange = {
                        hearingText = it
                        statusMessage = null
                    },
                    onKeyboardDone = { confirmAndAdvance() }
                )
            }
            if (captured.isNotEmpty()) {
                item {
                    VoiceCapturedList(
                        items = captured.map { (item, value, _) ->
                            Triple(item.capturedLabel, value, item.capturedIcon)
                        },
                        onItemClick = { capturedIndex ->
                            goToQuestion(captured[capturedIndex].third)
                        },
                        scheme = scheme,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }

        VoiceSetupDock(
            isListening = isListening,
            onPrevious = {
                if (questionIndex > 0) goToQuestion(questionIndex - 1) else onBack()
            },
            onMicClick = {
                if (isListening) stopListening() else startListening()
            },
            onConfirm = { confirmAndAdvance() },
            scheme = scheme
        )
    }
}
