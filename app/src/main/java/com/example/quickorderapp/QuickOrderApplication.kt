package com.example.quickorderapp

import android.app.Application
import com.example.quickorderapp.data.remote.firebase.FirebaseSyncManager
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class QuickOrderApplication : Application() {
    
    @Inject
    lateinit var syncManager: FirebaseSyncManager
    
    override fun onCreate() {
        super.onCreate()
        // Iniciar sincronización automática al arrancar la app
        syncManager.syncAll()
    }
}

