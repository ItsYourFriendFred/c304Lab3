package com.comp304.lab3.views

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.comp304.lab3.R
import com.comp304.lab3.util.Constants
import com.comp304.lab3.viewmodels.AppViewModelProvider
import com.comp304.lab3.viewmodels.TestDetails
import com.comp304.lab3.viewmodels.TestEntryViewModel
import kotlinx.coroutines.launch

class TestActivity : AppCompatActivity() {

    private val viewModel: TestEntryViewModel by viewModels { AppViewModelProvider.Factory }
    companion object {
        const val PATIENT_ID_KEY = Constants.PATIENT_ID_KEY
    }
    private var patientId: Int = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_test)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Retrieve Nurse ID from shared preferences & Patient ID from intent
        val sharedPreferences = getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val currentNurseId = sharedPreferences.getInt(Constants.SHARED_PREF_KEY, 0)
        patientId = intent.getIntExtra(PATIENT_ID_KEY, -1)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Obtaining references to UI components
        val nurseId = intent.getIntExtra(Constants.PATIENT_NURSE_ID_KEY, currentNurseId)
        val editTextNurseId: EditText = findViewById(R.id.editTextNurseId)
        val buttonSave: Button = findViewById(R.id.buttonSave)

        // Setting the text of the nurse ID automatically for filling and sensibility (that the nurse operating is the one adding a test)
        editTextNurseId.setText(nurseId.toString())

        buttonSave.setOnClickListener {
            saveTextInfo(patientId)
        }

    }

    // Saving (adding) a new test's information
    private fun saveTextInfo(patientId: Int) {
        val updatedNurseId = findViewById<EditText>(R.id.editTextNurseId).text.toString().toInt()
        val updatedPatientId = patientId
        val updatedBPH = findViewById<EditText>(R.id.editTextBPH).text.toString().toDouble()
        val updatedBPL = findViewById<EditText>(R.id.editTextBPL).text.toString().toDouble()
        val updatedTemperature = findViewById<EditText>(R.id.editTextTemperature).text.toString().toDouble()
        val updatedHeartRate = findViewById<EditText>(R.id.editTextHeartRate).text.toString().toDouble()
        val updatedRbcCount = findViewById<EditText>(R.id.editTextRbcCount).text.toString().toDouble()
        val updatedFPG = findViewById<EditText>(R.id.editTextFPG).text.toString().toDouble()

        // Creating a TestDetails object from the filled in form fields
        val testDetails = TestDetails(
            patientId = updatedPatientId,
            nurseId = updatedNurseId,
            BPL = updatedBPL,
            BPH = updatedBPH,
            temperature = updatedTemperature,
            heartRate = updatedHeartRate,
            rbcCount = updatedRbcCount,
            FPG = updatedFPG
        )

        // Validation of the form
        viewModel.updateUiState(testDetails)

        if (!viewModel.testEntryUiState.isEntryValid) {
            Toast.makeText(this, "Invalid input", Toast.LENGTH_SHORT).show()
            return
        }

        // Also, navigate back to the ViewTestInfo activity upon successful test insertion
        lifecycleScope.launch {
            try {
                viewModel.saveTest()
                Toast.makeText(this@TestActivity, "Test added successfully", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@TestActivity, ViewTestInfoActivity::class.java).apply {
                    putExtra(Constants.PATIENT_ID_KEY, patientId)
                }
                startActivity(intent)
                finish()
            } catch (e: Exception) {
                Log.e("TestEntryViewModel", "Unexpected error during adding", e)
                Toast.makeText(this@TestActivity,
                    "Error occurred when adding new test",
                    Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressedDispatcher.onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}