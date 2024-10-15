package com.mandk.biblereasercher

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePage(navController: NavController) {
        val translations = listOf("Tysiąclecia", "Wujka")
        val selectedValue = remember{ mutableStateOf("Tysiąclecia")}
        Column(modifier = Modifier.fillMaxWidth(1f)) {
                DynamicSelectTextField(selectedValue.value, translations, "Tłumaczenie 1",
                        onValueChangedEvent = {
                                selectedValue.value = it
                        })
                Spacer(modifier = Modifier.padding(20.dp))
                // TODO add different way of selecting the Bible
                SelectionBox(
                        selectedValue.value,
                        translations,
                        onValueChangedEvent = {
                        selectedValue.value = it
                })

        }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectionBox(
        selectedValue: String,
        options: List<String>,
        onValueChangedEvent: (String) -> Unit,)
{
        var expanded by remember { mutableStateOf(false) }
        var textFieldSize by remember { mutableStateOf(Size.Zero)}

        Row(modifier = Modifier
                .fillMaxWidth(1f)
                .padding(start = 40.dp, end = 40.dp),
                ) {
                Surface(
                        modifier = Modifier.weight(1f),
                        shape = MaterialTheme.shapes.extraLarge,
                        color = MaterialTheme.colorScheme.secondary,
                )
                {
                        ExposedDropdownMenuBox(
                                expanded = expanded,
                                onExpandedChange = { expanded = !expanded },
//                                modifier = modifier
                        )  {
                                TextField(
                                        enabled = false,
                                        readOnly = true,
                                        singleLine = true,
                                        value = selectedValue,
                                        onValueChange = {},
                                        label = {
                                                Text(
                                                        style = MaterialTheme.typography.labelMedium, // TODO font to be changed
                                                        text = "label"
                                                )
                                        },

                                        colors = TextFieldDefaults.colors(
                                                disabledTextColor = MaterialTheme.colorScheme.onSurface,
                                                disabledLabelColor = MaterialTheme.colorScheme.onSurface,

                                                ),
                                        modifier = Modifier
                                                .fillMaxWidth()
                                                .menuAnchor()
                                                .clickable(onClick = {
                                                        Log.d("Clicked txtfield", "log")
                                                }),
                                        textStyle = MaterialTheme.typography.labelLarge,

                                        )
                                DropdownMenu(
                                        expanded = expanded,
                                        onDismissRequest = { expanded = false },
                                        properties = PopupProperties(
                                                focusable = true,
                                                dismissOnClickOutside = true,
                                                dismissOnBackPress = true
                                        ),
                                        modifier = Modifier.exposedDropdownSize()
                                        )
                                {
                                        options.forEach { option: String ->
                                                DropdownMenuItem(
                                                        text = { Text(text = option) },
                                                        onClick = {
                                                                expanded = false
                                                                onValueChangedEvent(option)
                                                        }
                                                )
                                        }
                                }
                        }
                }
        }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DynamicSelectTextField(
        selectedValue: String,
        options: List<String>,
        label: String,
        onValueChangedEvent: (String) -> Unit,
        modifier: Modifier = Modifier
) {
        var expanded1 by remember { mutableStateOf(false) }
        var expanded2 by remember { mutableStateOf(false) }


        ExposedDropdownMenuBox(
                expanded = expanded1,
                onExpandedChange = { expanded1 = !expanded1 },
                modifier = modifier
        ) {
                OutlinedTextField(
                        readOnly = true,
                        singleLine = true,
                        value = selectedValue,
                        onValueChange = {},
                        label = { Text(
                                style = MaterialTheme.typography.labelMedium, // TODO font to be changed
                                text = label) },
                        trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded1)
                        },
                        colors = OutlinedTextFieldDefaults.colors(),
                        modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth(),
                        textStyle = MaterialTheme.typography.labelLarge

                )

                ExposedDropdownMenu(expanded = expanded1, onDismissRequest = { expanded1 = false }) {
                        options.forEach { option: String ->
                                DropdownMenuItem(
                                        text = { Text(text = option) },
                                        onClick = {
                                                expanded1 = false
                                                onValueChangedEvent(option)
                                        }
                                )
                        }
                }
        }

        Spacer(modifier = Modifier.size(60.dp))

        ExposedDropdownMenuBox(
                expanded = expanded2,
                onExpandedChange = { expanded2 = !expanded2 },
                modifier = modifier
        ) {
                OutlinedTextField(
                        readOnly = true,
                        singleLine = true,
                        value = selectedValue,
                        onValueChange = {},
                        label = { Text(
                                style = MaterialTheme.typography.labelMedium, // TODO font to be changed
                                text = label) },
                        trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded2)
                        },
                        colors = OutlinedTextFieldDefaults.colors(),
                        modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth(),
                        textStyle = MaterialTheme.typography.labelLarge
                )

                ExposedDropdownMenu(expanded = expanded2, onDismissRequest = { expanded2 = false }) {
                        options.forEach { option: String ->
                                DropdownMenuItem(
                                        text = { Text(text = option) },
                                        onClick = {
                                                expanded2 = false
                                                onValueChangedEvent(option)
                                        }
                                )
                        }
                }
        }
}

