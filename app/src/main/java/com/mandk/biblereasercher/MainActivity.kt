package com.mandk.biblereasercher

import android.os.Bundle
import android.util.Log
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
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import it.skrape.core.htmlDocument
import it.skrape.core.htmlDocument
import it.skrape.fetcher.HttpFetcher
import it.skrape.fetcher.extract
import it.skrape.fetcher.response
import it.skrape.fetcher.skrape
import it.skrape.selects.html5.div
import it.skrape.selects.html5.h3
import it.skrape.selects.html5.img
import it.skrape.selects.html5.li
import it.skrape.selects.html5.ol
import it.skrape.core.*
import it.skrape.fetcher.*

//import com.mandk.biblereasercher.ui.theme.BibleResearcherTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            MyApp()
        }
    }
}
@Composable
fun MyApp() {
    var selectedTab by remember { mutableIntStateOf(0) }

    Column(modifier = Modifier.fillMaxSize()) {
        // Display the selected tab content
        when (selectedTab) {
            0 -> Tab1()
            1 -> Tab2()
            2 -> Tab3()
        }
            Row(modifier = Modifier.fillMaxSize()) {
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
        // Tab buttons
    }
}

@Composable
fun Tab1() {
    skrape(HttpFetcher) {
        // make an HTTP GET request to the specified URL
        request {
            url = "https://quotes.toscrape.com/"
        }
        response {
            htmlDocument {
                Log.d("body", html )
            }
        }
    }
}

@Composable
fun Tab2() {
    Text(text = "Content for Tab 2")
}

@Composable
fun Tab3() {
    Text(text = "Content for Tab 3")
}

//private suspend fun fetchData() : String
//{
//    skrape(HttpFetcher)
//    {
//        request {
//            url = "http://biblia-online.pl/Biblia/Tysiaclecia"
//        }
//        response {
//            htmlDocument {
//                div{
//                    withId = "vt1"
//                    findFirst(text)
//                }
//                }
//            }
//        }
//}



