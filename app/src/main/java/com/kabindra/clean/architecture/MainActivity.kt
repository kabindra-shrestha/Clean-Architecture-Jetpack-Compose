package com.kabindra.clean.architecture

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.kabindra.clean.architecture.presentation.ui.theme.JetpackComposeCleanArchitectureTheme
import com.kabindra.clean.architecture.utils.handleNavigationRedirection
import com.kabindra.inappupdate.initializeUpdateManager

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        installSplashScreen()

        println("Firebase handleIntent onCreate")
        handleIntent(intent)

        initializeUpdateManager(this)

        setContent {
            JetpackComposeCleanArchitectureTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    App(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        // Handle the new intent (in case the activity is already running)
        println("Firebase handleIntent onNewIntent")
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent?) {
        intent?.extras?.let { extras ->
            val pageId = extras.getString("page_id") ?: ""

            if (pageId.isNotEmpty()) {
                handleNavigationRedirection(
                    pageId = pageId
                )
            }
        }
    }
}