package com.mandk.biblereasercher.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.mandk.biblereasercher.MainViewModel
import com.mandk.biblereasercher.ReaderScreen
import com.mandk.biblereasercher.utils.Bookmark
import kotlinx.coroutines.launch

@Composable
fun BookmarkList(modifier: Modifier = Modifier, viewModel: MainViewModel, navController: NavController){

    val allBookmarks by viewModel.allBookmarks.collectAsStateWithLifecycle(initialValue = emptyList())

    Box {
        Column(
            modifier = modifier
                .padding(vertical = 8.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        )
        {
            if (allBookmarks.isEmpty()) {
                Text("Brak zakładek", modifier = Modifier.padding(vertical = 8.dp), lineHeight = 50.sp)
                Text("Aby dodać zakładkę kliknij przycisk akcji podczas czytania tekstu", fontSize = 14.sp)
            } else {
                // index needed to remove bookmark by index
                allBookmarks.forEach { bookmark ->
                    // TODO instead of row it needs to be a card with (-) button on the right and some sort of a divider
                    BookmarkCard(navController, viewModel, bookmark)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
        }
    }

@Composable
fun BookmarkPage(navController: NavController, viewModel: MainViewModel) {
    BookmarkList(modifier = Modifier, viewModel, navController)
}

@Composable
fun BookmarkCard(navController: NavController, viewModel : MainViewModel, bookmark: Bookmark)
{


    Row(modifier = Modifier
        .fillMaxWidth()
        .background(color = MaterialTheme.colorScheme.secondary),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
        )
    {
        Text(
            text = "${bookmark.translationName}, ${bookmark.bookAbbrName}, ${bookmark.chapter}",
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.align(Alignment.CenterVertically)
                .padding(start = 10.dp)
                .clickable {
                    viewModel.changeComparisonBoxState(false)
                    viewModel.updateTopSelection(bookmark)
                    viewModel.updateBottomSelection(bookmark)
                    viewModel.changeSelectedTab(1)
                    navController.navigate(ReaderScreen)
                    {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
        )
        IconButton(
            modifier = Modifier.alignByBaseline(), // Aligns text by baseline
            onClick = {
//                Log.d("deleting ",)
                viewModel.viewModelScope.launch{viewModel.removeBookmark(bookmark.id) }
            }
        ) {
            Icon(Icons.Outlined.Delete, contentDescription = "delete_bookmark")
        }
    }
}

//@Preview(showBackground = true, widthDp = 320)
//@Composable
//fun BookmarkListPreview() {
//    BookmarkList(modifier = Modifier, viewModel())
//}