package com.comp304.lab3.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.comp304.lab3.data.model.Test
import kotlinx.coroutines.flow.Flow

@Dao
interface TestDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(test: Test)

    @Update
    suspend fun update(test: Test)

    @Delete
    suspend fun delete(test: Test)

    @Query("select * from Tests order by testId asc")
    fun getAllTests(): Flow<List<Test>>

    @Query("select * from Tests where patientId = :id order by testId asc")
    fun getAllTestByPatient(id: Int): Flow<List<Test>>
}