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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import com.mandk.biblereasercher.navbar.Design
import com.mandk.biblereasercher.navbar.NavBar
import com.mandk.biblereasercher.navigationbar.NavigationBar
import com.mandk.biblereasercher.ui.theme.BibleResearcherTheme
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

class Bible()
{
    val websiteForBible = listOf<String>(
        "http://biblia-online.pl/Biblia/Tysiaclecia/",
        "http://biblia-online.pl/Biblia/UwspolczesnionaBibliaGdanska/",
        "http://biblia-online.pl/Biblia/Warszawska/",
        "http://biblia-online.pl/Biblia/JakubaWujka/",
        "http://biblia-online.pl/Biblia/Brzeska/")
    init {
    }
}
class Werset(var text :String, var nr : String)
{
    init {
    }

    fun printWerset() : Unit
    {
        Log.d("Werset print", "" + nr + ": " + text)
    }
}
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            BibleResearcherTheme()
            {
                MyApp()
            }
        }
    }
}
@Composable
fun MyApp() {
    var selectedTab by remember { mutableIntStateOf(1) }

    BibleResearcherTheme() {
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
}

@Composable
fun Tab1() {
    val rozdzial: MutableList<Werset> = ArrayList()
    var werset = Werset(text = "", nr = "")
    var textOnScreen = ""
    skrape(HttpFetcher) {
        // make an HTTP GET request to the specified URL
        request {
            url = "http://biblia-online.pl/Biblia/Tysiaclecia/Ksiega-Rodzaju/1/1"
        }
        response {
            htmlDocument {
                "div.vr"{
                    findAll{
                        forEach{
                            val text = it.findFirst(".vtbl-txt").text
                            val nr = it.findFirst(".vtbl-num").text
                            textOnScreen += nr+ ": " + text + "\n"
                            werset = Werset(
                                text = text,
                                nr = nr
                            )
                            rozdzial.add(werset)
                            werset.printWerset()
                        }
                    }
                }
            }
        }
    }
    BibleResearcherTheme()
    {
        Surface(modifier = Modifier
            .fillMaxWidth())
        {
            Text(modifier = Modifier
                .verticalScroll(rememberScrollState()),
                text = textOnScreen)
        }
    }


}

@Composable
fun Tab2() {
    BibleResearcherTheme()
    {
        Column (modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
            horizontalAlignment = Alignment.End){
            Row {
                Text(text = "Content for Tab 2")
            }
            NavBar(design = Design.Dom,
                iconHome = {
                Icon(
                    painter = painterResource(R.drawable.nav_bar_home),
                    contentDescription = null)
            },
                iconPismo = {
                    Icon(
                        painter = painterResource(R.drawable.nav_bar_import_contacts),
                        contentDescription = null,
                    )
                },
                iconZakladki = {
                    Icon(
                        painter = painterResource(R.drawable.nav_bar_icon1),
                        contentDescription = null,
                    )
                },
                iconUstawienia = {
                    Icon(
                        painter = painterResource(R.drawable.nav_bar_icon2),
                        contentDescription = null,
                    )
                })
        }
    }


}

@Composable
fun Tab3() {
    Text(text = "Content for Tab 3")
}





