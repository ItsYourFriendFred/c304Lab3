package com.comp304.lab3.views

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
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
import com.comp304.lab3.util.Constants.PATIENT_DEPARTMENT_KEY
import com.comp304.lab3.util.Constants.PATIENT_FIRSTNAME_KEY
import com.comp304.lab3.util.Constants.PATIENT_ID_KEY
import com.comp304.lab3.util.Constants.PATIENT_LASTNAME_KEY
import com.comp304.lab3.util.Constants.PATIENT_NURSE_ID_KEY
import com.comp304.lab3.util.Constants.PATIENT_ROOM_KEY
import com.comp304.lab3.viewmodels.AppViewModelProvider
import com.comp304.lab3.viewmodels.PatientDetails
import com.comp304.lab3.viewmodels.PatientEditViewModel
import kotlinx.coroutines.launch

class UpdateInfoActivity : AppCompatActivity() {

    companion object {
        const val PATIENT_ID_KEY = Constants.PATIENT_ID_KEY
    }

    private val viewModel: PatientEditViewModel by viewModels {
        AppViewModelProvider.Factory
    }

    private var patientId: Int = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_update_info)
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

        setupSpinners()

        val patientId = intent.getIntExtra(PATIENT_ID_KEY, -1)
        val firstName = intent.getStringExtra(PATIENT_FIRSTNAME_KEY)
        val lastName = intent.getStringExtra(PATIENT_LASTNAME_KEY)
        val department = intent.getStringExtra(PATIENT_DEPARTMENT_KEY)
        val nurseId = intent.getIntExtra(PATIENT_NURSE_ID_KEY, currentNurseId)
        val roomNumber = intent.getIntExtra(PATIENT_ROOM_KEY, -1)

        val editTextFirstName: EditText = findViewById(R.id.editTextFirstName)
        val editTextLastName: EditText = findViewById(R.id.editTextLastName)
        val editTextNurseId: EditText = findViewById(R.id.editTextNurseId)
        val spinnerDepartment: Spinner = findViewById(R.id.spinnerDepartment)
        val spinnerRoomNumber: Spinner = findViewById(R.id.spinnerRoomNumber)
        val buttonSave: Button = findViewById(R.id.buttonSavePatientInfo)
        val buttonViewTests: Button = findViewById(R.id.buttonViewTests)

        editTextFirstName.setText(firstName)
        editTextLastName.setText(lastName)
        editTextNurseId.setText(nurseId.toString())
        spinnerDepartment.setSelection(resources.getStringArray(R.array.department_array).indexOf(department))
        spinnerRoomNumber.setSelection(resources.getStringArray(R.array.room_array).indexOf(roomNumber.toString()))


        buttonSave.setOnClickListener {
            // Handle save logic
            savePatientInfo(patientId)
        }

        buttonViewTests.setOnClickListener {
            val intent = Intent(this, ViewTestInfoActivity::class.java).apply {
                putExtra(Constants.PATIENT_ID_KEY, patientId)
            }
            startActivity(intent)
        }

    }

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

    private fun savePatientInfo(patientId: Int) {
        val updatedFirstName = findViewById<EditText>(R.id.editTextFirstName).text.toString()
        val updatedLastName = findViewById<EditText>(R.id.editTextLastName).text.toString()
        val updatedNurseId = findViewById<EditText>(R.id.editTextNurseId).text.toString().toInt()
        val selectedDepartment = findViewById<Spinner>(R.id.spinnerDepartment).selectedItem.toString()
        val selectedRoomNumber = findViewById<Spinner>(R.id.spinnerRoomNumber).selectedItem.toString().toInt()

        val patientDetails = PatientDetails(
            patientId = patientId,
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

        lifecycleScope.launch {
            try {
                viewModel.updatePatient()
                Toast.makeText(this@UpdateInfoActivity, "Patient information saved", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@UpdateInfoActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            } catch (e: Exception) {
                Log.e("PatientEditViewModel", "Unexpected error during update", e)
                Toast.makeText(this@UpdateInfoActivity,
                    "Error occurred when updating patient",
                    Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_patient, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_patients -> {
                val intent = Intent(this, ViewTestInfoActivity::class.java).apply {
                    putExtra(Constants.PATIENT_ID_KEY, patientId)
                }
                startActivity(intent)
                true
            }
            android.R.id.home -> {
                onBackPressedDispatcher.onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}