package com.comp304.lab3

import android.app.Application
import com.comp304.lab3.data.AppContainer
import com.comp304.lab3.data.AppDataContainer

class HospitalApplication : Application() {
    // AppContainer instance used by the rest of classes to obtain dependencies
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}