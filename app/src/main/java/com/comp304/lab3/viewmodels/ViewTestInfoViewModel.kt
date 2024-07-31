package com.comp304.lab3.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.comp304.lab3.data.model.Test
import com.comp304.lab3.data.repository.TestsRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class ViewTestInfoViewModel(private val testsRepository: TestsRepository) : ViewModel() {
    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

    private val _patientId = MutableStateFlow<Int?>(null)

    @OptIn(ExperimentalCoroutinesApi::class)
    val testUiState: StateFlow<TestUiState> = _patientId
        .filterNotNull()
        .flatMapLatest { patientId ->
            testsRepository.getAllTestByPatientStream(patientId).map { TestUiState(it) }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = TestUiState()
        )

    fun setPatientId(patientId: Int) {
        _patientId.value = patientId
    }
}

/**
 * Ui State for ViewTestInfoActivity
 */
data class TestUiState(
    val testsList: List<Test> = listOf()
)