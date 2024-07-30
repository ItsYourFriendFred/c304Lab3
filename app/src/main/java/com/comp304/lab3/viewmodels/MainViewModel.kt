package com.comp304.lab3.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.comp304.lab3.data.model.Patient
import com.comp304.lab3.data.repository.PatientsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

/**
 * ViewModel to retrieve all patients from the Room database
 */
class MainViewModel(private val patientsRepository: PatientsRepository) : ViewModel() {
    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

    val mainUiState: StateFlow<MainUiState> =
        patientsRepository.getAllPatientsStream().map { MainUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = MainUiState()
            )
}

/**
 * Ui State for MainActivity
 */
data class MainUiState(
    val patientsList: List<Patient> = listOf()
)