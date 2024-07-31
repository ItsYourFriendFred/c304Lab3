package com.comp304.lab3.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.comp304.lab3.data.repository.PatientsRepository
import com.comp304.lab3.views.UpdateInfoActivity
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class PatientEditViewModel(
    savedStateHandle: SavedStateHandle,
    private val patientsRepository: PatientsRepository
) : ViewModel() {

    /**
     * Holds current patient UI state.
     */
    var patientUiState by mutableStateOf(PatientUiState())
        private set

    private val patientId: Int = checkNotNull(savedStateHandle[UpdateInfoActivity.PATIENT_ID_KEY])

    /**
     * Updates the [PatientUiState] with the value provided in the argument.
     * Also triggers a validation for input values.
     */
    fun updateUiState(patientDetails: PatientDetails) {
        patientUiState = PatientUiState(patientDetails = patientDetails, isEntryValid = validateInput(patientDetails))
        Log.d("PatientEditViewModel", "Patient UI State updated: $patientUiState")
    }

    // Validate that the input isn't blank; comparisons come from data class PatientDetails defined defaults
    private fun validateInput(uiState: PatientDetails = patientUiState.patientDetails): Boolean {
        return with(uiState) {
            val isValid = firstname.isNotBlank() && lastname.isNotBlank() && department.isNotBlank() && nurseId != 0 && room != 0
            Log.d("PatientEditViewModel", "Validation result: $isValid")
            isValid
        }
    }

    suspend fun updatePatient() {
        if (validateInput(patientUiState.patientDetails)) {
            Log.d("PatientEditViewModel", "Saving patient: ${patientUiState.patientDetails}")
            patientsRepository.updatePatient(patientUiState.patientDetails.toPatient())
        } else {
            Log.d("PatientEditViewModel", "Invalid patient data, not saving.")
        }
    }

    init {
        viewModelScope.launch {
            patientUiState = patientsRepository.getPatient(patientId)
                .filterNotNull()
                .first()
                .toPatientUiState(true)
        }
    }
}