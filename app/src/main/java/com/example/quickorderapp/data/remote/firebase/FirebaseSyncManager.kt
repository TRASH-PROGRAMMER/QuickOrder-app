package com.example.quickorderapp.data.remote.firebase

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseSyncManager @Inject constructor(
    private val firebaseProductDataSource: FirebaseProductDataSource,
    private val firebaseMesaDataSource: FirebaseMesaDataSource,
    private val firebaseCategoryDataSource: FirebaseCategoryDataSource,
    private val firebaseDailyMessageDataSource: FirebaseDailyMessageDataSource,
    private val firebaseRestaurantInfoDataSource: FirebaseRestaurantInfoDataSource,
    @ApplicationContext private val context: Context
) {
    companion object {
        private const val TAG = "FirebaseSyncManager"
    }

    private val _isSyncing = MutableStateFlow(false)
    val isSyncing: StateFlow<Boolean> = _isSyncing

    private val _lastSyncTime = MutableStateFlow(0L)
    val lastSyncTime: StateFlow<Long> = _lastSyncTime

    /**
     * Realiza una sincronización completa de la nube a la base de datos local.
     */
    fun syncAll() {
        if (!isNetworkAvailable()) return
        
        _isSyncing.value = true
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Sincronizar de Firestore a Room
                firebaseProductDataSource.syncAllFromCloud()
                firebaseMesaDataSource.syncAllFromCloud()
                firebaseCategoryDataSource.syncAllFromCloud()
                firebaseDailyMessageDataSource.syncAllFromCloud()
                firebaseRestaurantInfoDataSource.syncFromCloud()
                
                _lastSyncTime.value = System.currentTimeMillis()
            } catch (e: Exception) {
                Log.e(TAG, "General sync failed: ${e.message}", e)
            } finally {
                _isSyncing.value = false
            }
        }
    }

    fun hasInternetConnection(): Boolean = isNetworkAvailable()

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) 
            as ConnectivityManager
        
        val network = connectivityManager.activeNetwork
        val capabilities = connectivityManager.getNetworkCapabilities(network)
        
        return capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
    }
}
