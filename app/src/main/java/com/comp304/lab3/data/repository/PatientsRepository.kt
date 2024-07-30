package com.comp304.lab3.data.repository

import com.comp304.lab3.data.model.Patient
import kotlinx.coroutines.flow.Flow

interface PatientsRepository {
    fun getAllPatientsStream(): Flow<List<Patient>>
    fun getAllPatientsByNurseStream(nurseId: Int): Flow<List<Patient>>
    fun getPatient(id: Int): Flow<Patient>
    suspend fun insertTest(patient: Patient)
    suspend fun deleteTest(patient: Patient)
    suspend fun updateTest(patient: Patient)

}