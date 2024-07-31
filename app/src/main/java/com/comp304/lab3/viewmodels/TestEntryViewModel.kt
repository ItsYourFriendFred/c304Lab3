package com.comp304.lab3.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.comp304.lab3.data.model.Test
import com.comp304.lab3.data.repository.TestsRepository

/**
 * ViewModel to validate and insert Tests in the Room database
 */
class TestEntryViewModel(private val testsRepository: TestsRepository) : ViewModel() {

    /**
     * Holds current test UI state
     */
    var testEntryUiState by mutableStateOf(TestEntryUiState())
        private set

    /**
     * Updates the [TestEntryUiState] with the value provided in the argument.
     * Also triggers a validation for input values.
     */
    fun updateUiState(testDetails: TestDetails) {
        testEntryUiState = TestEntryUiState(testDetails = testDetails, isEntryValid = validateInput(testDetails))
        Log.d("TestEntryViewModel", "Test UI State updated: $testEntryUiState")
    }

    // Validate that the input isn't blank; comparisons come from data class TestDetails defined defaults
    private fun validateInput(uiState: TestDetails = testEntryUiState.testDetails): Boolean {
        return with(uiState) {
            val isValid = nurseId != 0 && patientId != 0 && BPL >= 0 && BPH >= 0 && temperature >= 0 && FPG >= 0 && rbcCount >= 0 && heartRate >= 0
            Log.d("TestEntryViewModel", "Validation result: $isValid")
            isValid
        }
    }

    suspend fun saveTest() {
        if (validateInput()) {
            Log.d("TestEntryViewModel", "Saving test: ${testEntryUiState.testDetails}")
            testsRepository.insertTest(testEntryUiState.testDetails.toTest())
        } else {
            Log.d("TestEntryViewModel", "Invalid test data, not saving.")
        }
    }
}

/**
 * Represents Ui State for a Test.
 */
data class TestEntryUiState(
    val testDetails: TestDetails = TestDetails(),
    val isEntryValid: Boolean = false
)

data class TestDetails(
    val testId: Int = 0,
    val nurseId: Int = 0,
    val patientId: Int = 0,
    val BPL: Double = 0.0,
    val BPH: Double = 0.0,
    val temperature: Double = 0.0,
    val FPG: Double = 0.0,
    val rbcCount: Double = 0.0,
    val heartRate: Double = 0.0
)

/**
 * Extension function to convert [TestDetails] to [Test]
 */
fun TestDetails.toTest(): Test = Test(
    testId = this.testId,
    nurseId = this.nurseId,
    patientId = this.patientId,
    BPL = this.BPL,
    BPH = this.BPH,
    temperature = this.temperature,
    FPG = this.FPG,
    rbcCount = this.rbcCount,
    heartRate = this.heartRate
)

/**
 * Extension function to convert [Test] to [TestEntryUiState]
 */
fun Test.toTestEntryUiState(isEntryValid: Boolean = false): TestEntryUiState = TestEntryUiState(
    testDetails = this.toTestDetails(),
    isEntryValid = isEntryValid
)

/**
 * Extension function to convert [Test] to [TestDetails]
 */
fun Test.toTestDetails(): TestDetails = TestDetails(
    testId = testId,
    nurseId = nurseId,
    patientId = patientId,
    BPL = BPL,
    BPH = BPH,
    temperature = temperature,
    FPG = FPG,
    rbcCount = rbcCount,
    heartRate = heartRate
)