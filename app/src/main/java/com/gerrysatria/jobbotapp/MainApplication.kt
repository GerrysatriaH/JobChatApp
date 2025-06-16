package com.gerrysatria.jobbotapp

import android.app.Application
import android.content.res.Configuration
import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import com.gerrysatria.jobbotapp.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        applyLightMode()
        startKoin {
            androidLogger()
            androidContext(this@MainApplication)
            modules(appModule)
        }
    }

    private fun applyLightMode(){
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            val isDarkMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
            AppCompatDelegate.setDefaultNightMode(if (isDarkMode) AppCompatDelegate.MODE_NIGHT_NO else AppCompatDelegate.MODE_NIGHT_NO)
        }
    }
}