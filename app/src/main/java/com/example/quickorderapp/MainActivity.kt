package com.example.quickorderapp
import androidx.activity.compose.setContent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.quickorderapp.ui.navigation.NavGraph
import com.example.quickorderapp.ui.theme.QuickOrderAppTheme
import com.example.quickorderapp.viewmodel.SplashViewModel
import dagger.hilt.android.AndroidEntryPoint
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        val splashScreen = installSplashScreen()

        super.onCreate(savedInstanceState)

        setContent {
            val viewModel: SplashViewModel = hiltViewModel()
            val isReady by viewModel.isReady.collectAsState()

            splashScreen.setKeepOnScreenCondition {
                !isReady
            }

            val startDestination by viewModel.startDestination.collectAsState()

            NavGraph(startDestination = startDestination)
        }
    }
}

