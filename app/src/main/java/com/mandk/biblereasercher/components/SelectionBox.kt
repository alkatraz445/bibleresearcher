package com.mandk.biblereasercher.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import com.mandk.biblereasercher.Book
import com.mandk.biblereasercher.Translation
import com.mandk.biblereasercher.UserSelection
import com.mandk.biblereasercher.utils.Scraper
import com.mandk.biblereasercher.utils.ScraperClass

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectionBox(
    selectedValue: UserSelection,
    onValueChangedEvent: (UserSelection) -> Unit,
    previousSelection: UserSelection? = null
) {
    var expandedTranslationMenu by remember { mutableStateOf(false) }
    var expandedTestamentMenu by remember { mutableStateOf(false) }
    var expandedBookMenu by remember { mutableStateOf(false) }
    var expandedChapterMenu by remember { mutableStateOf(false) }
    var expandedWersetMenu by remember { mutableStateOf(false) }

    val scraper : Scraper = ScraperClass()

    val translations = scraper.scrapeForTranslations()

    val testaments: List<String> = listOf("Stary Testament", "Nowy Testament")

    Row(
        modifier = Modifier
            .fillMaxWidth(1f)
            .padding(start = 40.dp, end = 40.dp),
    ) {
        Surface(
            modifier = Modifier.weight(1f),
            shape = MaterialTheme.shapes.extraLarge,
            color = MaterialTheme.colorScheme.secondary,
        )
        {
            if (previousSelection != null) {
                ExposedDropdownMenuBox(
                    expanded = expandedTranslationMenu,
                    onExpandedChange = { expandedTranslationMenu = !expandedTranslationMenu },
//                                modifier = modifier
                ) {
                    TextField(
                        enabled = false,
                        readOnly = true,
                        singleLine = true,
                        value = "${selectedValue.translation?.name?: ""}, ${selectedValue.book?.abbrName?: ""}, ${selectedValue.chapter}",
                        onValueChange = {},
                        label = {
                            Text(
                                style = MaterialTheme.typography.labelMedium, // TODO font to be changed
                                text = "Drugie tłumaczenie"
                            )
                        },

                        colors = TextFieldDefaults.colors(
                            disabledTextColor = MaterialTheme.colorScheme.onSurface,
                            disabledLabelColor = MaterialTheme.colorScheme.onSurface,
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                            .clickable(onClick = {}),
                        textStyle = MaterialTheme.typography.labelLarge,
                    )
                    // Menu for selecting Translation (from fixed list)
                    // TODO make it scrape from the website
                    DropdownMenu(
                        expanded = expandedTranslationMenu,
                        onDismissRequest = { expandedTranslationMenu = false },
                        properties = PopupProperties(
                            focusable = true,
                            dismissOnClickOutside = true,
                            dismissOnBackPress = true
                        ),
                        modifier = Modifier.exposedDropdownSize()
                    )
                    {
                        translations.forEach { option: Translation ->
                            DropdownMenuItem(
                                text = { Text(text = option.name?: "") },
                                onClick = {
                                    expandedTranslationMenu = false
                                    selectedValue.translation = option

                                    expandedTestamentMenu = true
                                }
                            )
                        }
                    }
                    selectedValue.testament = previousSelection.testament
                    val secondBookOptions = scraper.scrapeForBook(selectedValue.translation?.url ?:"", selectedValue.testament?:"")
                    selectedValue.book = secondBookOptions[previousSelection.book?.index?: 0]
                    selectedValue.chapter = previousSelection.chapter
                    onValueChangedEvent(selectedValue)
                }
            }
            else
            {
                ExposedDropdownMenuBox(
                    expanded = expandedTranslationMenu,
                    onExpandedChange = { expandedTranslationMenu = !expandedTranslationMenu },
//                                modifier = modifier
                ) {
                    var bookOptions : MutableList<Book>
                    var chapterOptions: MutableList<String>

                    TextField(
                        enabled = false,
                        readOnly = true,
                        singleLine = true,
                        value = "${selectedValue.translation?.name ?:""}, ${selectedValue.book?.abbrName?: ""}, ${selectedValue.chapter}",
                        onValueChange = {},
                        label = {
                            Text(
                                style = MaterialTheme.typography.labelMedium, // TODO font to be changed
                                text = "Pierwsze tłumaczenie"
                            )
                        },

                        colors = TextFieldDefaults.colors(
                            disabledTextColor = MaterialTheme.colorScheme.onSurface,
                            disabledLabelColor = MaterialTheme.colorScheme.onSurface,
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                            .clickable(onClick = {}),
                        textStyle = MaterialTheme.typography.labelLarge,

                        )
                    // Menu for selecting Translation (from fixed list)
                    // TODO make it scrape from the website
                    DropdownMenu(
                        expanded = expandedTranslationMenu,
                        onDismissRequest = { expandedTranslationMenu = false },
                        properties = PopupProperties(
                            focusable = true,
                            dismissOnClickOutside = true,
                            dismissOnBackPress = true
                        ),
                        modifier = Modifier.exposedDropdownSize()
                    )
                    {
                        translations.forEach() { option: Translation ->
                            DropdownMenuItem(
                                text = { Text(text = option.name?: "") },
                                onClick = {
                                    expandedTranslationMenu = false
                                    selectedValue.translation = option

                                    expandedTestamentMenu = true
                                }
                            )
                        }
                    }
                    // Menu for choice of testament
                    DropdownMenu(
                        expanded = expandedTestamentMenu,
                        onDismissRequest = { expandedTestamentMenu = false },
                        properties = PopupProperties(
                            focusable = true,
                            dismissOnClickOutside = true,
                            dismissOnBackPress = true
                        ),
                        modifier = Modifier.exposedDropdownSize()
                    )
                    {
                        testaments.forEach { option: String ->
                            DropdownMenuItem(
                                text = { Text(text = option) },
                                onClick = {
                                    expandedTestamentMenu = false
                                    selectedValue.testament = option
                                    expandedBookMenu = true
                                }
                            )
                        }
                    }
                    // Menu for choice of book from the translation
                    DropdownMenu(
                        expanded = expandedBookMenu,
                        onDismissRequest = { expandedBookMenu = false },
                        properties = PopupProperties(
                            focusable = true,
                            dismissOnClickOutside = true,
                            dismissOnBackPress = true
                        ),
                        modifier = Modifier.exposedDropdownSize()
                    )
                    {
                        bookOptions = scraper.scrapeForBook(selectedValue.translation?.url?: "", selectedValue.testament?: "")
                        bookOptions.forEach { option: Book ->
                            DropdownMenuItem(
                                text = { Text(text = option.fullName?: "") },
                                onClick = {
                                    expandedBookMenu = false
                                    selectedValue.book = option
                                    expandedChapterMenu = true
                                }
                            )
                        }
                    }
                    // menu to choose chapter
                    DropdownMenu(
                        expanded = expandedChapterMenu,
                        onDismissRequest = { expandedChapterMenu = false },
                        properties = PopupProperties(
                            focusable = true,
                            dismissOnClickOutside = true,
                            dismissOnBackPress = true
                        ),
                        modifier = Modifier.exposedDropdownSize()
                    )
                    {
                        chapterOptions = scraper.scrapeForChapters(selectedValue.book?.url?: "")

                        chapterOptions.forEach { option: String ->
                            DropdownMenuItem(
                                text = { Text(text = option) },
                                onClick = {
                                    expandedChapterMenu = false
                                    selectedValue.chapter = option
                                    onValueChangedEvent(selectedValue)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}