package com.mandk.biblereasercher

import android.content.Context
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmarks
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Bookmarks
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.sharp.ImportContacts
import androidx.compose.material.icons.twotone.ImportContacts
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class Book(
    val index: Int? = 0,
    val fullName: String? = "",
    val abbrName: String? = "",
    val url: String? = null
)

data class UserSelection(
    var translation: String? = "",
    var testament: String? = "",
    var book: Book? = Book(0, "","", ""),
    var chapter: String? = "",
    val werset: Int? = 1
)

data class BottomNavigationItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val hasNews: Boolean,
    val badgeCount: Int? = null
)

data class TopLevelRoute<T : Any>(val bottomNavigationItem: BottomNavigationItem, val route: T)


class MainViewModel(context : Context) : ViewModel()
{
    private var dataStore: DataStore<Preferences> = createDataStore(context)

    private val _selectedTab = MutableStateFlow<Int>(0)

    val selectedTab: StateFlow<Int> = _selectedTab.asStateFlow()

    fun changeSelectedTab(value : Int)
    {
        _selectedTab.update { value }
    }

    val topLevelRoutes = listOf(
        TopLevelRoute(
            BottomNavigationItem(
                title = "Dom",
                selectedIcon = Icons.Filled.Home,
                unselectedIcon = Icons.Outlined.Home,
                hasNews = false
            ),
            HomeScreen
        ),
        TopLevelRoute(
            BottomNavigationItem(
                title = "Pismo",
                selectedIcon = Icons.Sharp.ImportContacts,
                unselectedIcon = Icons.TwoTone.ImportContacts,
                hasNews = false
            ),
            ReaderScreen
        ),
        TopLevelRoute(
            BottomNavigationItem(
                title = "Zakładki",
                selectedIcon = Icons.Filled.Bookmarks,
                unselectedIcon = Icons.Outlined.Bookmarks,
                hasNews = false
            ),
            BookmarkScreen
        ),
        TopLevelRoute(
            BottomNavigationItem(
                title = "Ustawienia",
                selectedIcon = Icons.Filled.Settings,
                unselectedIcon = Icons.Outlined.Settings,
                hasNews = false
            ),
            SettingsScreen
        )
    )

    private val _topSelectionState = MutableStateFlow(
        UserSelection( // Initial state, can be placeholder values
            translation = "Tysiaclecia",
            testament = "Stary Testament",
            book = Book(0, "Ksiega Rodzaju", "Rdz", "/Biblia/Tysiaclecia/Ksiega-Rodzaju"),
            chapter = "1"
        )
    )
    private val _bottomSelectionState = MutableStateFlow(
        UserSelection( // Initial state, can be placeholder values
            translation = _topSelectionState.value.translation,
            testament = _topSelectionState.value.testament,
            book = _topSelectionState.value.book,
            chapter = _topSelectionState.value.chapter
        )
    )

    val topSelectionState: StateFlow<UserSelection> = _topSelectionState.asStateFlow()
    val bottomSelectionState: StateFlow<UserSelection> = _bottomSelectionState.asStateFlow()

    init {
        viewModelScope.launch {
            val userSelection1 = UserSelection(
                translation = read("top_selection_translation")?:"Tysiaclecia",
                testament = read("top_selection_testament")?:"Stary Testament",
                book = Book(
                    read("top_selection_book_index")?.toInt() ?: 0,
                    read("top_selection_book_name")?:"Ksiega Rodzaju",
                    read("top_selection_book_name_abbr")?:"Rdz",
                    read("top_selection_url")?:"/Biblia/Tysiaclecia/Ksiega-Rodzaju"
                ),
                chapter = read("top_selection_chapter")?:"1"
            )
            val userSelection2 = UserSelection(
                translation = read("bottom_selection_translation")?:"Tysiaclecia",
                testament = read("bottom_selection_testament")?:"Stary Testament",
                book = Book(
                    read("bottom_selection_book_index")?.toInt() ?: 0,
                    read("bottom_selection_book_name")?:"Ksiega Rodzaju",
                    read("bottom_selection_book_name_abbr")?:"Rdz",
                    read("bottom_selection_url")?:"/Biblia/Tysiaclecia/Ksiega-Rodzaju"
                ),
                chapter = read("bottom_selection_chapter")?:"1"
            )
            _topSelectionState.value = userSelection1
            _bottomSelectionState.value = userSelection2
        }
    }

    fun updateTopSelection(value : UserSelection)
    {
        _topSelectionState.update { currentState ->
            currentState.copy(
                translation = value.translation,
                testament = value.testament,
                book = value.book,
                chapter = value.chapter
            )
        }
        viewModelScope.launch {
            save("top_selection_translation",       value.translation ?: "")
            save("top_selection_testament",         value.testament?: "")
            save("top_selection_book_index",        value.book?.index.toString())
            save("top_selection_book_name",         value.book?.fullName ?: "")
            save("top_selection_book_name_abbr",    value.book?.abbrName ?: "")
            save("top_selection_url",               value.book?.url?: "")
            save("top_selection_chapter",           value.chapter?: "")
        }
    }

    fun updateBottomSelection(value : UserSelection)
    {
        _bottomSelectionState.update { currentState ->
            currentState.copy(
                translation = value.translation,
                testament = value.testament,
                book = value.book,
                chapter = value.chapter
            )
        }
        viewModelScope.launch {
            save("bottom_selection_translation",       value.translation ?: "")
            save("bottom_selection_testament",         value.testament?: "")
            save("bottom_selection_book_index",        value.book?.index.toString())
            save("bottom_selection_book_name",         value.book?.fullName ?: "")
            save("bottom_selection_book_name_abbr",    value.book?.abbrName ?: "")
            save("bottom_selection_url",               value.book?.url?: "")
            save("bottom_selection_chapter",           value.chapter?: "")
        }
    }

    private suspend fun save(key: String, value: String)
    {
        val dataStoreKey = stringPreferencesKey(key)
        dataStore.edit {
            it[dataStoreKey] = value
        }
    }

    private suspend fun read(key: String) : String?
    {
        val dataStoreKey = stringPreferencesKey(key)
        val preferences = dataStore.data.first()
        return preferences[dataStoreKey]
    }

}