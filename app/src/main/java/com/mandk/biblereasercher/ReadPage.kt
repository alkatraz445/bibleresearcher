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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import it.skrape.core.htmlDocument
import it.skrape.fetcher.HttpFetcher
import it.skrape.fetcher.response
import it.skrape.fetcher.skrape

fun skrapeText(selectedValue : UserSelection) : String {
    var textOnScreen = ""
    skrape(HttpFetcher) {
        // make an HTTP GET request to the specified URL
        request {
            // TODO Has to be dynamically changed by the user
            val temp = "http://biblia-online.pl/${selectedValue.book.url}"
            url = temp.substring(0, temp.length - 4) + "/${selectedValue.chapter}"
            Log.d("connected at", url)
        }
        response {
            htmlDocument {
                "div.vr" {
                    findAll {
                        forEach {
                            val text = it.findFirst(".vtbl-txt").text
                            val nr = it.findFirst(".vtbl-num").text
                            textOnScreen += "$nr: $text\n"
//                            werset = Werset(
//                                text = text,
//                                nr = nr
//                            )
//                            rozdzial.add(werset)
//                            werset.printWerset()
                        }
                    }
                }
            }
        }
    }
    return textOnScreen
}

@Composable
fun ReadPage(navController: NavController, viewModel: MainViewModel = viewModel()) {
//    val rozdzial: MutableList<Werset> = ArrayList()
//    var werset: Werset
    val scrollState = ScrollState(0)

    val selectedValue1 by viewModel.topSelectionState.collectAsStateWithLifecycle()
    val selectedValue2 by viewModel.bottomSelectionState.collectAsStateWithLifecycle()

    var textOnScreen1 by remember { mutableStateOf(skrapeText(selectedValue1)) }
    var textOnScreen2 by remember { mutableStateOf(skrapeText(selectedValue2)) }
//    skrape(HttpFetcher) {
//        // make an HTTP GET request to the specified URL
//        request {
//            // TODO Has to be dynamically changed by the user
//            val temp = "http://biblia-online.pl/${selectedValue1.book.url}"
//            url = temp.substring(0, temp.length-4) + "/${selectedValue1.chapter}"
//            Log.d("connected at", url)
//        }
//        response {
//            htmlDocument {
//                "div.vr" {
//                    findAll {
//                        forEach {
//                            val text = it.findFirst(".vtbl-txt").text
//                            val nr = it.findFirst(".vtbl-num").text
//                            textOnScreen1 += "$nr: $text\n"
////                            werset = Werset(
////                                text = text,
////                                nr = nr
////                            )
////                            rozdzial.add(werset)
////                            werset.printWerset()
//                        }
//                    }
//                }
//            }
//        }
//        request {
//            // TODO Has to be dynamically changed by the user
//            val temp = "http://biblia-online.pl/${selectedValue2.book.url}"
//            url = temp.substring(0, temp.length-4) + "/${selectedValue2.chapter}"
//            Log.d("connected at", url)
//        }
//        response {
//            htmlDocument {
//                "div.vr" {
//                    findAll {
//                        forEach {
//                            val text = it.findFirst(".vtbl-txt").text
//                            val nr = it.findFirst(".vtbl-num").text
//                            textOnScreen2 += "$nr: $text\n"
////                            werset = Werset(
////                                text = text,
////                                nr = nr
////                            )
////                            rozdzial.add(werset)
////                            werset.printWerset()
//                        }
//                    }
//                }
//            }
//        }
//    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
    )
    {
        SwipeCard(
            onSwipeRight =
            {
                Log.d("r", "right")
                selectedValue1.chapter = (selectedValue1.chapter.toInt() - 1).toString()
                selectedValue2.chapter = (selectedValue2.chapter.toInt() - 1).toString()
                viewModel.updateTopSelection(selectedValue1)
                viewModel.updateBottomSelection(selectedValue2)
                textOnScreen1 =  skrapeText(selectedValue1)
                textOnScreen2 = skrapeText(selectedValue2)
            },
            onSwipeLeft =
            {
                Log.d("l", "left")
                selectedValue1.chapter = (selectedValue1.chapter.toInt() + 1).toString()
                selectedValue2.chapter = (selectedValue2.chapter.toInt() + 1).toString()
                viewModel.updateTopSelection(selectedValue1)
                viewModel.updateBottomSelection(selectedValue2)
                textOnScreen1 =  skrapeText(selectedValue1)
                textOnScreen2 = skrapeText(selectedValue2)
            })
        {


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
                                text = "Biblia ${selectedValue1.translation}"
                            )
                            Text(
                                modifier = Modifier
                                    .verticalScroll(scrollState),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface,
                                text = textOnScreen1,
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
                                text = "Biblia ${selectedValue2.translation}"
                            )
                            Text(
                                modifier = Modifier
                                    .verticalScroll(scrollState),
                                text = textOnScreen2,
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