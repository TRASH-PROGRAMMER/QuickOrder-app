package com.example.quickorderapp.data.local

import android.content.Context
import android.net.Uri
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Copia una imagen seleccionada (content://) al almacenamiento interno de la
 * app. El URI entregado por el selector de galeria es temporal y deja de ser
 * accesible cuando el proceso de la app muere, por eso se copia a un archivo
 * propio de la app cuya ruta persiste entre reinicios.
 */
@Singleton
class LocalImageDataSource @Inject constructor(
    @ApplicationContext private val context: Context
) {
    suspend fun copyToInternalStorage(source: Uri): String? = withContext(Dispatchers.IO) {
        try {
            val dir = File(context.filesDir, "productos").apply { if (!exists()) mkdirs() }
            val file = File(dir, "${UUID.randomUUID()}.jpg")
            context.contentResolver.openInputStream(source)?.use { input ->
                file.outputStream().use { output -> input.copyTo(output) }
            }
            Uri.fromFile(file).toString()
        } catch (e: Exception) {
            null
        }
    }
}
