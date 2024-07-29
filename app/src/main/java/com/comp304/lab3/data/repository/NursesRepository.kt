package com.comp304.lab3.data.repository

import com.comp304.lab3.data.model.Nurse
import kotlinx.coroutines.flow.Flow

interface NursesRepository {
    fun getAllNursesStream(): Flow<List<Nurse>>

    fun getNurseStream(id: Int, password: String): Flow<Nurse?>

    suspend fun insertNurse(nurse: Nurse)

    suspend fun deleteNurse(nurse: Nurse)

    suspend fun updateNurse(nurse: Nurse)

}