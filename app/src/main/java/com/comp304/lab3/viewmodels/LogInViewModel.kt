package com.comp304.lab3.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.comp304.lab3.data.model.Nurse
import com.comp304.lab3.data.repository.NursesRepository
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

/**
 * ViewModel to retrieve and update a nurse from the [NursesRepository]'s data source
 */
class LogInViewModel(private val nursesRepository: NursesRepository) : ViewModel() {

    // Holds current login/nurse UI state
    var logInUiState by mutableStateOf(LogInUiState())
        private set

    fun updateUiState(logInDetails: NurseDetails) {
        logInUiState =
            LogInUiState(logInDetails = logInDetails,
                isEntryValid = validateInput(logInDetails))
    }

    // Validate that the input isn't blank; comparisons come from data class NurseDetails defined defaults
    private fun validateInput(uiState: NurseDetails = logInUiState.logInDetails): Boolean {
        return with(uiState) {
            Log.d("LogInViewModel", "Validating input: nurseId=${logInUiState.logInDetails.nurseId}, password=${logInUiState.logInDetails.password}")
            nurseId != 0 && password.isNotBlank()
        }
    }

    // Attempt logging in if input is valid
    fun logIn(
        onLoginSuccess: (Nurse) -> Unit,
        onLoginFailure: () -> Unit,
        onAccessFailure: () -> Unit,
        onInputFailure: () -> Unit
    ) {
        if (validateInput()) {
            viewModelScope.launch {
                try {
                    Log.d("LogInViewModel", "Starting login process")
                    nursesRepository.getNurseStream(logInUiState.logInDetails.nurseId, logInUiState.logInDetails.password)
                        .catch { e ->
                            Log.e("LogInViewModel", "Error during database access", e)
                            onAccessFailure()
                        }
                        .collect { nurse ->
                            Log.d("LogInViewModel", "Collecting data from repository")
                            if (nurse != null) {
                                Log.d("LogInViewModel", "Login successful")
                                onLoginSuccess(nurse)
                            } else {
                                Log.d("LogInViewModel", "Login failed: Username and/or password were invalid")
                                onLoginFailure()
                            }
                        }
                } catch (e: Exception) {
                    Log.e("LogInViewModel", "Unexpected error during login", e)
                    onAccessFailure()
                }
            }
        } else {
            Log.d("LogInViewModel", "Input validation failed")
            onInputFailure()
        }
    }
}

data class LogInUiState(
    val logInDetails: NurseDetails = NurseDetails(),
    val isEntryValid: Boolean = false
)

data class NurseDetails(
    val nurseId: Int = 0,
    val firstname: String = "",
    val lastname: String = "",
    val department: String = "",
    val password: String = ""
)
/**
 * Extension function to convert [NurseDetails] to [Nurse].
 */
fun NurseDetails.toItem(): Nurse = Nurse(
    nurseId = this.nurseId,
    firstname = this.firstname,
    lastname = this.lastname,
    department = this.department,
    password = this.password
)

/**
 * Extension function to convert [Nurse] to [LogInUiState]
 */
fun Nurse.toItemUiState(isEntryValid: Boolean = false): LogInUiState = LogInUiState(
    logInDetails = this.toNurseDetails(),
    isEntryValid = isEntryValid
)

/**
 * Extension function to convert [Nurse] to [NurseDetails]
 */
fun Nurse.toNurseDetails(): NurseDetails = NurseDetails(
    nurseId = nurseId,
    firstname = firstname,
    lastname = lastname,
    department = department,
    password = password
)