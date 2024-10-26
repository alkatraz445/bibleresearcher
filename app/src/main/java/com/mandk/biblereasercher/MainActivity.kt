@file:OptIn(ExperimentalMaterial3Api::class)

package com.mandk.biblereasercher

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.ExperimentalMaterial3Api
import com.mandk.biblereasercher.components.ConnectivityStatus
import com.mandk.biblereasercher.pages.ErrorPage
import com.mandk.biblereasercher.utils.ConnectivityObserver
import com.mandk.biblereasercher.utils.NetworkConnectivityObserver


//import com.mandk.biblereasercher.ui.theme.BibleResearcherTheme



class MainActivity : ComponentActivity() {
    private lateinit var connectivityObserver: ConnectivityObserver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        TODO: this does not work, and crashes the app
        connectivityObserver = NetworkConnectivityObserver(application = application)
        enableEdgeToEdge()
        setContent {
//            TODO: move to Navigation.kt
            if (connectivityObserver.isConnected() == true) {
                ConnectivityStatus(connectivityObserver.isConnected())
                Navigation(applicationContext)  // Show the navigation screen if connected
            } else {
                ErrorPage("Brak połączenia z internetem")  // Show the error screen if not connected
            }

        }
    }
}
















