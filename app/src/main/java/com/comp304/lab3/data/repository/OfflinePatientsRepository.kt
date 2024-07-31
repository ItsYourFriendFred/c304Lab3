package com.comp304.lab3.data.repository

import com.comp304.lab3.data.dao.PatientDao
import com.comp304.lab3.data.model.Patient
import kotlinx.coroutines.flow.Flow

class OfflinePatientsRepository(private val patientDao: PatientDao): PatientsRepository {
    override fun getAllPatientsStream(): Flow<List<Patient>> = patientDao.getAllPatients()

    override fun getAllPatientsByNurseStream(nurseId: Int): Flow<List<Patient>> =
        patientDao.getAllPatientsByNurse(nurseId)

    override fun getPatient(id: Int): Flow<Patient> = patientDao.getPatientAsFlow(id)

    override suspend fun insertPatient(patient: Patient) = patientDao.insert(patient)

    override suspend fun deletePatient(patient: Patient) = patientDao.delete(patient)
    override suspend fun updatePatient(patient: Patient) = patientDao.update(patient)
}