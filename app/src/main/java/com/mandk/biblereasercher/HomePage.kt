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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import androidx.navigation.NavController
import it.skrape.core.htmlDocument
import it.skrape.fetcher.HttpFetcher
import it.skrape.fetcher.response
import it.skrape.fetcher.skrape
import it.skrape.selects.html5.a
import it.skrape.selects.html5.div

data class Book(
        val fullName: String,
        val abbrName: String,
        val url : String? = null
)

data class UserSelection(
        var translation : String,
        var testament : String,
        var book : Book,
        var chapter: String = "1",
        val werset : Int? = 1
)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePage(navController: NavController) {
        val websiteForBible = "http://biblia-online.pl/Biblia/"

        val translations = listOf("Tysiaclecia", "UwspolczesnionaBibliaGdanska", "Warszawska","JakubaWujka", "Brzeska")

        val selectedValue1 = remember{ mutableStateOf(
                UserSelection(
                        translation = translations[0],
                        testament = "Stary Testament",
                        book=Book("Ksiega Rodzaju", "Rdz"),
                        chapter = "1"
                ))}
//        val selectedValue2 = remember{ mutableStateOf(UserSelection(translations[0], 1))}
        Column(modifier = Modifier.fillMaxWidth(1f)) {
//                DynamicSelectTextField(selectedValue.value, translations, "Tłumaczenie 1",
//                        onValueChangedEvent = {
//                                selectedValue.value = it
//                        })
//                Spacer(modifier = Modifier.padding(20.dp))
                // TODO add different way of selecting the Bible
                SelectionBox(
                        selectedValue1.value,
                        translations,
                        onValueChangedEvent = {
                        selectedValue1.value = it
                })

        }
}


// TODO Scraper needs internet connection, otherwise app doesn't open
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectionBox(
        selectedValue: UserSelection,
        options: List<String>,
        onValueChangedEvent: (UserSelection) -> Unit,)
{
        var expandedTranslationMenu by remember { mutableStateOf(false) }
        var expandedTestamentMenu by remember { mutableStateOf(false) }
        var expandedBookMenu by remember { mutableStateOf(false) }
        var expandedChapterMenu by remember { mutableStateOf(false) }
        var expandedWersetMenu by remember { mutableStateOf(false) }

//        var selectedTranslation by remember { mutableStateOf("") }
//        var selectedTestament by remember { mutableStateOf("") }
//        var selectedBook : Book by remember { mutableStateOf(Book("","", "")) }
////        var selectedBook by remember { mutableStateOf("") }
//        var selectedChapter by remember { mutableStateOf("0") }

        val testaments : List<String> = listOf("Stary Testament", "Nowy Testament")


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
                                expanded = expandedTranslationMenu,
                                onExpandedChange = { expandedTranslationMenu = !expandedTranslationMenu },
//                                modifier = modifier
                        )  {
                                val bookOptions : MutableList<Book> = ArrayList()
                                val chapterOptions : MutableList<String> = ArrayList()
                                TextField(
                                        enabled = false,
                                        readOnly = true,
                                        singleLine = true,
                                        value = "${selectedValue.translation}, ${selectedValue.book.abbrName}, ${selectedValue.chapter}",
                                        onValueChange = {},
                                        label = {
                                                Text(
                                                        style = MaterialTheme.typography.labelMedium, // TODO font to be changed
                                                        text = "Tłumaczenie"
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
//                                                                onValueChangedEvent(UserSelection(option, 1))
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
//                                                                onValueChangedEvent(UserSelection(option, 1))
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
                                        // create list of chapters according to the translation
                                        skrape(HttpFetcher)
                                        {
                                                request {

                                                        url = "http://biblia-online.pl/Biblia/ListaKsiag/${selectedValue.translation}/"
                                                }
                                                response {
                                                        htmlDocument {
                                                                div{
                                                                        withId="ot-books-list"
                                                                        findFirst{
                                                                                this.div{
                                                                                        withClass = "book-list-item"
                                                                                        findAll{
                                                                                                forEach{
                                                                                                        val fullBookName = it.findFirst("a").text
                                                                                                        val abbrBookName = it.findFirst("span").text
                                                                                                        val url = it.findFirst("a").attribute("href")
                                                                                                        Log.d("url", url)
                                                                                                        bookOptions.add(Book(fullBookName, abbrBookName, url))
                                                                                                }
                                                                                        }
                                                                                }
                                                                        }
                                                                }
                                                        }
                                                }
                                        }
                                        bookOptions.forEach {  option: Book ->
                                                DropdownMenuItem(
                                                        text = { Text(text = option.fullName) },
                                                        onClick = {
                                                                expandedBookMenu = false
                                                                selectedValue.book = option
                                                                expandedChapterMenu = true

//                                                                onValueChangedEvent(UserSelection(
//                                                                        translation = selectedTranslation,
//                                                                        testament = selectedTestament,
//                                                                        book = option))
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
                                        skrape(HttpFetcher)
                                        {
                                                request {

                                                        url = "http://biblia-online.pl/${selectedValue.book.url}"
                                                }
                                                response {
                                                        htmlDocument {
                                                                div{
                                                                        withClass="mnav-chptrs-cell"
                                                                        findFirst{
                                                                                this.a{
                                                                                        findAll{
                                                                                                forEach{
                                                                                                        chapterOptions.add(it.text)
                                                                                                }
                                                                                        }
                                                                                }
                                                                        }
                                                                }
                                                        }
                                                }
                                        }
                                        chapterOptions.forEach {  option: String ->
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

