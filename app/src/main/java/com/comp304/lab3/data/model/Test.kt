package com.comp304.lab3.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Tests")
data class Test(
    @PrimaryKey(autoGenerate = true)
    val testId: Int = 0,
    val nurseId: Int,
    val patientId: Int,
    val BPL: Double,  // Presumably, blood pressure (diastolic, lower number)
    val BPH: Double,  // Presumably, blood pressure (systolic, upper number)
    val temperature: Double,
    val FPG: Double,  // Fasting plasma glucose, for measuring sugar in blood, level (mg/dL) - below 100 = normal
    val rbcCount: Double,  // Red blood cell count, million cells/mcL (cells/mcL)
    val heartRate: Double
)
