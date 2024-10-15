@file:OptIn(ExperimentalMaterial3Api::class)

package com.mandk.biblereasercher

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.ExperimentalMaterial3Api
import com.example.compose.AppTheme

//import com.mandk.biblereasercher.ui.theme.BibleResearcherTheme


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            AppTheme(darkTheme = true)
            {
                Navigation()
            }
        }
    }
}















