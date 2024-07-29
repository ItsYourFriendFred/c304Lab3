package com.comp304.lab3.data.repository

import com.comp304.lab3.data.model.Test
import kotlinx.coroutines.flow.Flow

interface TestsRepository {
    fun getAllTestsStream(): Flow<List<Test>>
    fun getAllTestByPatientStream(patientId: Int): Flow<List<Test>>
    fun getTestStream(id: Int): Flow<Test?>
    suspend fun insertTest(test: Test)
    suspend fun deleteTest(test: Test)
    suspend fun updateTest(test: Test)
}