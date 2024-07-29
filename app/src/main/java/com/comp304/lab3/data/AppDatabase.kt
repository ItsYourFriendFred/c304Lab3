package com.comp304.lab3.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.comp304.lab3.data.dao.NurseDao
import com.comp304.lab3.data.dao.PatientDao
import com.comp304.lab3.data.dao.TestDao
import com.comp304.lab3.data.model.Nurse
import com.comp304.lab3.data.model.Patient
import com.comp304.lab3.data.model.Test

@Database(entities = [Nurse::class, Patient::class, Test::class], version = 1, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {
    abstract fun nurseDao(): NurseDao
    abstract fun patientDao(): PatientDao
    abstract fun testDao(): TestDao

    companion object {
        private var Instance: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, AppDatabase::class.java, "hospital_database")
                    .fallbackToDestructiveMigration()
                    .createFromAsset("database/app.db")
                    .build()
                    .also { Instance = it }
            }
        }
    }

}