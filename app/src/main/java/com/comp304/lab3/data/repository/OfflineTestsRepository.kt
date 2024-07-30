package com.comp304.lab3.data.repository

import com.comp304.lab3.data.dao.TestDao
import com.comp304.lab3.data.model.Test
import kotlinx.coroutines.flow.Flow

class OfflineTestsRepository(private val testDao: TestDao): TestsRepository {
    override fun getAllTestsStream(): Flow<List<Test>> = testDao.getAllTests()

    override fun getAllTestByPatientStream(patientId: Int): Flow<List<Test>> =
        testDao.getAllTestByPatient(patientId)

    override fun getTestStream(id: Int): Flow<Test?> = testDao.getTest(id)

    override suspend fun insertTest(test: Test) = testDao.insert(test)

    override suspend fun deleteTest(test: Test) = testDao.delete(test)

    override suspend fun updateTest(test: Test) = testDao.update(test)

}