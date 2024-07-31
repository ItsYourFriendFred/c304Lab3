package com.comp304.lab3.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.comp304.lab3.data.model.Patient
import com.comp304.lab3.data.repository.PatientsRepository

/**
 * ViewModel to validate and insert Patients in the Room database
 */
class PatientEntryViewModel(private val patientsRepository: PatientsRepository) : ViewModel() {

    /**
     * Holds current patient UI state
     */
    var patientUiState by mutableStateOf(PatientUiState())
        private set

    /**
     * Updates the [PatientUiState] with the value provided in the argument.
     * Also triggers a validation for input values.
     */
    fun updateUiState(patientDetails: PatientDetails) {
        patientUiState = PatientUiState(patientDetails = patientDetails, isEntryValid = validateInput(patientDetails))
        Log.d("PatientEntryViewModel", "Patient UI State updated: $patientUiState")
    }

    // Validate that the input isn't blank; comparisons come from data class PatientDetails defined defaults
    private fun validateInput(uiState: PatientDetails = patientUiState.patientDetails): Boolean {
        return with(uiState) {
            val isValid = firstname.isNotBlank() && lastname.isNotBlank() && department.isNotBlank() && nurseId != 0 && room != 0
            Log.d("PatientEntryViewModel", "Validation result: $isValid")
            isValid
        }
    }

    suspend fun savePatient() {
        if (validateInput()) {
            Log.d("PatientEntryViewModel", "Saving patient: ${patientUiState.patientDetails}")
            patientsRepository.insertPatient(patientUiState.patientDetails.toPatient())
        } else {
            Log.d("PatientEntryViewModel", "Invalid patient data, not saving.")
        }
    }
}

/**
 * Represents Ui State for a Patient.
 */
data class PatientUiState(
    val patientDetails: PatientDetails = PatientDetails(),
    val isEntryValid: Boolean = false
)

data class PatientDetails(
    val patientId: Int = 0,
    val firstname: String = "",
    val lastname: String = "",
    val department: String = "",
    val nurseId: Int = 0,
    val room: Int = 0
)

/**
 * Extension function to convert [PatientDetails] to [Patient]
 */
fun PatientDetails.toPatient(): Patient = Patient(
    patientId = this.patientId,
    firstname = this.firstname,
    lastname = this.lastname,
    department = this.department,
    nurseId = this.nurseId,
    room = this.room
)

/**
 * Extension function to convert [Patient] to [PatientUiState]
 */
fun Patient.toPatientUiState(isEntryValid: Boolean = false): PatientUiState = PatientUiState(
    patientDetails = this.toPatientDetails(),
    isEntryValid = isEntryValid
)

/**
 * Extension function to convert [Patient] to [PatientDetails]
 */
fun Patient.toPatientDetails(): PatientDetails = PatientDetails(
    patientId = patientId,
    firstname = firstname,
    lastname = lastname,
    department = department,
    nurseId = nurseId,
    room = room
)