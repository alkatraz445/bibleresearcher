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
import com.mandk.biblereasercher.utils.Scraper
import com.mandk.biblereasercher.utils.ScraperClass

@Composable
fun ReadPage(navController: NavController, viewModel: MainViewModel = viewModel()) {

    val scrollState = ScrollState(0)

    val selectedValue1 by viewModel.topSelectionState.collectAsStateWithLifecycle()
    val selectedValue2 by viewModel.bottomSelectionState.collectAsStateWithLifecycle()

    val scraper : Scraper = ScraperClass()

    var textOnScreen1 by remember { mutableStateOf(scraper.skrapeText(selectedValue1)) }
    var textOnScreen2 by remember { mutableStateOf(scraper.skrapeText(selectedValue2)) }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
    )
    {
        SwipeCard(
            onSwipeRight =
            {
                Log.d("r", "right")
                selectedValue1.chapter = ((selectedValue1.chapter?.toInt()?: 0) - 1).toString()
                selectedValue2.chapter = ((selectedValue2.chapter?.toInt()?: 0) - 1).toString()
                viewModel.updateTopSelection(selectedValue1)
                viewModel.updateBottomSelection(selectedValue2)
                textOnScreen1 = scraper.skrapeText(selectedValue1)
                textOnScreen2 = scraper.skrapeText(selectedValue2)
            },
            onSwipeLeft =
            {
                Log.d("l", "left")
                selectedValue1.chapter = ((selectedValue1.chapter?.toInt()?: 0) + 1).toString()
                selectedValue2.chapter = ((selectedValue2.chapter?.toInt()?: 0) + 1).toString()
                viewModel.updateTopSelection(selectedValue1)
                viewModel.updateBottomSelection(selectedValue2)
                textOnScreen1 = scraper.skrapeText(selectedValue1)
                textOnScreen2 = scraper.skrapeText(selectedValue2)
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
                                text = "${selectedValue1.translation?.name}"
                            )
                            Text(
                                modifier = Modifier
                                    .verticalScroll(scrollState),
                                style = MaterialTheme.typography.displaySmall,
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
                                text = "${selectedValue2.translation?.name}"
                            )
                            Text(
                                modifier = Modifier
                                    .verticalScroll(scrollState),
                                text = textOnScreen2,
                                color = MaterialTheme.colorScheme.onSurface,
                                style = MaterialTheme.typography.displaySmall
                            )
                        }
                    }
                }
            }
        }
    }
}