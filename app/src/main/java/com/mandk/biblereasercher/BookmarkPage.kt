package com.mandk.biblereasercher

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.compose.AppTheme

@Composable
fun BookmarkPage(navController: NavController, viewModel: MainViewModel = viewModel()) {
    val topSelection by viewModel.topSelectionState.collectAsStateWithLifecycle()
    val bottomSelection by viewModel.bottomSelectionState.collectAsStateWithLifecycle()

    AppTheme()
    {
        Column(
            modifier = Modifier
                .fillMaxHeight(0.5f)
                .padding(0.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        )
        {
            Row(modifier = Modifier.fillMaxWidth())
            {

            }

        }
        Column(
            modifier = Modifier
                .fillMaxHeight(0.5f)
                .padding(0.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(text = "Top selection: ${topSelection.translation}, ${topSelection.book.abbrName}, ${topSelection.chapter}")
            }
            Spacer(modifier = Modifier.padding(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(text = "Bottom selection: ${bottomSelection.translation}, ${bottomSelection.book.abbrName}, ${bottomSelection.chapter}")
            }
        }
    }
}