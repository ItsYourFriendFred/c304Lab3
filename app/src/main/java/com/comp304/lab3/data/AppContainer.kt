package com.comp304.lab3.data

import android.content.Context
import com.comp304.lab3.data.repository.NursesRepository
import com.comp304.lab3.data.repository.OfflineNursesRepository
import com.comp304.lab3.data.repository.OfflinePatientsRepository
import com.comp304.lab3.data.repository.OfflineTestsRepository
import com.comp304.lab3.data.repository.PatientsRepository
import com.comp304.lab3.data.repository.TestsRepository

// App container for Dependency injection.
interface AppContainer {
    val nursesRepository: NursesRepository
    val patientsRepository: PatientsRepository
    val testsRepository: TestsRepository
}

/**
 * [AppContainer] implementation that provides instances of [OfflineNursesRepository], [OfflinePatientsRepository], & [OfflineTestsRepository]
 */
class AppDataContainer(private val context: Context): AppContainer {
    /**
     * Implementation for [OfflineNursesRepository]
     */
    override val nursesRepository: NursesRepository by lazy {
        OfflineNursesRepository(AppDatabase.getDatabase(context).nurseDao())
    }

    /**
     * Implementation for [OfflinePatientsRepository]
     */
    override val patientsRepository: PatientsRepository by lazy {
        OfflinePatientsRepository(AppDatabase.getDatabase(context).patientDao())
    }

    /**
     * Implementation for [OfflineTestsRepository]
     */
    override val testsRepository: TestsRepository by lazy {
        OfflineTestsRepository(AppDatabase.getDatabase(context).testDao())
    }

}