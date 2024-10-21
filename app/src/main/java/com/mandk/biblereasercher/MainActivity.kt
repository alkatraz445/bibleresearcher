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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.lifecycleScope
import com.example.compose.AppTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


//import com.mandk.biblereasercher.ui.theme.BibleResearcherTheme

// State to track whether there is an internet connection
private var isConnected by mutableStateOf(false)

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            AppTheme(darkTheme = true)
            {
                if (isConnected) {
                    Navigation(remember { createDataStore(applicationContext) })  // Show the navigation screen if connected
                } else {
                    ErrorPage("No Internet Connection")  // Show the error screen if not connected
                }
            }
        }
        startNetworkCheck()
    }

    private fun startNetworkCheck() {
        lifecycleScope.launch(Dispatchers.IO) {
            while (true) {
                val connected = isOnline(this@MainActivity)
//                Log.i("NetworkCheck", "Connected: $connected")
                isConnected = connected
                delay(2000)  // Wait for 2 seconds before the next check
            }
        }
    }
}

fun isOnline(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (connectivityManager != null) {
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
//                Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
//                Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
//                Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                return true
            }
        }
    }
    return false
}

















