package com.comp304.lab3.data.repository

import com.comp304.lab3.data.dao.NurseDao
import com.comp304.lab3.data.model.Nurse
import kotlinx.coroutines.flow.Flow

class OfflineNursesRepository(private val nurseDao: NurseDao): NursesRepository {
    override fun getAllNursesStream(): Flow<List<Nurse>> = nurseDao.getAllNurses()

    override fun getNurseStream(id: Int, password: String): Flow<Nurse?> = nurseDao.getNurseAsFlow(id, password)

    override suspend fun insertNurse(nurse: Nurse) = nurseDao.insert(nurse)

    override suspend fun deleteNurse(nurse: Nurse) = nurseDao.delete(nurse)

    override suspend fun updateNurse(nurse: Nurse) = nurseDao.update(nurse)
}