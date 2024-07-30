package com.comp304.lab3.views

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
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
import com.comp304.lab3.util.Constants.SHARED_PREF_KEY
import com.comp304.lab3.util.Constants.SHARED_PREF_NAME
import com.comp304.lab3.viewmodels.AppViewModelProvider
import com.comp304.lab3.viewmodels.MainUiState
import com.comp304.lab3.viewmodels.MainViewModel
import kotlinx.coroutines.flow.collect
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

        val newTitle = supportActionBar?.title.toString() + getString(R.string.main_app_bar_nurse_id_text) + nurseId
        supportActionBar?.title = newTitle

        recyclerView = findViewById(R.id.recyclerViewPatients)
        recyclerView.layoutManager = LinearLayoutManager(this)
        patientAdapter = PatientRecyclerViewAdapter(this, sharedPreferences)
        recyclerView.adapter = patientAdapter

        lifecycleScope.launch {
            mainViewModel.mainUiState.collect { uiState ->
                Log.d("MainActivity", "Collected UI State: ${uiState.patientsList}")
                updateUI(uiState)
            }
        }

    }

    private fun updateUI(uiState: MainUiState) {
        Log.d("MainActivity", "Updating UI with patients: ${uiState.patientsList}")
        patientAdapter.submitList(uiState.patientsList)
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