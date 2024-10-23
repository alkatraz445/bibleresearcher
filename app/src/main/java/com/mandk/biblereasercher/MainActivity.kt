@file:OptIn(ExperimentalMaterial3Api::class)

package com.mandk.biblereasercher

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.lifecycleScope
import com.example.compose.AppTheme
import com.mandk.biblereasercher.utils.ConnectivityObserver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


//import com.mandk.biblereasercher.ui.theme.BibleResearcherTheme



class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        TODO: this does not work, and crashes the app
        val connectivityObserver = ConnectivityObserver(this)
        enableEdgeToEdge()
        setContent {

//            TODO: move to Navigation.kt
            AppTheme(darkTheme = false)
            {
                if (true) {
                    Navigation(applicationContext)  // Show the navigation screen if connected
                } else {
                    ErrorPage("Brak połączenia z internetem")  // Show the error screen if not connected
                }
            }
            connectivityObserver.startObserving()
        }
    }
}
















