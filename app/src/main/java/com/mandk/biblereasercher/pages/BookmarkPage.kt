package com.mandk.biblereasercher.pages

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
import com.example.compose.AppTheme
import com.mandk.biblereasercher.MainViewModel

val Bookmarks: List<String> = emptyList()

@Composable
fun BookmarkList(modifier: Modifier = Modifier, bookmarks: List<String> ){
    Box {
        Column(modifier = modifier.padding(vertical = 8.dp)) {
            if (bookmarks.isEmpty()) {
                Text("Brak zakładek", modifier = Modifier.padding(vertical = 8.dp), lineHeight = 50.sp)
                Text("Aby dodać zakładkę kliknij przycisk akcji podczas czytania tekstu", fontSize = 14.sp)
            } else {
                bookmarks.forEach { bookmark ->
                    Row {
                        Text(
                            bookmark,
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
fun BookmarkPage(navController: NavController, viewModel: MainViewModel = viewModel()) {
    val topSelection by viewModel.topSelectionState.collectAsStateWithLifecycle()
    val bottomSelection by viewModel.bottomSelectionState.collectAsStateWithLifecycle()

    AppTheme()
    {
        BookmarkList(modifier = Modifier, bookmarks = Bookmarks)
//        Column(
//            modifier = Modifier
//                .fillMaxHeight(0.5f)
//                .padding(0.dp),
//            horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.Top
//        )
//        {
//            Row(modifier = Modifier.fillMaxWidth())
//            {
//
//            }
//
//        }
//        Column(
//            modifier = Modifier
//                .fillMaxHeight(0.5f)
//                .padding(0.dp),
//            horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.Bottom
//        ) {
//            Row(modifier = Modifier.fillMaxWidth()) {
//                Text(text = "Top selection: ${topSelection.translation}, ${topSelection.book?.abbrName}, ${topSelection.chapter}")
//            }
//            Spacer(modifier = Modifier.padding(16.dp))
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                verticalAlignment = Alignment.CenterVertically,
//                horizontalArrangement = Arrangement.Center
//            ) {
//                Text(text = "Bottom selection: ${bottomSelection.translation}, ${bottomSelection.book?.abbrName}, ${bottomSelection.chapter}")
//            }
//        }
    }
}

@Preview(showBackground = true, widthDp = 320)
@Composable
fun BookmarkListPreview() {
    BookmarkList(modifier = Modifier,listOf("1","2","3"))

}