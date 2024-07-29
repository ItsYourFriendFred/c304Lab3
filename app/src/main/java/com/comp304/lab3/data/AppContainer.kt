package com.comp304.lab3.data

import android.content.Context
import com.comp304.lab3.data.repository.NursesRepository
import com.comp304.lab3.data.repository.OfflineNursesRepository

interface AppContainer {
    val nursesRepository: NursesRepository
}
class AppDataContainer(private val context: Context): AppContainer {
    override val nursesRepository: NursesRepository by lazy {
        OfflineNursesRepository(AppDatabase.getDatabase(context).nurseDao())
    }
}