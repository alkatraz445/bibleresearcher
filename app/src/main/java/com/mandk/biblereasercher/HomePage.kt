package com.mandk.biblereasercher

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.mandk.biblereasercher.components.SelectionBox

@Composable
fun HomePage(navController: NavController, viewModel: MainViewModel = viewModel()) {

    val selectedValue1 by viewModel.topSelectionState.collectAsStateWithLifecycle()
    val selectedValue2 by viewModel.bottomSelectionState.collectAsStateWithLifecycle()
    val checkedBox = viewModel.checkedComparisonBox.collectAsStateWithLifecycle()

    Column(modifier = Modifier.fillMaxWidth(1f),
        horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(modifier = Modifier.padding(40.dp))

        // TODO add different way of selecting the Bible
        SelectionBox(
            selectedValue1,
            onValueChangedEvent = {
//                selectedValue1 = it
                viewModel.updateTopSelection(it)
            })

        CheckboxMinimalExample(viewModel)
        if(checkedBox.value){
            Spacer(Modifier.padding(40.dp))
            SelectionBox(
                selectedValue2,
                onValueChangedEvent = {
                    viewModel.updateBottomSelection(it)
                },
                selectedValue1)
        }

        Spacer(Modifier.padding(40.dp))

        Column(modifier = Modifier.fillMaxWidth(1f), horizontalAlignment = Alignment.CenterHorizontally) {
            Button(onClick = {
                viewModel.changeSelectedTab(1)
                navController.navigate(viewModel.topLevelRoutes[1].route) {
                    popUpTo(navController.graph.findStartDestination().id) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            })
            {
                Text("Przeczytaj")
            }

        }


    }
}

@Composable
fun CheckboxMinimalExample(viewModel: MainViewModel) {
    var checked = viewModel.checkedComparisonBox.collectAsStateWithLifecycle()

    Spacer(Modifier.padding(40.dp))
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            "Tryb Porównywania",
            style = MaterialTheme.typography.bodySmall
        )
        Checkbox(
            checked = checked.value,
            onCheckedChange = { viewModel.changeComparisonBoxState(it) }
        )
    }
}
