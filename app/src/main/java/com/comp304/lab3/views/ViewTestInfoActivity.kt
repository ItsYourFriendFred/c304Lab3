package com.comp304.lab3.views

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.comp304.lab3.R
import com.comp304.lab3.adapters.TestRecyclerViewAdapter
import com.comp304.lab3.util.Constants
import com.comp304.lab3.viewmodels.AppViewModelProvider
import com.comp304.lab3.viewmodels.PatientDetails
import com.comp304.lab3.viewmodels.TestUiState
import com.comp304.lab3.viewmodels.ViewTestInfoViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch

class ViewTestInfoActivity : AppCompatActivity() {
    private val viewTestInfoViewModel: ViewTestInfoViewModel by viewModels { AppViewModelProvider.Factory }
    private lateinit var recyclerView: RecyclerView
    private lateinit var testAdapter: TestRecyclerViewAdapter
    private var patientId: Int = -1
    private lateinit var noTestsTextView: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_view_test_info)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Retrieve Nurse ID from shared preferences & Patient ID from intent
        val sharedPreferences = getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val nurseId = sharedPreferences.getInt(Constants.SHARED_PREF_KEY, 0)
        patientId = intent.getIntExtra(Constants.PATIENT_ID_KEY, -1)

        // Check if patientId is valid
        if (patientId == -1) {
            Log.e("ViewTestInfoActivity", "Invalid Patient ID")
            Toast.makeText(this, "Invalid Patient ID", Toast.LENGTH_SHORT).show()
            finish() // Close the activity
            return
        }

        // Add Patient ID to the app bar for context
        val newTitle = supportActionBar?.title.toString() + getString(R.string.main_app_bar_patient_id_text) + patientId
        supportActionBar?.title = newTitle

        // Set up the recycler
        recyclerView = findViewById(R.id.recyclerViewTests)
        testAdapter = TestRecyclerViewAdapter(this, sharedPreferences)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = testAdapter

        // TextView used to display a message if the patient doesn't have any tests yet
        noTestsTextView = findViewById(R.id.textViewNoTests)

        // Set Patient ID to a reference for adding tests
        viewTestInfoViewModel.setPatientId(patientId)

        // Retrieving the Tests through the ViewModel
        lifecycleScope.launch {
            viewTestInfoViewModel.testUiState.collect { uiState ->
                Log.d("ViewTestInfoActivity", "Collected UI State: ${uiState.testsList}")
                updateUI(uiState)
            }
        }

        // Set button to proceed to test activity for adding tests
        val addTestButton: FloatingActionButton = findViewById(R.id.fabAddTest)
        addTestButton.setOnClickListener {
            val intent = Intent(this, TestActivity::class.java).apply {
                putExtra(Constants.PATIENT_ID_KEY, patientId)
            }
            startActivity(intent)
        }
    }

    // Update the UI state setting the visibility of a placeholder message if the list of tests is empty, as well as the list's visibility if it's empty
    // Based on UIState to get the latest tests
    private fun updateUI(uiState: TestUiState) {
        Log.d("ViewTestInfoActivity", "Updating UI with tests: ${uiState.testsList}")
        if (uiState.testsList.isEmpty()) {
            recyclerView.visibility = View.GONE
            noTestsTextView.visibility = View.VISIBLE
        } else {
            recyclerView.visibility = View.VISIBLE
            noTestsTextView.visibility = View.GONE
            testAdapter.updateList(uiState.testsList)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_test, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_tests -> {
                val intent = Intent(this, TestActivity::class.java).apply {
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