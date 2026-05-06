package com.example.quickorderapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**

 * Clase base [Application] para la aplicación de Pedidos Rápidos.

 *
 * Esta clase está anotada con [HiltAndroidApp] para activar la generación de código de Hilt,

 * que actúa como contenedor de dependencias a nivel de aplicación.

 */
@HiltAndroidApp
class QuickOrderApplication : Application()
