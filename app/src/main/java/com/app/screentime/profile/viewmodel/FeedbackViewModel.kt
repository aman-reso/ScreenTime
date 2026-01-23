package com.app.screentime.profile.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.screentime.core.network.service.FeedbackService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Sealed class representing feedback submission result states
 */
sealed class FeedbackSubmitResult {
    object Loading : FeedbackSubmitResult()
    data class Success(val message: String? = null) : FeedbackSubmitResult()
    data class Error(val message: String) : FeedbackSubmitResult()
}

@HiltViewModel
class FeedbackViewModel @Inject constructor(
    private val feedbackService: FeedbackService
) : ViewModel() {

    private val _submitResult = MutableStateFlow<FeedbackSubmitResult?>(null)
    val submitResult: StateFlow<FeedbackSubmitResult?> = _submitResult.asStateFlow()

    fun submitFeedback(message: String) {
        viewModelScope.launch {
            _submitResult.value = FeedbackSubmitResult.Loading
            
            feedbackService.submitFeedback(message).fold(
                onSuccess = { apiResponse ->
                    if (apiResponse.success == true) {
                        _submitResult.value = FeedbackSubmitResult.Success(
                            message = apiResponse.data?.message
                        )
                    } else {
                        _submitResult.value = FeedbackSubmitResult.Error(
                            message = apiResponse.message ?: "Failed to submit feedback"
                        )
                    }
                },
                onFailure = { exception ->
                    _submitResult.value = FeedbackSubmitResult.Error(
                        message = exception.message ?: "Failed to submit feedback"
                    )
                }
            )
        }
    }

    fun clearResult() {
        _submitResult.value = null
    }
}
