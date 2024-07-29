package com.comp304.lab3.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.comp304.lab3.data.model.Patient
import kotlinx.coroutines.flow.Flow

@Dao
interface PatientDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(patient: Patient)

    @Update
    suspend fun update(patient: Patient)

    @Delete
    suspend fun delete(patient: Patient)

    @Query("select * from Patients WHERE patientId = :id")
    fun getPatientAsPatient(id: Int): Patient

    @Query("SELECT * from Patients WHERE patientId = :id")
    fun getPatientAsFlow(id: Int): Flow<Patient>

    @Query("select * from Patients order by patientId asc")
    fun getAllPatients(): Flow<List<Patient>>

    @Query("select * from Patients where nurseId = :id order by patientId asc")
    fun getAllPatientsByNurse(id: Int): Flow<List<Patient>>
}