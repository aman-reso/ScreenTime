package com.app.screentime.reward.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.app.screentime.config.R
import com.app.screentime.reward.model.RewardCatalogItem
import com.app.screentime.reward.model.SavedClaimDetails
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSImage
import com.telekom.odsystem.atoms.ODSImageModel
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.checkbox.ODSCheckbox
import com.telekom.odsystem.atoms.checkbox.ODSCheckboxProps
import com.telekom.odsystem.atoms.checkbox.ODSCheckboxSelected
import com.telekom.odsystem.atoms.checkbox.ODSCheckboxSize
import com.telekom.odsystem.atoms.button.ODSButton
import com.telekom.odsystem.atoms.button.ODSButtonProps
import com.telekom.odsystem.atoms.button.ODSButtonSize
import com.telekom.odsystem.atoms.button.ODSButtonVariant
import com.telekom.odsystem.atoms.textfield.ODSTextField
import com.telekom.odsystem.atoms.textfield.ODSTextFieldMode
import com.telekom.odsystem.atoms.textfield.ODSTextFieldProps
import com.telekom.odsystem.atoms.textfield.ODSTextFieldSize
import com.telekom.odsystem.atoms.textfield.ODSTextFieldSupportMessageProps
import com.telekom.odsystem.atoms.divider.ODSDivider
import com.telekom.odsystem.atoms.divider.ODSDividerProps
import com.telekom.odsystem.atoms.divider.ODSDividerVariant
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinner
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinnerLabelAlignment
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinnerProps
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinnerSize
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinnerVariant
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.molecules.bottomsheet.ODSBottomSheet
import com.telekom.odsystem.molecules.bottomsheet.ODSBottomSheetProps
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * Helper function to determine if a reward is physical
 * Physical rewards typically require shipping address
 */
private fun isPhysicalReward(category: String?): Boolean {
    // Categories that typically require physical shipping
    val physicalCategories = listOf(
        "Electronics",
        "Physical",
        "Gift Card",
        "Merchandise",
        "Hardware"
    )
    return category != null && physicalCategories.any {
        category.contains(it, ignoreCase = true)
    }
}

/**
 * Validation functions
 */
private fun isValidEmail(email: String): Boolean {
    if (email.isBlank()) return false
    val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\$".toRegex()
    return email.matches(emailRegex)
}

private fun isValidName(name: String): Boolean {
    return name.trim().length >= 2
}

private fun isValidAddress(address: String): Boolean {
    return address.trim().isNotBlank()
}

private fun isValidPostalCode(postalCode: String): Boolean {
    return postalCode.length == 6 && postalCode.all { it.isDigit() }
}

private fun isValidPhoneNumber(phoneNumber: String): Boolean {
    if (phoneNumber.isBlank()) return false
    // Remove common phone number characters like +, -, spaces, parentheses
    val cleaned = phoneNumber.replace(Regex("[+\\s()-]"), "")
    return cleaned.all { it.isDigit() } && cleaned.length >= 10
}

private fun isValidUpiId(upiId: String): Boolean {
    if (upiId.isBlank()) return false
    // Basic UPI format: username@bank
    val upiRegex = "^[A-Za-z0-9._-]{2,}@[A-Za-z]{2,}\$".toRegex()
    return upiId.matches(upiRegex)
}

/**
 * Reward Info Bottom Sheet Component
 * Displays reward information and claim form
 * Shows address fields only if reward is physical
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RewardInfoBottomSheet(
    showBottomSheet: Boolean,
    onDismiss: () -> Unit,
    reward: RewardCatalogItem?,
    isLoading: Boolean = false,
    errorMessage: String? = null,
    savedClaimDetails: SavedClaimDetails? = null,
    onClaimClick: (String, String, String, String, String?, String?, Boolean) -> Unit = { _, _, _, _, _, _, _ -> },
    scheme: ODSTheme
) {
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val snackbarHostState = remember { SnackbarHostState() }

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var upiId by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var postalCode by remember { mutableStateOf("") }
    var saveDetails by remember { mutableStateOf(false) }

    // Load saved details when bottom sheet opens
    LaunchedEffect(showBottomSheet) {
        if (showBottomSheet) {
            savedClaimDetails?.let { details ->
                name = details.name
                email = details.email
                phoneNumber = details.phone
                upiId = details.upiId
                address = details.address ?: ""
                postalCode = details.postalCode ?: ""
                saveDetails = true // Auto-check if saved data exists
            } ?: run {
                // Reset fields if no saved details
                name = ""
                email = ""
                phoneNumber = ""
                upiId = ""
                address = ""
                postalCode = ""
                saveDetails = false
            }
        }
    }

    // Show error toast when error occurs
    LaunchedEffect(errorMessage) {
        if (errorMessage != null) {
            snackbarHostState.showSnackbar(
                message = errorMessage
            )
        }
    }

    val isPhysical = reward?.let { isPhysicalReward(it.rewardType) } ?: false

    // Validation states
    val nameError =
        remember(name) { if (name.isNotBlank() && !isValidName(name)) "Name must be at least 2 characters" else null }
    val emailError =
        remember(email) { if (email.isNotBlank() && !isValidEmail(email)) "Please enter a valid email address" else null }
    val phoneError =
        remember(phoneNumber) { if (phoneNumber.isNotBlank() && !isValidPhoneNumber(phoneNumber)) "Please enter a valid phone number (digits only)" else null }
    val upiError =
        remember(upiId) { if (upiId.isNotBlank() && !isValidUpiId(upiId)) "Please enter a valid UPI ID" else null }
    val addressError =
        remember(address) { if (isPhysical && address.isNotBlank() && !isValidAddress(address)) "Address cannot be empty" else null }
    val postalCodeError = remember(postalCode) {
        if (isPhysical && postalCode.isNotBlank() && !isValidPostalCode(postalCode)) {
            if (postalCode.any { !it.isDigit() }) "Postal code must contain only numbers"
            else "Postal code must be exactly 6 digits"
        } else null
    }

    // Check if form is valid
    val isFormValid = isValidName(name) &&
            isValidEmail(email) &&
            isValidPhoneNumber(phoneNumber) &&
            isValidUpiId(upiId) &&
            (!isPhysical || (isValidAddress(address) && isValidPostalCode(postalCode)))

    if (reward == null) return

    ODSBottomSheet(
        scheme = scheme,
        props = ODSBottomSheetProps(
            showHandle = false
        ),
        showBottomSheet = showBottomSheet,
        bottomSheetState = bottomSheetState,
        snackbarHostState = snackbarHostState,
        onDismissRequest = {
            if (!isLoading) {
                onDismiss()
            }
        },
        onCloseClicked = {
            if (!isLoading) {
                onDismiss()
            }
        },
        titleSlot = {
            ODSText(
                text = reward.title,
                style = DSTextStyles.bodyL,
                color = scheme.basicText
            )
        },
        contentSlot = {
            if (isLoading) {
                ODSBox(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    contentAlignment = Alignment.Center
                ) {
                    ODSLoadingSpinner(
                        modifier = Modifier.wrapContentHeight(),
                        scheme = scheme,
                        props = ODSLoadingSpinnerProps(
                            labelText = stringResource(R.string.loading),
                            size = ODSLoadingSpinnerSize.SMALL,
                            variant = ODSLoadingSpinnerVariant.STANDARD,
                            labelAlignment = ODSLoadingSpinnerLabelAlignment.HORIZONTAL
                        )
                    )
                }
            } else {
                ODSColumn(
                    modifier = Modifier
                        .fillMaxWidth(),
                    gap = DSVariables.spacingComponent4
                ) {
                    reward.imageUrl?.let { imageUrl ->
                        ODSImage(
                            imageModel = ODSImageModel(
                                url = imageUrl,
                                contentDescription = reward.title
                            ),
                            modifier = Modifier.fillMaxWidth(),
                            cornerRadius = ODSCorners(all = 12.dp)
                        )
                    }

                    // Reward Details
                    ODSText(
                        text = stringResource(R.string.reward_price_coins, reward.coinPrice),
                        style = DSTextStyles.bodyMBold,
                        color = scheme.basicText
                    )

                    // Divider
                    if (reward.isActive && reward.stockQuantity > 0) {
                        ODSDivider(
                            scheme = scheme,
                            props = ODSDividerProps(
                                variant = ODSDividerVariant.HORIZONTAL
                            )
                        )

                        // Claim Form
                        ODSText(
                            text = stringResource(R.string.claim_reward),
                            style = DSTextStyles.bodyMBold,
                            color = scheme.basicText
                        )

                        // Name field (required)
                        ODSTextField(
                            scheme = scheme,
                            props = ODSTextFieldProps(
                                label = stringResource(R.string.name),
                                inputText = name,
                                size = ODSTextFieldSize.SMALL,
                                mode = if (nameError != null) ODSTextFieldMode.ERROR else ODSTextFieldMode.STANDARD,
                                supportMessageProps = nameError?.let {
                                    ODSTextFieldSupportMessageProps(message = it)
                                }
                            ),
                            onValueChange = { name = it }
                        )

                        // Email field (required)
                        ODSTextField(
                            scheme = scheme,
                            props = ODSTextFieldProps(
                                label = stringResource(R.string.email),
                                inputText = email,
                                size = ODSTextFieldSize.SMALL,
                                mode = if (emailError != null) ODSTextFieldMode.ERROR else ODSTextFieldMode.STANDARD,
                                supportMessageProps = emailError?.let {
                                    ODSTextFieldSupportMessageProps(message = it)
                                }
                            ),
                            onValueChange = { email = it }
                        )

                        // Phone Number field (required)
                        ODSTextField(
                            scheme = scheme,
                            props = ODSTextFieldProps(
                                label = stringResource(R.string.phone_number),
                                inputText = phoneNumber,
                                size = ODSTextFieldSize.SMALL,
                                mode = if (phoneError != null) ODSTextFieldMode.ERROR else ODSTextFieldMode.STANDARD,
                                supportMessageProps = phoneError?.let {
                                    ODSTextFieldSupportMessageProps(message = it)
                                }
                            ),
                            onValueChange = { newValue ->
                                // Allow only digits, +, -, spaces, and parentheses
                                phoneNumber =
                                    newValue.filter { it.isDigit() || it == '+' || it == '-' || it == ' ' || it == '(' || it == ')' }
                            }
                        )

                        // UPI ID field (required)
                        ODSTextField(
                            scheme = scheme,
                            props = ODSTextFieldProps(
                                label = stringResource(R.string.upi_id),
                                inputText = upiId,
                                size = ODSTextFieldSize.SMALL,
                                mode = if (upiError != null) ODSTextFieldMode.ERROR else ODSTextFieldMode.STANDARD,
                                supportMessageProps = upiError?.let {
                                    ODSTextFieldSupportMessageProps(message = it)
                                }
                            ),
                            onValueChange = { upiId = it }
                        )

                        // Address fields (only if physical reward)
                        if (isPhysical) {
                            ODSTextField(
                                scheme = scheme,
                                props = ODSTextFieldProps(
                                    label = stringResource(R.string.address),
                                    inputText = address,
                                    size = ODSTextFieldSize.SMALL,
                                    mode = if (addressError != null) ODSTextFieldMode.ERROR else ODSTextFieldMode.STANDARD,
                                    supportMessageProps = addressError?.let {
                                        ODSTextFieldSupportMessageProps(message = it)
                                    }
                                ),
                                onValueChange = { address = it }
                            )

                            ODSTextField(
                                scheme = scheme,
                                props = ODSTextFieldProps(
                                    label = stringResource(R.string.postal_code),
                                    inputText = postalCode,
                                    size = ODSTextFieldSize.SMALL,
                                    mode = if (postalCodeError != null) ODSTextFieldMode.ERROR else ODSTextFieldMode.STANDARD,
                                    supportMessageProps = postalCodeError?.let {
                                        ODSTextFieldSupportMessageProps(message = it)
                                    }
                                ),
                                onValueChange = { newValue ->
                                    // Allow only digits and limit to 6 characters
                                    postalCode = newValue.filter { it.isDigit() }.take(6)
                                }
                            )
                        }

                        // Save details checkbox
                        ODSCheckbox(
                            scheme = scheme,
                            props = ODSCheckboxProps(
                                label = stringResource(R.string.save_details_for_future),
                                selected = if (saveDetails) ODSCheckboxSelected.SELECTED else ODSCheckboxSelected.UNSELECTED,
                                size = ODSCheckboxSize.SMALL
                            ),
                            onClick = { newState ->
                                saveDetails = newState == ODSCheckboxSelected.SELECTED
                            }
                        )
                    }
                }
            }
        },
        actionSlot = {
            if (reward.isActive && reward.stockQuantity > 0 && !isLoading) {
                ODSButton(
                    modifier = Modifier.fillMaxWidth(),
                    scheme = scheme,
                    props = ODSButtonProps(
                        label = stringResource(R.string.claim_reward),
                        variant = ODSButtonVariant.SECONDARY,
                        size = ODSButtonSize.SMALL,
                        disabled = !isFormValid
                    ),
                    onClick = {
                        if (isFormValid) {
                            onClaimClick(
                                name,
                                email,
                                phoneNumber,
                                upiId,
                                if (isPhysical) address else null,
                                if (isPhysical) postalCode else null,
                                saveDetails
                            )
                        }
                    }
                )
            }
        }
    )
}

