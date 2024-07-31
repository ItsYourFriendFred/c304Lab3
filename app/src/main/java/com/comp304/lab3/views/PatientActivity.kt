package com.comp304.lab3.views

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.comp304.lab3.R
import com.comp304.lab3.util.Constants
import com.comp304.lab3.util.Constants.PATIENT_NURSE_ID_KEY
import com.comp304.lab3.viewmodels.AppViewModelProvider
import com.comp304.lab3.viewmodels.PatientDetails
import com.comp304.lab3.viewmodels.PatientEntryViewModel
import kotlinx.coroutines.launch

class PatientActivity : AppCompatActivity() {

    private val viewModel: PatientEntryViewModel by viewModels { AppViewModelProvider.Factory }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_patient)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Retrieve Nurse ID from shared preferences
        val sharedPreferences = getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val currentNurseId = sharedPreferences.getInt(Constants.SHARED_PREF_KEY, 0)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setupSpinners()

        // Get the stored nurse ID from the Main activity that was stored via shared preferences
        val nurseId = intent.getIntExtra(PATIENT_NURSE_ID_KEY, currentNurseId)
        val editTextNurseId: EditText = findViewById(R.id.editTextNurseId)
        val buttonAdd: Button = findViewById(R.id.buttonAddPatientInfo)

        // Automatically set the Nurse ID editText in the form to the user/operating Nurse's ID
        editTextNurseId.setText(nurseId.toString())

        buttonAdd.setOnClickListener {
            savePatientInfo()
        }

    }

    // Populate the spinners with their relevant options
    private fun setupSpinners() {
        val departments = resources.getStringArray(R.array.department_array)
        val rooms = resources.getStringArray(R.array.room_array)

        val departmentAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, departments)
        departmentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        findViewById<Spinner>(R.id.spinnerDepartment).adapter = departmentAdapter

        val roomAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, rooms)
        roomAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        findViewById<Spinner>(R.id.spinnerRoomNumber).adapter = roomAdapter
    }

    // Save changes to patient information
    private fun savePatientInfo() {
        val updatedFirstName = findViewById<EditText>(R.id.editTextFirstName).text.toString()
        val updatedLastName = findViewById<EditText>(R.id.editTextLastName).text.toString()
        val updatedNurseId = findViewById<EditText>(R.id.editTextNurseId).text.toString().toInt()
        val selectedDepartment = findViewById<Spinner>(R.id.spinnerDepartment).selectedItem.toString()
        val selectedRoomNumber = findViewById<Spinner>(R.id.spinnerRoomNumber).selectedItem.toString().toInt()

        // Creating a PatientDetails object from the filled in form fields
        val patientDetails = PatientDetails(
            firstname = updatedFirstName,
            lastname = updatedLastName,
            nurseId = updatedNurseId,
            department = selectedDepartment,
            room = selectedRoomNumber
        )

        viewModel.updateUiState(patientDetails)

        if (!viewModel.patientUiState.isEntryValid) {
            Toast.makeText(this, "Invalid input", Toast.LENGTH_SHORT).show()
            return
        }

        // Also, navigate to previous Main activity if successful in saving patient info
        lifecycleScope.launch {
            try {
                viewModel.savePatient()
                Toast.makeText(this@PatientActivity, "Patient added successfully", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@PatientActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            } catch (e: Exception) {
                Log.e("PatientEntryViewModel", "Unexpected error during adding", e)
                Toast.makeText(this@PatientActivity,
                    "Error occurred when adding new patient",
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