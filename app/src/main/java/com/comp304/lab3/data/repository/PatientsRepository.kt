package com.comp304.lab3.data.repository

import com.comp304.lab3.data.model.Patient
import kotlinx.coroutines.flow.Flow

interface PatientsRepository {
    fun getAllPatientsStream(): Flow<List<Patient>>
    fun getAllPatientsByNurseStream(nurseId: Int): Flow<List<Patient>>
    fun getPatient(id: Int): Flow<Patient>
    suspend fun insertPatient(patient: Patient)
    suspend fun deletePatient(patient: Patient)
    suspend fun updatePatient(patient: Patient)

}