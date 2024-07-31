package com.comp304.lab3.views

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.comp304.lab3.R
import com.comp304.lab3.adapters.PatientRecyclerViewAdapter
import com.comp304.lab3.util.Constants.PATIENT_DEPARTMENT_KEY
import com.comp304.lab3.util.Constants.PATIENT_FIRSTNAME_KEY
import com.comp304.lab3.util.Constants.PATIENT_ID_KEY
import com.comp304.lab3.util.Constants.PATIENT_LASTNAME_KEY
import com.comp304.lab3.util.Constants.PATIENT_NURSE_ID_KEY
import com.comp304.lab3.util.Constants.PATIENT_ROOM_KEY
import com.comp304.lab3.util.Constants.SHARED_PREF_KEY
import com.comp304.lab3.util.Constants.SHARED_PREF_NAME
import com.comp304.lab3.viewmodels.AppViewModelProvider
import com.comp304.lab3.viewmodels.MainUiState
import com.comp304.lab3.viewmodels.MainViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val mainViewModel: MainViewModel by viewModels { AppViewModelProvider.Factory }
    private lateinit var recyclerView: RecyclerView
    private lateinit var patientAdapter: PatientRecyclerViewAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Retrieve Nurse ID from shared preferences
        val sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val nurseId = sharedPreferences.getInt(SHARED_PREF_KEY, 0)

        // Add current Nurse ID to action bar for displaying
        val newTitle = supportActionBar?.title.toString() + getString(R.string.main_app_bar_nurse_id_text) + nurseId
        supportActionBar?.title = newTitle

        // Setting up the RecyclerView using the RecyclerViewAdapter
        recyclerView = findViewById(R.id.recyclerViewPatients)
        patientAdapter = PatientRecyclerViewAdapter(this, sharedPreferences) { patient ->
            val intent = Intent(this, UpdateInfoActivity::class.java).apply {
                putExtra(PATIENT_ID_KEY, patient.patientId)
                putExtra(PATIENT_FIRSTNAME_KEY, patient.firstname)
                putExtra(PATIENT_LASTNAME_KEY, patient.lastname)
                putExtra(PATIENT_DEPARTMENT_KEY, patient.department)
                putExtra(PATIENT_NURSE_ID_KEY, patient.nurseId)
                putExtra(PATIENT_ROOM_KEY, patient.room)
            }
            startActivity(intent)
        }
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = patientAdapter

        // Retrieving the Patients through the ViewModel
        lifecycleScope.launch {
            mainViewModel.mainUiState.collect { uiState ->
                Log.d("MainActivity", "Collected UI State: ${uiState.patientsList}")
                updateUI(uiState)
            }
        }

        val addPatientButton: FloatingActionButton = findViewById(R.id.fabAddPatient)
        addPatientButton.setOnClickListener {
            val intent = Intent(this, PatientActivity::class.java)
            startActivity(intent)
        }

    }

    // Update UI based on UIState to get the latest patients
    private fun updateUI(uiState: MainUiState) {
        Log.d("MainActivity", "Updating UI with patients: ${uiState.patientsList}")
        patientAdapter.updateList(uiState.patientsList)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_main -> {
                val intent = Intent(this, PatientActivity::class.java)
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