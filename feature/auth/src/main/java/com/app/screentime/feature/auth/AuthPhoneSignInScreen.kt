package com.app.screentime.feature.auth

import android.app.Activity
import android.content.Context
import android.graphics.Paint
import android.telephony.TelephonyManager
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import kotlinx.coroutines.delay
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.app.screentime.core.ui.theme.zonaODSTheme
import com.google.android.gms.auth.api.identity.GetPhoneNumberHintIntentRequest
import com.google.android.gms.auth.api.identity.Identity
import com.telekom.odsystem.R as ODSR
import com.telekom.odsystem.atoms.ODSBorder
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.ODSLazyColumn
import com.telekom.odsystem.atoms.button.ODSButton
import com.telekom.odsystem.atoms.button.ODSButtonProps
import com.telekom.odsystem.atoms.button.ODSButtonSize
import com.telekom.odsystem.atoms.button.ODSButtonVariant
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.atoms.textfield.ODSTextField
import com.telekom.odsystem.atoms.textfield.ODSTextFieldMode
import com.telekom.odsystem.atoms.textfield.ODSTextFieldProps
import com.telekom.odsystem.atoms.textfield.ODSTextFieldSize
import com.telekom.odsystem.atoms.textfield.ODSTextFieldSupportMessageProps
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.foundations.ODSTextStyle
import com.telekom.odsystem.molecules.bottomsheet.ODSBottomSheet
import com.telekom.odsystem.molecules.bottomsheet.ODSBottomSheetProps
import com.telekom.odsystem.molecules.codeinput.ODSCodeInput
import com.telekom.odsystem.molecules.codeinput.ODSCodeInputMode
import com.telekom.odsystem.molecules.codeinput.ODSCodeInputProps
import com.telekom.odsystem.molecules.codeinput.ODSInputItemModel
import com.telekom.odsystem.molecules.dialog.ODSDialog
import com.telekom.odsystem.molecules.dialog.ODSDialogProps
import com.telekom.odsystem.slots.bottomsheetheader.ODSBottomSheetHeader
import com.telekom.odsystem.slots.bottomsheetheader.ODSBottomSheetHeaderProps
import com.telekom.odsystem.slots.bottomsheetheader.ODSBottomSheetHeaderSize
import com.telekom.odsystem.tokens.ODSTextStyles
import com.telekom.odsystem.tokens.ODSVariables
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * Winter Phone Sign-In Screen with OTP Verification Step & Google Phone Number Hint API.
 * 100% constructed with Telekom ODS components (ODSTextField, ODSButton) conforming to Figma (node-id 70-6).
 */
@Composable
fun AuthPhoneSignInScreen(
    uiState: AuthUiState,
    onPhoneChange: (String) -> Unit,
    onCountryCodeChange: (String, String) -> Unit = { _, _ -> },
    onOtpChange: (String) -> Unit = {},
    onContinueClick: (String) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    scheme: ODSTheme = zonaODSTheme
) {
    BackHandler(onBack = onBackClick)

    val context = LocalContext.current
    val focusRequester = remember { FocusRequester() }

    val isOtpStep = uiState.step == AuthStep.OTP_INPUT

    val phoneNumberHintLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
        try {
            if (result.resultCode == Activity.RESULT_OK && result.data != null) {
                val rawPhone =
                    Identity.getSignInClient(context).getPhoneNumberFromIntent(result.data)
                if (rawPhone.isNotBlank()) {
                    val matchedCountry = SUPPORTED_COUNTRIES
                        .sortedByDescending { it.dialCode.length }
                        .firstOrNull { rawPhone.startsWith(it.dialCode) }
                    if (matchedCountry != null) {
                        onCountryCodeChange(matchedCountry.dialCode, matchedCountry.flag)
                        val nationalNumber =
                            rawPhone.removePrefix(matchedCountry.dialCode).filter { it.isDigit() }
                        onPhoneChange(nationalNumber)
                    } else {
                        val cleanDigits = rawPhone.filter { it.isDigit() }
                        if (cleanDigits.length in 6..15) {
                            onPhoneChange(cleanDigits)
                        }
                    }
                }
            }
        } catch (_: Exception) {
        }
    }

    fun requestGooglePhoneNumberHint() {
        try {
            val request = GetPhoneNumberHintIntentRequest.builder().build()
            Identity.getSignInClient(context)
                .getPhoneNumberHintIntent(request)
                .addOnSuccessListener { pendingIntent ->
                    try {
                        phoneNumberHintLauncher.launch(
                            IntentSenderRequest.Builder(pendingIntent.intentSender).build()
                        )
                    } catch (_: Exception) {
                    }
                }
                .addOnFailureListener {}
        } catch (_: Exception) {
        }
    }

    LaunchedEffect(uiState.step) {
        try {
            focusRequester.requestFocus()
            if (!isOtpStep && uiState.phone.isBlank()) {
                requestGooglePhoneNumberHint()
            }
        } catch (_: Exception) {
        }
    }

    ODSBox(
        modifier = modifier.fillMaxSize(),
        background = listOf(ODSColorModel(hexColor = scheme.basicBackground))
    ) {
        ODSColumn(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .imePadding()
                .padding(horizontal = 24.dp, vertical = 12.dp),
            verticalAlignment = Alignment.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ODSRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                ODSBox(
                    modifier = Modifier
                        .size(42.dp)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = onBackClick
                        ),
                    cornerRadius = ODSCorners(all = 21.dp),
                    border = ODSBorder(
                        width = 1.dp,
                        colorList = listOf(ODSColorModel(hexColor = scheme.basicStroke))
                    ),
                    contentAlignment = Alignment.Center
                ) {
                    ODSIcon(
                        iconModel = ODSIconModel(
                            drawableRes = ODSR.drawable.ic_arrow_left,
                            contentDescription = "Back"
                        ),
                        tint = scheme.basicText.getColor(),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Centered "Winter" Logo Title (Pacifico 36sp)
            ODSText(
                text = "Winter",
                style = ODSTextStyle(
                    fontFamily = ODSR.font.pacifico_regular,
                    fontSize = 36,
                    lineHeight = 36
                ),
                color = scheme.basicAccent,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            if (!isOtpStep) {
                EnterMobileNumberContent(
                    uiState = uiState,
                    focusRequester = focusRequester,
                    scheme = scheme,
                    onPhoneChange = onPhoneChange,
                    onCountryCodeChange = onCountryCodeChange,
                    onContinueClick = onContinueClick
                )
            } else {
                VerifyOtpContent(
                    uiState = uiState,
                    focusRequester = focusRequester,
                    scheme = scheme,
                    onOtpChange = onOtpChange,
                    onContinueClick = onContinueClick,
                    onBackClick = onBackClick
                )
            }
        }
    }
}

data class CountryCodeOption(
    val isoCode: String,
    val name: String,
    val dialCode: String,
    val flag: String
)

private val SUPPORTED_COUNTRIES = listOf(
    CountryCodeOption("IN", "India", "+91", "🇮🇳"),
    CountryCodeOption("US", "United States", "+1", "🇺🇸"),
    CountryCodeOption("GB", "United Kingdom", "+44", "🇬🇧"),
    CountryCodeOption("CA", "Canada", "+1", "🇨🇦"),
    CountryCodeOption("AU", "Australia", "+61", "🇦🇺"),
    CountryCodeOption("AE", "United Arab Emirates", "+971", "🇦🇪"),
    CountryCodeOption("SA", "Saudi Arabia", "+966", "🇸🇦"),
    CountryCodeOption("DE", "Germany", "+49", "🇩🇪"),
    CountryCodeOption("FR", "France", "+33", "🇫🇷"),
    CountryCodeOption("SG", "Singapore", "+65", "🇸🇬"),
    CountryCodeOption("MY", "Malaysia", "+60", "🇲🇾"),
    CountryCodeOption("QA", "Qatar", "+974", "🇶🇦"),
    CountryCodeOption("KW", "Kuwait", "+965", "🇰🇼"),
    CountryCodeOption("OM", "Oman", "+968", "🇴🇲"),
    CountryCodeOption("BH", "Bahrain", "+973", "🇧🇭"),
    CountryCodeOption("NP", "Nepal", "+977", "🇳🇵"),
    CountryCodeOption("BD", "Bangladesh", "+880", "🇧🇩"),
    CountryCodeOption("PK", "Pakistan", "+92", "🇵🇰"),
    CountryCodeOption("LK", "Sri Lanka", "+94", "🇱🇰"),
    CountryCodeOption("ZA", "South Africa", "+27", "🇿🇦"),
    CountryCodeOption("NG", "Nigeria", "+234", "🇳🇬"),
    CountryCodeOption("KE", "Kenya", "+254", "🇰🇪"),
    CountryCodeOption("BR", "Brazil", "+55", "🇧🇷"),
    CountryCodeOption("JP", "Japan", "+81", "🇯🇵"),
    CountryCodeOption("KR", "South Korea", "+82", "🇰🇷"),
    CountryCodeOption("ID", "Indonesia", "+62", "🇮🇩"),
    CountryCodeOption("PH", "Philippines", "+63", "🇵🇭"),
    CountryCodeOption("TH", "Thailand", "+66", "🇹🇭"),
    CountryCodeOption("VN", "Vietnam", "+84", "🇻🇳"),
    CountryCodeOption("TR", "Turkey", "+90", "🇹🇷"),
    CountryCodeOption("IT", "Italy", "+39", "🇮🇹"),
    CountryCodeOption("ES", "Spain", "+34", "🇪🇸"),
    CountryCodeOption("NL", "Netherlands", "+31", "🇳🇱"),
    CountryCodeOption("CH", "Switzerland", "+41", "🇨🇭"),
    CountryCodeOption("SE", "Sweden", "+46", "🇸🇪"),
    CountryCodeOption("NO", "Norway", "+47", "🇳🇴"),
    CountryCodeOption("DK", "Denmark", "+45", "🇩🇰"),
    CountryCodeOption("IE", "Ireland", "+353", "🇮🇪"),
    CountryCodeOption("NZ", "New Zealand", "+64", "🇳🇿"),
    CountryCodeOption("RU", "Russia", "+7", "🇷🇺"),
    CountryCodeOption("CN", "China", "+86", "🇨🇳"),
    CountryCodeOption("MX", "Mexico", "+52", "🇲🇽"),
    CountryCodeOption("AR", "Argentina", "+54", "🇦🇷")
)

/**
 * Detect user's current country based on SIM, Telephony network, or device Locale.
 */
fun detectDeviceCountry(context: Context): CountryCodeOption {
    try {
        val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as? TelephonyManager
        val simCountry = tm?.simCountryIso?.uppercase()?.trim()
        if (!simCountry.isNullOrBlank()) {
            val matched =
                SUPPORTED_COUNTRIES.find { it.isoCode.equals(simCountry, ignoreCase = true) }
            if (matched != null) return matched
        }
        val networkCountry = tm?.networkCountryIso?.uppercase()?.trim()
        if (!networkCountry.isNullOrBlank()) {
            val matched =
                SUPPORTED_COUNTRIES.find { it.isoCode.equals(networkCountry, ignoreCase = true) }
            if (matched != null) return matched
        }
    } catch (_: Exception) {
    }

    val localeCountry = try {
        val configurationLocales = context.resources.configuration.locales
        if (!configurationLocales.isEmpty) {
            configurationLocales.get(0)?.country?.uppercase()?.trim()
        } else {
            java.util.Locale.getDefault().country.uppercase().trim()
        }
    } catch (_: Exception) {
        java.util.Locale.getDefault().country.uppercase().trim()
    }

    if (!localeCountry.isNullOrBlank()) {
        val matched =
            SUPPORTED_COUNTRIES.find { it.isoCode.equals(localeCountry, ignoreCase = true) }
        if (matched != null) return matched
    }

    return SUPPORTED_COUNTRIES.first()
}

/**
 * Separate Component for Entering Mobile Number with Country Code & Confirmation Dialog.
 */
@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
private fun EnterMobileNumberContent(
    uiState: AuthUiState,
    focusRequester: FocusRequester,
    scheme: ODSTheme,
    onPhoneChange: (String) -> Unit,
    onCountryCodeChange: (String, String) -> Unit,
    onContinueClick: (String) -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val context = LocalContext.current
    val cleanDigits = remember(uiState.phone) {
        uiState.phone.filter { it.isDigit() }
    }
    val isPhoneValid = remember(cleanDigits) {
        cleanDigits.length in 6..15
    }

    var hasAutoDetectedLocale by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        if (!hasAutoDetectedLocale) {
            hasAutoDetectedLocale = true
            val detected = detectDeviceCountry(context)
            onCountryCodeChange(detected.dialCode, detected.flag)
        }
    }

    var showCountryPicker by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }

    ODSColumn(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ODSText(
            modifier = Modifier.fillMaxWidth(),
            text = "What's your number?",
            style = ODSTextStyle(
                fontFamily = ODSR.font.funnelsans_bold,
                fontSize = 26,
                lineHeight = 26
            ),
            color = scheme.basicText,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Subtitle
        ODSText(
            modifier = Modifier.fillMaxWidth(),
            text = "We'll send you an OTP on WhatsApp",
            style = ODSTextStyle(
                fontFamily = ODSR.font.funnelsans_medium,
                fontSize = 14,
                lineHeight = 14
            ),
            color = scheme.basicTextRecessive,
            textAlign = TextAlign.Center
        )

        // 32dp margin before mobile number box
        Spacer(modifier = Modifier.height(32.dp))

        // Mobile Number Row with Country Code Selector
        ODSRow(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            gap = 8.dp
        ) {
            // Country Code selector chip
            ODSBox(
                modifier = Modifier
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = { showCountryPicker = true }
                    ),
                border = ODSBorder(
                    width = 1.dp,
                    colorList = listOf(ODSColorModel(hexColor = scheme.basicStroke))
                ),
                cornerRadius = ODSCorners(all = 12.dp),
                background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard)),
                padding = ODSPadding(horizontal = 12.dp, vertical = 16.dp)
            ) {
                ODSRow(
                    verticalAlignment = Alignment.CenterVertically,
                    gap = 6.dp
                ) {
                    ODSText(
                        text = "${uiState.countryFlag} ${uiState.countryCode}",
                        style = ODSTextStyles.bodyMBold,
                        color = scheme.basicText
                    )
                    ODSIcon(
                        iconModel = ODSIconModel(
                            drawableRes = ODSR.drawable.ic_chevron_down,
                            contentDescription = "Select Country"
                        ),
                        tint = scheme.basicTextRecessive.getColor(),
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            ODSTextField(
                modifier = Modifier
                    .weight(1f)
                    .focusRequester(focusRequester),
                scheme = scheme,
                props = ODSTextFieldProps(
                    placeholderText = "Phone number",
                    inputText = uiState.phone,
                    mode = if (!uiState.error.isNullOrBlank()) {
                        ODSTextFieldMode.ERROR
                    } else {
                        ODSTextFieldMode.STANDARD
                    },
                    supportMessageProps = if (!uiState.error.isNullOrBlank()) {
                        ODSTextFieldSupportMessageProps(message = uiState.error)
                    } else null,
                    size = ODSTextFieldSize.SMALL
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Phone,
                    imeAction = if (isPhoneValid) ImeAction.Done else ImeAction.None
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        if (isPhoneValid && !uiState.isLoading) {
                            keyboardController?.hide()
                            val phoneWithCode = "${uiState.countryCode}${cleanDigits}"
                            onContinueClick(phoneWithCode)
                        }
                    }
                ),
                onValueChange = { input ->
                    val clean = input.filter { it.isDigit() }.take(15)
                    onPhoneChange(clean)
                }
            )
        }

        // 32dp margin before Continue button
        Spacer(modifier = Modifier.height(32.dp))

        // ODS Continue Button
        ODSButton(
            modifier = Modifier.fillMaxWidth(),
            scheme = scheme,
            props = ODSButtonProps(
                label = if (uiState.isLoading) "Sending code..." else "Continue",
                variant = ODSButtonVariant.SECONDARY,
                disabled = !isPhoneValid || uiState.isLoading
            ),
            onClick = {
                keyboardController?.hide()
                val phoneWithCode = "${uiState.countryCode}${cleanDigits}"
                onContinueClick(phoneWithCode)
            }
        )
    }

    // ── Country Picker Bottom Sheet ─────────────────────────────────────────
    if (showCountryPicker) {
        ODSBottomSheet(
            scheme = scheme,
            showBottomSheet = true,
            props = ODSBottomSheetProps(showHandle = true),
            onDismissRequest = {
                showCountryPicker = false
                searchQuery = ""
            },
            onCloseClicked = {
                showCountryPicker = false
                searchQuery = ""
            },
            titleSlot = {
                ODSBottomSheetHeader(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 40.dp),
                    scheme = scheme,
                    props = ODSBottomSheetHeaderProps(
                        smallHeading = "Select Country",
                        size = ODSBottomSheetHeaderSize.SMALL
                    )
                )
            },
            contentSlot = {
                val screenHeight = LocalConfiguration.current.screenHeightDp.dp
                ODSColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(screenHeight * 0.88f)
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    gap = 12.dp
                ) {
                    ODSTextField(
                        modifier = Modifier.fillMaxWidth(),
                        scheme = scheme,
                        props = ODSTextFieldProps(
                            label = "Search Country or Code",
                            inputText = searchQuery,
                            size = ODSTextFieldSize.SMALL
                        ),
                        onValueChange = { searchQuery = it }
                    )

                    val filteredCountries = remember(searchQuery) {
                        if (searchQuery.isBlank()) SUPPORTED_COUNTRIES
                        else SUPPORTED_COUNTRIES.filter {
                            it.name.contains(searchQuery, ignoreCase = true) ||
                                    it.dialCode.contains(searchQuery) ||
                                    it.isoCode.contains(searchQuery, ignoreCase = true)
                        }
                    }

                    ODSLazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        gap = 4.dp
                    ) {
                        items(filteredCountries.size) { index ->
                            val country = filteredCountries[index]
                            ODSRow(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        onCountryCodeChange(country.dialCode, country.flag)
                                        showCountryPicker = false
                                        searchQuery = ""
                                    }
                                    .padding(horizontal = 12.dp, vertical = 12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                ODSRow(
                                    verticalAlignment = Alignment.CenterVertically,
                                    gap = 12.dp
                                ) {
                                    ODSText(
                                        text = country.flag,
                                        style = ODSTextStyles.bodyMBold,
                                        color = scheme.basicText
                                    )
                                    ODSText(
                                        text = country.name,
                                        style = ODSTextStyles.bodyMRegular,
                                        color = scheme.basicText
                                    )
                                }
                                ODSText(
                                    text = country.dialCode,
                                    style = ODSTextStyles.bodySBold,
                                    color = scheme.basicAccent
                                )
                            }
                        }
                    }
                }
            }
        )
    }
}

/**
 * Separate Component for Verifying OTP with 60s Resend Timer & Edit Number Link.
 */
@Composable
private fun VerifyOtpContent(
    uiState: AuthUiState,
    focusRequester: FocusRequester,
    scheme: ODSTheme,
    onOtpChange: (String) -> Unit,
    onContinueClick: (String) -> Unit,
    onBackClick: () -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val isOtpValid = remember(uiState.otp) {
        val len = uiState.otp.filter { it.isDigit() }.length
        len == 6
    }

    var resendTimer by remember { mutableIntStateOf(60) }

    LaunchedEffect(uiState.step) {
        resendTimer = 60
    }

    LaunchedEffect(resendTimer) {
        if (resendTimer > 0) {
            delay(1000L)
            resendTimer--
        }
    }

    ODSColumn(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Headline
        ODSText(
            modifier = Modifier.fillMaxWidth(),
            text = "Enter verification code",
            style = ODSTextStyle(
                fontFamily = ODSR.font.funnelsans_bold,
                fontSize = 26,
                lineHeight = 26
            ),
            color = scheme.basicText,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Subtitle with Edit Number option
        ODSRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalAlignment = Alignment.CenterVertically,
            gap = 6.dp
        ) {
            val displayPhone = uiState.fullFormattedPhone.ifBlank {
                "${uiState.countryCode} ${uiState.phone}"
            }
            ODSText(
                text = "We sent a WhatsApp code to $displayPhone",
                style = ODSTextStyle(
                    fontFamily = ODSR.font.funnelsans_medium,
                    fontSize = 14,
                    lineHeight = 14
                ),
                color = scheme.basicTextRecessive,
                textAlign = TextAlign.Center
            )
            ODSRow(
                modifier = Modifier
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = onBackClick
                    ),
                verticalAlignment = Alignment.CenterVertically,
                gap = 4.dp
            ) {
                ODSIcon(
                    iconModel = ODSIconModel(
                        drawableRes = ODSR.drawable.ic_edit_3,
                        contentDescription = "Edit Number"
                    ),
                    tint = scheme.basicAccent.getColor(),
                    modifier = Modifier.size(14.dp)
                )
                ODSText(
                    text = "Edit",
                    style = ODSTextStyle(
                        fontFamily = ODSR.font.funnelsans_bold,
                        fontSize = 14,
                        lineHeight = 14
                    ),
                    color = scheme.basicAccent,
                    textAlign = TextAlign.Center
                )
            }
        }

        if (!uiState.otpHint.isNullOrBlank() && !uiState.otpHint.contains("123456")) {
            Spacer(modifier = Modifier.height(8.dp))
            ODSText(
                text = uiState.otpHint,
                style = ODSTextStyle(
                    fontFamily = ODSR.font.funnelsans_medium,
                    fontSize = 13,
                    lineHeight = 13
                ),
                color = scheme.basicAccent,
                textAlign = TextAlign.Center
            )
        }

        val fullPhone = uiState.fullFormattedPhone.ifBlank {
            val clean = uiState.phone.filter { it.isDigit() }
            "${uiState.countryCode}$clean"
        }

        // 32dp margin before OTP input box
        Spacer(modifier = Modifier.height(32.dp))

        // ODS Code Input (6-digit OTP)
        ODSCodeInput(
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester),
            scheme = scheme,
            props = ODSCodeInputProps(
                masked = false,
                mode = if (!uiState.error.isNullOrBlank()) ODSCodeInputMode.ERROR else ODSCodeInputMode.STANDARD,
                errorMessage = uiState.error ?: "",
                inputItems = List(6) { index ->
                    ODSInputItemModel(
                        inputText = uiState.otp.getOrNull(index)?.toString(),
                        placeHolder = "•"
                    )
                }
            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    if (isOtpValid && !uiState.isLoading) {
                        keyboardController?.hide()
                        onContinueClick(fullPhone)
                    }
                }
            ),
            onValueChange = { input ->
                val clean = input.filter { it.isDigit() }.take(6)
                onOtpChange(clean)
            },
            onCodeFilled = { filledCode ->
                val clean = filledCode.filter { it.isDigit() }
                if (clean.length == 6) {
                    keyboardController?.hide()
                    onOtpChange(clean)
                    if (!uiState.isLoading) {
                        onContinueClick(fullPhone)
                    }
                }
            }
        )

        // 32dp margin before Verify button
        Spacer(modifier = Modifier.height(32.dp))

        // ODS Verify Button
        ODSButton(
            modifier = Modifier.fillMaxWidth(),
            scheme = scheme,
            props = ODSButtonProps(
                label = if (uiState.isLoading) "Verifying..." else "Verify & Continue",
                variant = ODSButtonVariant.SECONDARY,
                disabled = !isOtpValid || uiState.isLoading
            ),
            onClick = {
                keyboardController?.hide()
                onContinueClick(fullPhone)
            }
        )

        // 24dp margin after button to Resend action
        Spacer(modifier = Modifier.height(24.dp))

        if (resendTimer > 0) {
            ODSRow(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                gap = 6.dp
            ) {
                ODSText(
                    text = "Resend code in",
                    style = ODSTextStyle(
                        fontFamily = ODSR.font.funnelsans_medium,
                        fontSize = 14,
                        lineHeight = 14
                    ),
                    color = scheme.basicTextRecessive,
                    textAlign = TextAlign.Center
                )
                ODSText(
                    text = "${resendTimer}s",
                    style = ODSTextStyle(
                        fontFamily = ODSR.font.funnelsans_bold,
                        fontSize = 14,
                        lineHeight = 14
                    ),
                    color = scheme.basicAccent,
                    textAlign = TextAlign.Center
                )
            }
        } else {
            ODSRow(
                modifier = Modifier
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = {
                            resendTimer = 60
                            val fullPhone = uiState.fullFormattedPhone.ifBlank {
                                "${uiState.countryCode}${uiState.phone}"
                            }
                            onContinueClick(fullPhone)
                        }
                    ),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                gap = 6.dp
            ) {
                ODSText(
                    text = "Didn't receive code?",
                    style = ODSTextStyle(
                        fontFamily = ODSR.font.funnelsans_medium,
                        fontSize = 14,
                        lineHeight = 14
                    ),
                    color = scheme.basicTextRecessive,
                    textAlign = TextAlign.Center
                )
                ODSText(
                    text = "Resend via WhatsApp",
                    style = ODSTextStyle(
                        fontFamily = ODSR.font.funnelsans_bold,
                        fontSize = 14,
                        lineHeight = 14
                    ),
                    color = scheme.basicAccent,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
