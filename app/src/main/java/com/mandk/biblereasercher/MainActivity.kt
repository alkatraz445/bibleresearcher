package com.mandk.biblereasercher

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.compose.AppTheme
import com.mandk.biblereasercher.navbar.Design
import com.mandk.biblereasercher.navbar.NavBar
import com.mandk.biblereasercher.navbar.NavigationBarDom
import it.skrape.core.htmlDocument
import it.skrape.fetcher.HttpFetcher
import it.skrape.fetcher.response
import it.skrape.fetcher.skrape

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
            AppTheme()
            {
                MyApp()
            }
        }
    }
}
@Composable
fun MyApp() {
    var selectedTab by remember { mutableIntStateOf(1) }
    val scrollState = rememberScrollState()
    AppTheme() {
        Column(modifier = Modifier.fillMaxSize()) {
            // Display the selected tab content
            when (selectedTab) {
                0 -> Tab1(scrollState)
                1 -> Tab2()
                2 -> Tab3()
            }
            Row(modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.Bottom) {
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
fun Tab1(scrollState: ScrollState) {
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
    AppTheme()
    {
        Surface(modifier = Modifier
            .fillMaxWidth())
        {
            Column (modifier = Modifier.fillMaxWidth()
                .padding(vertical = 20.dp),
                verticalArrangement = Arrangement.Top){
                Row (modifier = Modifier.fillMaxHeight(0.45f)){
                    Text(modifier = Modifier
                        .verticalScroll(scrollState),
                        text = textOnScreen)
                }

                Spacer(modifier = Modifier.padding(10.dp))

                Row (modifier = Modifier.fillMaxHeight(0.83f)){
                    Text(modifier = Modifier
                        .verticalScroll(scrollState),
                        text = textOnScreen)
                }
            }
        }
    }


}

@Composable
fun Tab2() {
    AppTheme()
    {
        Column (modifier = Modifier
            .fillMaxHeight(0.5f)
            .padding(0.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top)
        {
            Row(modifier = Modifier.fillMaxWidth())
            {

            }

        }

        Column (modifier = Modifier
            .fillMaxHeight(0.5f)
            .padding(0.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom){
            Row (modifier = Modifier.fillMaxWidth()){

            }
            Spacer(modifier = Modifier.padding(16.dp))
            Row(modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center){
                NavBar(modifier = Modifier
                    .fillMaxWidth()
                    ,
                    design = Design.Dom,
                    iconHome = {
                        Icon(
                            modifier = Modifier
                                .offset(4.dp, 2.dp),
                            painter = painterResource(R.drawable.nav_bar_home3),
                            contentDescription = null)
                    },
                    iconPismo = {
                        Icon(
                            modifier = Modifier
                                .offset(1.dp, 3.dp),
                            painter = painterResource(R.drawable.nav_bar_import_contacts),
                            contentDescription = null,
                        )
                    },
                    iconZakladki = {
                        Icon(
                            modifier = Modifier
                                .offset(4.dp, 2.dp),
                            painter = painterResource(R.drawable.nav_bar_icon1),
                            contentDescription = null,
                        )
                    },
                    iconUstawienia = {
                        Icon(
                            modifier = Modifier
                                .offset(1.dp, 2.dp),
                            painter = painterResource(R.drawable.nav_bar_icon2),
                            contentDescription = null,
                        )
                    })
            }
        }
    }


}

@Composable
fun Tab3() {

    }





