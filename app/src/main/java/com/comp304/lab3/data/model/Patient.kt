package com.comp304.lab3.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "Patients")
data class Patient(
    @PrimaryKey(autoGenerate = true)
    val patientId: Int = 0,
    val firstname: String,
    val lastname: String,
    val department: String,
    val nurseId: Int,
    val room: Int
)
