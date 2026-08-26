package com.app.screentime.feature.auth

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.screentime.core.ui.components.*
import com.telekom.odsystem.atoms.*
import com.telekom.odsystem.atoms.button.ODSButton
import com.telekom.odsystem.atoms.button.ODSButtonProps
import com.telekom.odsystem.atoms.button.ODSButtonSize
import com.telekom.odsystem.atoms.button.ODSButtonVariant
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.atoms.textfield.ODSTextField
import com.telekom.odsystem.atoms.textfield.ODSTextFieldProps
import com.telekom.odsystem.foundations.*
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.ODSTextStyles
import com.telekom.odsystem.tokens.tokens.ODSTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthGateScreen(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    onAuthSuccess: () -> Unit = {},
    viewModel: AuthViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showAuthSheet by remember { mutableStateOf(false) }
    var isRegisterMode by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) onAuthSuccess()
    }

    ODSBox(
        modifier = modifier.fillMaxSize(),
        background = listOf(ODSColorModel(hexColor = scheme.basicBackground))
    ) {
        ODSColumn(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .statusBarsPadding()
                .navigationBarsPadding()
        ) {
            // 1. Top Bar
            EvermoreTopBar(
                title = "EVERM♥RE",
                scheme = scheme,
                onMenuClick = { showAuthSheet = true }
            )

            Spacer(modifier = Modifier.height(12.dp))

            // 2. Tagline & Main Pompiere Title
            ODSColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                gap = 8.dp
            ) {
                ODSText(
                    text = "Find Your Kind of Connection",
                    style = ODSTextStyles.bodyMBold,
                    color = scheme.basicTextRecessive
                )

                PompiereTitle(
                    text = "Meet new people.\nMake real connections.",
                    scheme = scheme,
                    style = ODSTextStyles.pompiereDisplay,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(14.dp))

                // 3. CTA Buttons (ODS Buttons with label and onClick callback)
                ODSRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally)
                ) {
                    ODSButton(
                        modifier = Modifier.weight(1f),
                        scheme = scheme,
                        props = ODSButtonProps(
                            label = "Get Started",
                            variant = ODSButtonVariant.PRIMARY,
                            size = ODSButtonSize.SMALL
                        ),
                        onClick = {
                            isRegisterMode = true
                            showAuthSheet = true
                        }
                    )
                    ODSButton(
                        modifier = Modifier.weight(1f),
                        scheme = scheme,
                        props = ODSButtonProps(
                            label = "Sign In",
                            variant = ODSButtonVariant.SECONDARY,
                            size = ODSButtonSize.SMALL
                        ),
                        onClick = {
                            isRegisterMode = false
                            showAuthSheet = true
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // 4. Organic Blob Illustration
            OrganicBlobIllustration(scheme = scheme)

            Spacer(modifier = Modifier.height(28.dp))

            // 5. Feature cards using ODS rows/columns & scheme tokens
            ODSColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                gap = 14.dp
            ) {
                ODSRow(
                    modifier = Modifier.fillMaxWidth(),
                    gap = 12.dp
                ) {
                    FeatureGridCard(
                        icon = Icons.Outlined.CheckCircle,
                        title = "Smart Matching",
                        description = "We connect you with people who match your true vibe.",
                        scheme = scheme,
                        modifier = Modifier.weight(1f)
                    )
                    FeatureGridCard(
                        icon = Icons.Filled.Phone,
                        title = "Real Audio",
                        description = "Instant crystal clear voice calls with no numbers shared.",
                        scheme = scheme,
                        modifier = Modifier.weight(1f)
                    )
                }

                ODSRow(
                    modifier = Modifier.fillMaxWidth(),
                    gap = 12.dp
                ) {
                    FeatureGridCard(
                        icon = Icons.Filled.Lock,
                        title = "Safe & Secure",
                        description = "Your privacy matters. Zero audio stored on server.",
                        scheme = scheme,
                        modifier = Modifier.weight(1f)
                    )
                    FeatureGridCard(
                        icon = Icons.Filled.Person,
                        title = "Live Models",
                        description = "Talk with genuine, verified model companions anytime.",
                        scheme = scheme,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Footer note
            ODSText(
                text = "Safe, simple, and authentic · Chatty",
                style = ODSTextStyles.microcopyRegular,
                color = scheme.basicTextRecessive,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
            )
        }

        // 6. Bottom Sheet for Login / Register
        if (showAuthSheet) {
            ModalBottomSheet(
                onDismissRequest = { showAuthSheet = false },
                containerColor = scheme.basicBackgroundCard.getColor(),
                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
            ) {
                AuthBottomSheetContent(
                    scheme = scheme,
                    isRegisterMode = isRegisterMode,
                    uiState = uiState,
                    onPhoneChange = { viewModel.onPhoneChanged(it) },
                    onNameChange = { viewModel.onNameChanged(it) },
                    onRoleChange = { viewModel.onRoleSelected(it) },
                    onSubmit = { viewModel.submitAuth() },
                    onToggleMode = { isRegisterMode = !isRegisterMode }
                )
            }
        }
    }
}

@Composable
private fun AuthBottomSheetContent(
    scheme: ODSTheme,
    isRegisterMode: Boolean,
    uiState: AuthUiState,
    onPhoneChange: (String) -> Unit,
    onNameChange: (String) -> Unit,
    onRoleChange: (String) -> Unit,
    onSubmit: () -> Unit,
    onToggleMode: () -> Unit
) {
    ODSColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .padding(bottom = 36.dp),
        gap = 16.dp
    ) {
        ODSRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            PompiereTitle(
                text = if (isRegisterMode) "Join Evermore" else "Welcome Back",
                scheme = scheme,
                style = ODSTextStyles.pompiereTitleL
            )
            ODSText(
                text = if (isRegisterMode) "Sign In" else "Register",
                style = ODSTextStyles.bodyMBold,
                color = scheme.basicText,
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .clickable(onClick = onToggleMode)
                    .padding(8.dp)
            )
        }

        ODSText(
            text = if (isRegisterMode)
                "Create your account to start meeting people"
            else
                "Enter your phone number to continue",
            style = ODSTextStyles.bodySRegular,
            color = scheme.basicTextRecessive
        )

        ODSTextField(
            scheme = scheme,
            props = ODSTextFieldProps(
                label = "Phone Number",
                placeholderText = "e.g. 9876543210",
                inputText = uiState.phone,
                leftIcon = ODSIconModel(imageVector = Icons.Filled.Phone)
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            onValueChange = onPhoneChange
        )

        if (isRegisterMode) {
            ODSTextField(
                scheme = scheme,
                props = ODSTextFieldProps(
                    label = "Your Name",
                    placeholderText = "e.g. Aman",
                    inputText = uiState.name,
                    leftIcon = ODSIconModel(imageVector = Icons.Filled.Person)
                ),
                onValueChange = onNameChange
            )
        }

        if (isRegisterMode) {
            ODSText(
                text = "Join as:",
                style = ODSTextStyles.bodySRegular,
                color = scheme.basicTextRecessive
            )
            val isModel = uiState.role == "model"
            ODSRow(gap = 10.dp) {
                ODSBox(
                    modifier = Modifier
                        .clickable { onRoleChange("user") },
                    background = listOf(
                        ODSColorModel(
                            hexColor = if (!isModel) scheme.basicAccent else scheme.basicBackgroundSubtle
                        )
                    ),
                    cornerRadius = ODSCorners(all = 12.dp),
                    padding = ODSPadding(horizontal = 16.dp, vertical = 10.dp)
                ) {
                    ODSText(
                        text = "👤  User",
                        style = ODSTextStyles.bodySBold,
                        color = if (!isModel) scheme.basicTextOnAccent else scheme.basicText
                    )
                }
                ODSBox(
                    modifier = Modifier
                        .clickable { onRoleChange("model") },
                    background = listOf(
                        ODSColorModel(
                            hexColor = if (isModel) scheme.basicAccent else scheme.basicBackgroundSubtle
                        )
                    ),
                    cornerRadius = ODSCorners(all = 12.dp),
                    padding = ODSPadding(horizontal = 16.dp, vertical = 10.dp)
                ) {
                    ODSText(
                        text = "✦  Model (Girls)",
                        style = ODSTextStyles.bodySBold,
                        color = if (isModel) scheme.basicTextOnAccent else scheme.basicText
                    )
                }
            }
        }

        if (!uiState.error.isNullOrEmpty()) {
            ODSText(
                text = uiState.error,
                style = ODSTextStyles.bodySRegular,
                color = scheme.functionalDestructiveStandard
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        ODSButton(
            modifier = Modifier.fillMaxWidth(),
            scheme = scheme,
            props = ODSButtonProps(
                label = if (uiState.isLoading) "Processing..." else if (isRegisterMode) "Create Account" else "Continue",
                variant = ODSButtonVariant.PRIMARY,
                size = ODSButtonSize.SMALL
            ),
            onClick = onSubmit
        )
    }
}
