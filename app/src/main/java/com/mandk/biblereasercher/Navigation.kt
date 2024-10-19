package com.mandk.biblereasercher

import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmarks
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Bookmarks
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.sharp.ImportContacts
import androidx.compose.material.icons.twotone.ImportContacts
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class Werset(var text: String, var nr: String) {
    fun printWerset() {
        Log.d("Werset print", "$nr: $text")
    }
}

data class Book(
    val index: Int,
    val fullName: String,
    val abbrName: String,
    val url: String? = null
)

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

class UserSelectionViewmodel : ViewModel()
{
    // TODO These values have to be saved even after closing the application
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Navigation() {
    val navController = rememberNavController()
    val viewModelSelection = UserSelectionViewmodel()
    var selectedTab by rememberSaveable {
        mutableIntStateOf(0)
    }
    val scrollState = rememberScrollState(0)
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

    Scaffold(
        modifier = Modifier.padding(top = 50.dp),
        topBar = {
            Text(
                modifier = Modifier.padding(top = 40.dp),
                text = topTitle(topLevelRoutes[selectedTab].bottomNavigationItem.title),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        },
        bottomBar = {
            NavigationBar {
                topLevelRoutes.forEachIndexed { index, topLevelRoute ->
                    NavigationBarItem(
                        selected = index == selectedTab,
                        onClick = {
                            selectedTab = index
                            navController.navigate(topLevelRoute.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {
                            BadgedBox(
                                badge = {}
                            ) {
                                Icon(
                                    imageVector = if (index == selectedTab) {
                                        topLevelRoute.bottomNavigationItem.selectedIcon
                                    } else topLevelRoute.bottomNavigationItem.unselectedIcon,
                                    contentDescription = topLevelRoute.bottomNavigationItem.title
                                )
                            }
                        },
                        label = { Text(topLevelRoute.bottomNavigationItem.title) }
                    )
                }
            }
        }
    )
    { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = HomeScreen,
            modifier = Modifier
                .padding(
                    top = paddingValues.calculateTopPadding(),
                    bottom = paddingValues.calculateBottomPadding()
                )
        )
        {
            composable<HomeScreen> {
                HomePage(navController, viewModelSelection)
            }

            composable<ReaderScreen>
            {
                ReadPage(navController)
            }

            composable<BookmarkScreen> {
                BookmarkPage(navController, viewModelSelection)
            }

            composable<SettingsScreen> {
                SettingsPage(navController)
            }
        }
    }
}

fun topTitle(string: String) : String
{
    when(string)
    {
        "Dom" -> return "Bible Researcher"
        "Pismo" -> return string
        "Zakładki" -> return string
        "Ustawienia" -> return string
    }
    return string
}