package com.comp304.lab3.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Nurses")
data class Nurse(
    @PrimaryKey(autoGenerate = true)
    val nurseId: Int = 1,
    val firstname: String,
    val lastname: String,
    val department: String,
    val password: String
)
