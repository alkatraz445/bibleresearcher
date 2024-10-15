package com.mandk.biblereasercher

import android.util.Log
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import it.skrape.core.htmlDocument
import it.skrape.fetcher.HttpFetcher
import it.skrape.fetcher.response
import it.skrape.fetcher.skrape

@Composable
fun ReadPage(navController: NavController) {
    val rozdzial: MutableList<Werset> = ArrayList()
    var werset: Werset
    var textOnScreen = ""
    val scrollState = ScrollState(0)

    skrape(HttpFetcher) {
        // make an HTTP GET request to the specified URL
        request {
            url = "http://biblia-online.pl/Biblia/Tysiaclecia/Ksiega-Rodzaju/1/1"
        }
        response {
            htmlDocument {
                "div.vr" {
                    findAll {
                        forEach {
                            val text = it.findFirst(".vtbl-txt").text
                            val nr = it.findFirst(".vtbl-num").text
                            textOnScreen += "$nr: $text\n"
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

    Surface(
        modifier = Modifier
            .fillMaxWidth()
    )
    {
        SwipeCard(
            onSwipeRight = { Log.d("r", "right") },
            onSwipeLeft = { Log.d("l", "left") }) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 30.dp, vertical = 30.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Surface(
                    modifier = Modifier.weight(1f),
                    shape = MaterialTheme.shapes.extraLarge,
                    color = MaterialTheme.colorScheme.secondary,
                ) {
                    Surface(
                        shape = RectangleShape,
                        color = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.padding(all = 20.dp)
                    ) {
                        Box(
                            Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .alpha(0.08f),
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurface,
                                text = "Biblia\nTysiąclecia"
                            )
                            Text(
                                modifier = Modifier
                                    .verticalScroll(scrollState),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface,
                                text = textOnScreen,
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.padding(10.dp))

                Surface(
                    modifier = Modifier.weight(1f),
                    shape = MaterialTheme.shapes.extraLarge,
                    color = MaterialTheme.colorScheme.secondary,
                ) {
                    Surface(
                        shape = RectangleShape,
                        color = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.padding(all = 20.dp)
                    ) {
                        Box(
                            Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .alpha(0.08f),
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurface,
                                text = "Biblia\nTysiąclecia"
                            )
                            Text(
                                modifier = Modifier
                                    .verticalScroll(scrollState),
                                text = textOnScreen,
                                color = MaterialTheme.colorScheme.onSurface,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }
        }
    }
}