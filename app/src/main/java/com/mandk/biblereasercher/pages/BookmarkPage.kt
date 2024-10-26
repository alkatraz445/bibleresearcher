package com.mandk.biblereasercher.pages

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.mandk.biblereasercher.MainViewModel

@Composable
fun BookmarkList(modifier: Modifier = Modifier, viewModel: MainViewModel){

    val allBookmarks by viewModel.allBookmarks.collectAsStateWithLifecycle(initialValue = emptyList())

    Box {
        Column(modifier = modifier.padding(vertical = 8.dp)) {
            if (allBookmarks.isEmpty()) {
                Text("Brak zakładek", modifier = Modifier.padding(vertical = 8.dp), lineHeight = 50.sp)
                Text("Aby dodać zakładkę kliknij przycisk akcji podczas czytania tekstu", fontSize = 14.sp)
            } else {
                allBookmarks.forEach { bookmark ->
                    Log.d("bookmark", bookmark.bookAbbrName?:"")
                    Row {
                        Text(
                            bookmark.bookAbbrName?:"",
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                        Spacer(modifier = Modifier.padding(8.dp))
                        IconButton(onClick = TODO()) { }
                    }
                }
            }
        }
        }
    }

@Composable
fun BookmarkPage(navController: NavController, viewModel: MainViewModel) {
    val topSelection by viewModel.topSelectionState.collectAsStateWithLifecycle()
    val bottomSelection by viewModel.bottomSelectionState.collectAsStateWithLifecycle()
    BookmarkList(modifier = Modifier, viewModel)
}

@Preview(showBackground = true, widthDp = 320)
@Composable
fun BookmarkListPreview() {
    BookmarkList(modifier = Modifier, viewModel())
}