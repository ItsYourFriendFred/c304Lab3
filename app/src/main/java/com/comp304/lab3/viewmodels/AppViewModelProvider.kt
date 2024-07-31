package com.comp304.lab3.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.comp304.lab3.HospitalApplication

/**
 * Provides Factory to create instance of ViewModel for the entire app
 */
object AppViewModelProvider {
    val Factory = viewModelFactory {
        // Initializer for LogInViewModel
        initializer {
            LogInViewModel(hospitalApplication().container.nursesRepository)
        }
        // Initializer for MainViewModel
        initializer {
            MainViewModel(hospitalApplication().container.patientsRepository)
        }
        // Initializer for PatientEntryViewModel
        initializer {
            PatientEntryViewModel(hospitalApplication().container.patientsRepository)
        }
        // Initializer for PatientEditViewModel
        initializer {
            PatientEditViewModel(this.createSavedStateHandle(),
                hospitalApplication().container.patientsRepository)
        }
        // Initializer for TestViewModel
        initializer {
            ViewTestInfoViewModel(hospitalApplication().container.testsRepository)
        }
        // Initializer for TestEntryViewModel
        initializer {
            TestEntryViewModel(hospitalApplication().container.testsRepository)
        }
    }
}

/**
 * Extension function to query for [Application] object and return an instance of
 * [HospitalApplication].
 */
fun CreationExtras.hospitalApplication(): HospitalApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as HospitalApplication)