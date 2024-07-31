package com.comp304.lab3.views

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import com.comp304.lab3.R
import com.comp304.lab3.util.Constants.SHARED_PREF_KEY
import com.comp304.lab3.util.Constants.SHARED_PREF_NAME
import com.comp304.lab3.viewmodels.AppViewModelProvider
import com.comp304.lab3.viewmodels.LogInViewModel

// Activity for a nurse to log in. Successful log in leads to main activity containing the core app navigation
class LogInActivity : AppCompatActivity() {

    private val logInViewModel: LogInViewModel by viewModels { AppViewModelProvider.Factory }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_log_in)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Hide action bar
        supportActionBar?.hide()

        // Obtain references to UI components
        val usernameEditText: EditText = findViewById(R.id.editTextUsername)
        val passwordEditText: EditText = findViewById(R.id.editTextPassword)
        val logInButton: Button = findViewById(R.id.buttonLogIn)

        /**
         * Set listeners for username and password fields to update the state of the user input
         * so that the state in the ViewModel reflects latest input
         */
        usernameEditText.addTextChangedListener {
            logInViewModel.updateUiState(
                logInViewModel.logInUiState.logInDetails.copy(nurseId = it.toString().toIntOrNull() ?: 0)
            )
        }
        passwordEditText.addTextChangedListener {
            logInViewModel.updateUiState(
                logInViewModel.logInUiState.logInDetails.copy(password = it.toString())
            )
        }

        // Set listener logic for logging in, accounting for issues in input, incorrect username & password, or issues with database acess
        logInButton.setOnClickListener {
            logInViewModel.logIn(
                onLoginSuccess = { nurse ->
                    // Save Nurse ID to SharedPreferences
                    val sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
                    with(sharedPreferences.edit()) {
                        putInt(SHARED_PREF_KEY, nurse.nurseId)
                        apply()
                    }

                    Toast.makeText(this, "Logged in as ${nurse.firstname} ${nurse.lastname}", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                },
                onLoginFailure = {
                    Toast.makeText(this, "Login failed: Username and/or password were invalid", Toast.LENGTH_SHORT).show()
                },
                onAccessFailure = {
                    Toast.makeText(this, "Access Error", Toast.LENGTH_SHORT).show()
                },
                onInputFailure = {
                    Toast.makeText(this, "Input cannot be blank", Toast.LENGTH_SHORT).show()
                }
            )
        }
    }
}