package com.mandk.biblereasercher

import android.content.Context
import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmarks
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Bookmarks
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.sharp.ImportContacts
import androidx.compose.material.icons.twotone.ImportContacts
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mandk.biblereasercher.utils.AppDatabase
import com.mandk.biblereasercher.utils.Bookmark
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class Translation(
    val name: String? = "",
    val url: String? = ""
)

data class Book(
    val index: Int? = 0,
    val fullName: String? = "",
    val abbrName: String? = "",
    val url: String? = null
)

data class UserSelection(
    var translation: Translation? = Translation("", ""),
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

data class TopBarItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val onClickEvent : () -> Unit
)

data class TopLevelRoute<T : Any>(val bottomNavigationItem: BottomNavigationItem, val route: T)

/**
 * Main ViewModel of the application
 *
 * Used to share data between screens as well as to save preferences
 *
 * @param context - Context in which DataStore<Preferences> can be created; without it saves wouldn't work
 */
class MainViewModel(context : Context, dataBase: AppDatabase) : ViewModel()
{
    /** value with a list of all routes in the Navigation*/
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
    )

    val topBarOptions = listOf(
        TopBarItem(
            title = "Search",
            selectedIcon = Icons.Filled.Search,
            unselectedIcon = Icons.Outlined.Search,
            onClickEvent = {}
        ),
        TopBarItem(
            title = "Bookmark",
            selectedIcon = Icons.Filled.Bookmarks,
            unselectedIcon = Icons.Outlined.Bookmarks,
            onClickEvent = {
                addBookmark(topSelectionState.value)
            }
        ),
        TopBarItem(
            title = "Settings",
            selectedIcon = Icons.Filled.Settings,
            unselectedIcon = Icons.Outlined.Settings,
            onClickEvent = { changeSettingUiState(true) }
        )
    )

    /** where preferences are stored*/
    private var dataStore: DataStore<Preferences> = createDataStore(context)

    private val _dbDao = dataBase.bookmarksDao()

    private val _bookmarkCount: Flow<Int> = _dbDao.getBookmarkCount()
    val bookmarkCount : Flow<Int> = _bookmarkCount

    private val _allBookmarks: Flow<List<Bookmark>> = _dbDao.getAll()
    val allBookmarks : Flow<List<Bookmark>> = _allBookmarks

    fun addBookmark(value: UserSelection)
    {
        viewModelScope.launch {
            val bookmark = Bookmark(
                translationName = value.translation?.name,
                translationUrl = value.translation?.url,
                bookIndex = value.book?.index,
                bookName = value.book?.fullName,
                bookAbbrName = value.book?.abbrName,
                bookUrl = value.book?.url,
                testament = value.testament,
                chapter = value.chapter
            )
            Log.d("inserting bookmark", bookmark.bookAbbrName?: "")
            _dbDao.insertOne(bookmark)
        }
    }

    fun removeBookmark(index: Int)
    {
        viewModelScope.launch {
            _dbDao.delete(_dbDao.loadById(index))
        }
    }

    private val _checkedComparisonBox = MutableStateFlow(true)

    val checkedComparisonBox = _checkedComparisonBox.asStateFlow()

    fun changeComparisonBoxState(value : Boolean)
    {
        _checkedComparisonBox.update {
            value
        }
    }

    /** Stores which Tab is selected*/
    private val _selectedTab = MutableStateFlow(0)

    /** Stores which Tab is selected, but is mutable and public*/
    val selectedTab: StateFlow<Int> = _selectedTab.asStateFlow()

    /**
     * Function to update selected Tab
     *
     * @param value - Int representing index of the Tab
     */
    fun changeSelectedTab(value : Int)
    {
        _selectedTab.update { value }
    }

    /** Top selection state of HomePage */
    private val _topSelectionState = MutableStateFlow(
        UserSelection( // Initial state, can be placeholder values
            translation = Translation("Biblia Tysiąclecia", "/Biblia/Tysiaclecia"),
            testament = "Stary Testament",
            book = Book(0, "Ksiega Rodzaju", "Rdz", "/Biblia/Tysiaclecia/Ksiega-Rodzaju"),
            chapter = "1"
        )
    )

    /** Bottom selection state of HomePage */
    private val _bottomSelectionState = MutableStateFlow(
        UserSelection( // Initial state, can be placeholder values
            translation = _topSelectionState.value.translation,
            testament = _topSelectionState.value.testament,
            book = _topSelectionState.value.book,
            chapter = _topSelectionState.value.chapter
        )
    )

    /** Top selection state of HomePage, but public */
    val topSelectionState: StateFlow<UserSelection> = _topSelectionState.asStateFlow()
    /** Bottom selection state of HomePage, but public */
    val bottomSelectionState: StateFlow<UserSelection> = _bottomSelectionState.asStateFlow()

    init {
        viewModelScope.launch {
            val userSelection1 = UserSelection(
                translation =
                    Translation(
                        name = read("top_selection_translation_name")?: "Biblia Tysiaclecia",
                        url = read("top_selection_translation_url")?: "/Biblia/Tysiaclecia"
                    ),
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
                translation =
                Translation(
                    name = read("bottom_selection_translation_name")?: "Biblia Tysiaclecia",
                    url = read("bottom_selection_translation_url")?: "/Biblia/Tysiaclecia"
                ),
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

            _darkMode.value = read_settings("dark_mode") ?: false
            _dynamicColor.value = read_settings("dynamic_color") ?: false
        }
    }

    /**
     * Function used to:
     * - update top selection
     * - save values of top selection to Preferences
     *
     * @param value - UserSelection
     */
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
            save("top_selection_translation_name",  value.translation?.name ?: "")
            save("top_selection_translation_url",   value.translation?.url ?: "")
            save("top_selection_testament",         value.testament?: "")
            save("top_selection_book_index",        value.book?.index.toString())
            save("top_selection_book_name",         value.book?.fullName ?: "")
            save("top_selection_book_name_abbr",    value.book?.abbrName ?: "")
            save("top_selection_url",               value.book?.url?: "")
            save("top_selection_chapter",           value.chapter?: "")
        }
    }

    fun updateTopSelection(value : Bookmark)
    {
        _topSelectionState.update { currentState ->
            currentState.copy(
                translation = Translation(value.translationName, value.translationUrl),
                testament = value.testament,
                book = Book(value.bookIndex, value.bookName, value.bookAbbrName, value.bookUrl),
                chapter = value.chapter
            )
        }
        viewModelScope.launch {
            save("top_selection_translation_name",  value.translationName ?: "")
            save("top_selection_translation_url",   value.translationUrl ?: "")
            save("top_selection_testament",         value.testament?: "")
            save("top_selection_book_index",        value.bookIndex.toString())
            save("top_selection_book_name",         value.bookName ?: "")
            save("top_selection_book_name_abbr",    value.bookAbbrName ?: "")
            save("top_selection_url",               value.bookUrl?: "")
            save("top_selection_chapter",           value.chapter?: "")
        }
    }

    /**
     * Function used to:
     * - update bottom selection
     * - save values of bottom selection to Preferences
     *
     * @param value - UserSelection
     */
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
            save("bottom_selection_translation_name",  value.translation?.name ?: "")
            save("bottom_selection_translation_url",   value.translation?.url ?: "")
            save("bottom_selection_testament",         value.testament?: "")
            save("bottom_selection_book_index",        value.book?.index.toString())
            save("bottom_selection_book_name",         value.book?.fullName ?: "")
            save("bottom_selection_book_name_abbr",    value.book?.abbrName ?: "")
            save("bottom_selection_url",               value.book?.url?: "")
            save("bottom_selection_chapter",           value.chapter?: "")
        }
    }


    fun updateBottomSelection(value : Bookmark)
    {
//        val scraper : Scraper = ScraperClass()
        _bottomSelectionState.update { currentState ->
            // scrape for book in order to match the bookmarks book
//            scraper.scrapeForBook(navController, viewModel, )
            currentState.copy(
                translation = currentState.translation,
                testament = currentState.testament,
                book = Book(value.bookIndex, value.bookName, value.bookAbbrName, value.bookUrl),
                chapter = value.chapter
            )
        }
        viewModelScope.launch {
            save("bottom_selection_translation_name",  value.translationName ?: "")
            save("bottom_selection_translation_url",   value.translationUrl ?: "")
            save("bottom_selection_testament",         value.testament?: "")
            save("bottom_selection_book_index",        value.bookIndex.toString())
            save("bottom_selection_book_name",         value.bookName ?: "")
            save("bottom_selection_book_name_abbr",    value.bookAbbrName ?: "")
            save("bottom_selection_url",               value.bookUrl?: "")
            save("bottom_selection_chapter",           value.chapter?: "")
        }
    }

    fun swapSelections()
    {
        val temp = topSelectionState.value
        updateTopSelection(bottomSelectionState.value)
        updateBottomSelection(temp)

    }

    // SETTINGS

    private val _settingsUiState = MutableStateFlow<Boolean>(false)

    val settingsUiState: StateFlow<Boolean> = _settingsUiState.asStateFlow()

    fun changeSettingUiState(value : Boolean)
    {
        _settingsUiState.update { value }
    }

    /** Stores dark mode bool is selected*/
    private val _darkMode = MutableStateFlow<Boolean>(false)

    val darkMode: StateFlow<Boolean> = _darkMode.asStateFlow()

    fun updateDarkTheme(value : Boolean)
    {
        _darkMode.update { value }
        viewModelScope.launch {
            save("dark_mode", value)
        }
    }

    /** Stores dark mode bool is selected*/
    private val _dynamicColor = MutableStateFlow<Boolean>(false)

    val dynamicColor: StateFlow<Boolean> = _dynamicColor.asStateFlow()

    fun updateDynamicColorPreference(value : Boolean)
    {
        _dynamicColor.update { value }
        viewModelScope.launch {
            save("dynamic_color", value)
        }
    }

    /**
     * Function used to save value to a specified key
     *
     * @param key - unique, identifying key of a Preference
     * @param value - value to store in key (String)
     */
    private suspend fun save(key: String, value: String)
    {
        val dataStoreKey = stringPreferencesKey(key)
        dataStore.edit {
            it[dataStoreKey] = value
        }
    }

    /**
     * Function used to read value from a specified key
     *
     * @param key - unique, identifying key of a Preference
     * @return nullable String - so if there is no value returns null
     */
    private suspend fun read(key: String) : String?
    {
        val dataStoreKey = stringPreferencesKey(key)
        val preferences = dataStore.data.first()
        return preferences[dataStoreKey]
    }

    /**
     * Function used to save value to a specified key
     *
     * @param key - unique, identifying key of a Preference
     * @param value - value to store in key (Boolean)
     */
    private suspend fun save(key: String, value: Boolean)
    {
        val dataStoreKey = booleanPreferencesKey(key)
        dataStore.edit {
            it[dataStoreKey] = value
        }
    }

    /**
     * Function used to read settings with value from a specified key
     *
     * @param key - unique, identifying key of a Preference
     * @return nullable Boolean - so if there is no value returns null
     */
    private suspend fun read_settings(key: String) : Boolean?
    {
        val dataStoreKey = booleanPreferencesKey(key)
        val preferences = dataStore.data.first()
        return preferences[dataStoreKey]
    }

}