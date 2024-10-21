package com.mandk.biblereasercher

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
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class UserSelection(
    var translation: String,
    var testament: String,
    var book: Book,
    var chapter: String = "1",
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


class MainViewModel : ViewModel()
{
    // TODO These values have to be saved even after closing the application

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
        UserSelection(
            translation = "Tysiaclecia",
            testament = "Stary Testament",
            book = Book(0, "Ksiega Rodzaju", "Rdz"),
            chapter = "1"
        ))

    val topSelectionState: StateFlow<UserSelection> = _topSelectionState.asStateFlow()

    private val _bottomSelectionState = MutableStateFlow(
        UserSelection(
            translation = topSelectionState.value.translation,
            testament = topSelectionState.value.testament,
            book = topSelectionState.value.book,
            chapter = "1"
        ))

    val bottomSelectionState: StateFlow<UserSelection> = _bottomSelectionState.asStateFlow()

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
    }



}