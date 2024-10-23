package com.mandk.biblereasercher

import android.util.Log
import android.widget.Space
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import it.skrape.core.htmlDocument
import it.skrape.fetcher.HttpFetcher
import it.skrape.fetcher.response
import it.skrape.fetcher.skrape
import it.skrape.selects.html5.a
import it.skrape.selects.html5.div

@Composable
fun HomePage(navController: NavController, viewModel: MainViewModel = viewModel()) {
    val websiteForBible = "http://biblia-online.pl/Biblia/"
    val translations = listOf(
        "Tysiaclecia",
        "UwspolczesnionaBibliaGdanska",
        "Warszawska",
        "JakubaWujka",
        "Brzeska"
    )

    val selectedValue1 by viewModel.topSelectionState.collectAsStateWithLifecycle()
    val selectedValue2 by viewModel.bottomSelectionState.collectAsStateWithLifecycle()


    Column(modifier = Modifier.fillMaxWidth(1f)) {
        Spacer(modifier = Modifier.padding(40.dp))

        // TODO add different way of selecting the Bible
        SelectionBox(
            selectedValue1,
            translations,
            onValueChangedEvent = {
//                selectedValue1 = it
                viewModel.updateTopSelection(it)
            })
        Spacer(Modifier.padding(40.dp))

        SelectionBox(
            selectedValue2,
            translations,
            onValueChangedEvent = {
                viewModel.updateBottomSelection(it)
            },
            selectedValue1)
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectionBox(
    selectedValue: UserSelection,
    options: List<String>,
    onValueChangedEvent: (UserSelection) -> Unit,
    previousSelection: UserSelection? = null
) {
    var expandedTranslationMenu by remember { mutableStateOf(false) }
    var expandedTestamentMenu by remember { mutableStateOf(false) }
    var expandedBookMenu by remember { mutableStateOf(false) }
    var expandedChapterMenu by remember { mutableStateOf(false) }
    var expandedWersetMenu by remember { mutableStateOf(false) }

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
                        value = "${selectedValue.translation}, ${selectedValue.book?.abbrName?: ""}, ${selectedValue.chapter}",
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
                        options.forEach { option: String ->
                            DropdownMenuItem(
                                text = { Text(text = option) },
                                onClick = {
                                    expandedTranslationMenu = false
                                    selectedValue.translation = option

                                    expandedTestamentMenu = true
                                }
                            )
                        }
                    }
                    selectedValue.testament = previousSelection.testament
                    val secondBookOptions = scrapeForBook(selectedValue.translation ?:"", selectedValue.testament?:"")
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
                    var bookOptions : MutableList<Book> = ArrayList()
                    val chapterOptions: MutableList<String> = ArrayList()

                    TextField(
                        enabled = false,
                        readOnly = true,
                        singleLine = true,
                        value = "${selectedValue.translation}, ${selectedValue.book?.abbrName?: ""}, ${selectedValue.chapter}",
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
                        options.forEach { option: String ->
                            DropdownMenuItem(
                                text = { Text(text = option) },
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
                        bookOptions = scrapeForBook(selectedValue.translation?: "", selectedValue.testament?: "")
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
                        // create list of chapters according to the translation
                        try {
                            skrape(HttpFetcher)
                            {
                                request {
                                    url = "http://biblia-online.pl/${selectedValue.book?.url?: ""}"
                                    timeout = 5000
                                }
                                response {
                                    htmlDocument {
                                        div {
                                            withClass = "mnav-chptrs-cell"
                                            findFirst {
                                                this.a {
                                                    findAll {
                                                        forEach {
                                                            chapterOptions.add(it.text)
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        catch (e: Exception)
                        {
                            ErrorPage(e.message)
                        }

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

@Composable
fun scrapeForBook(translation: String, testament: String) : MutableList<Book> {
    val bookOptions: MutableList<Book> = ArrayList()
    // create list of chapters according to the translation
    try{
        skrape(HttpFetcher)
        {
            request {
                if(translation!="")
                    url = "http://biblia-online.pl/Biblia/ListaKsiag/${translation}/"
                else
                    return@request
            }
            response {
                htmlDocument {
                    div {
                        withId = "ot-books-list"
                        if (testament == "Stary Testament") {
                            findFirst {
                                this.div {
                                    withClass = "book-list-item"
                                    findAll {
                                        forEachIndexed { index, it ->
                                            val fullBookName =
                                                it.findFirst("a").text
                                            val abbrBookName =
                                                it.findFirst("span").text
                                            val url =
                                                it.findFirst("a").attribute("href")
                                            bookOptions.add(
                                                Book(
                                                    index,
                                                    fullBookName,
                                                    abbrBookName,
                                                    url
                                                )
                                            )
                                        }
                                    }
                                }
                            }
                        } else {
                            findSecond {
                                this.div {
                                    withClass = "book-list-item"
                                    findAll {
                                        forEachIndexed { index, it ->
                                            val fullBookName =
                                                it.findFirst("a").text
                                            val abbrBookName =
                                                it.findFirst("span").text
                                            val url =
                                                it.findFirst("a").attribute("href")
                                            bookOptions.add(
                                                Book(
                                                    index,
                                                    fullBookName,
                                                    abbrBookName,
                                                    url
                                                )
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    catch (e: Exception)
    {
        e.message?.let { Log.d("error scraping", it) }
        ErrorPage(e.message)
    }
    return bookOptions
}

