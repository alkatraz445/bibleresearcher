package com.mandk.biblereasercher

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

import com.mandk.biblereasercher.ui.theme.BibleResearcherTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BibleResearcherTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    BibleResearcherTheme {
        Greeting("Android")
    }
}
@Composable
fun MyApp() {
    var selectedTab by remember { mutableStateOf(0) }

    Column(modifier = Modifier.fillMaxSize()) {
        // Display the selected tab content
        when (selectedTab) {
            0 -> Tab1()
            1 -> Tab2()
            2 -> Tab3()
        }

        // Tab buttons
        Row {
            Button(onClick = { selectedTab = 0 }) {
                Text(text = "Tab 1")
            }
            Button(onClick = { selectedTab = 1 }) {
                Text(text = "Tab 2")
            }
            Button(onClick = { selectedTab = 2 }) {
                Text(text = "Tab 3")
            }
        }
    }
}

@Composable
fun Tab1() {
    Text(text = "Content for Tab 1")
}

@Composable
fun Tab2() {
    Text(text = "Content for Tab 2")
}

@Composable
fun Tab3() {
    Text(text = "Content for Tab 3")
}