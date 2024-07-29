package com.comp304.lab3.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.comp304.lab3.data.model.Nurse
import kotlinx.coroutines.flow.Flow

@Dao
interface NurseDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(nurse: Nurse)

    @Update
    suspend fun update(nurse: Nurse)

    @Delete
    suspend fun delete(nurse: Nurse)

    @Query("select * from Nurses WHERE nurseId = :id and password = :password")
    fun getNurseAsNurse(id: Int, password: String): Nurse

    @Query("SELECT * from Nurses WHERE nurseId = :id and password = :password")
    fun getNurseAsFlow(id: Int, password: String): Flow<Nurse>

    @Query("SELECT * from Nurses ORDER BY firstname ASC")
    fun getAllNurses(): Flow<List<Nurse>>

}