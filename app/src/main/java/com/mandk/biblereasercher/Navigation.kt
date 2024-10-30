package com.mandk.biblereasercher

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.compose.AppTheme
import com.mandk.biblereasercher.pages.BookmarkPage
import com.mandk.biblereasercher.pages.ErrorPage
import com.mandk.biblereasercher.utils.AppDatabase

class Werset(var text: String, var nr: String) {
    fun printWerset() {
        Log.d("Werset print", "$nr: $text")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Navigation(
    context: Context
) {
    val navController = rememberNavController()

    val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(db: SupportSQLiteDatabase) {
            // SQL statement to add a new column
            db.execSQL("ALTER TABLE Bookmark ADD COLUMN bookIndex INTEGER")
            db.execSQL("ALTER TABLE Bookmark ADD COLUMN bookName TEXT")
        }
    }

    val dataBase = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        "bookmarks"
        )
        .addMigrations(MIGRATION_1_2)
        .build()

    val viewModel = remember { MainViewModel(context, dataBase = dataBase) }
    val selectedTab by viewModel.selectedTab.collectAsStateWithLifecycle()
    val selectedValue by viewModel.topSelectionState.collectAsStateWithLifecycle()
    val comparisonModeOn by viewModel.checkedComparisonBox.collectAsStateWithLifecycle()

    AppTheme(
        darkTheme = viewModel.darkMode.collectAsStateWithLifecycle().value,
        dynamicColor = viewModel.dynamicColor.collectAsStateWithLifecycle().value
    ) {
        if (viewModel.settingsUiState.collectAsStateWithLifecycle().value) {
            SettingsDialog(
                onDismiss = { viewModel.changeSettingUiState(false) },
                viewModel
            )
        }
        Scaffold(
            modifier = Modifier.padding(top = 50.dp),
            topBar = {
                // different top bars for each selectedTab
                if(viewModel.chapterUpdated.collectAsStateWithLifecycle().value)
                {
                    Log.d("false chapter updated", selectedValue.chapter?:"")
                    viewModel.setChapterUpdated(false)
                    TopBar(viewModel, selectedValue, selectedTab, comparisonModeOn)
                }
                else
                {
                    TopBar(viewModel, selectedValue, selectedTab, comparisonModeOn)
                }

            },
            bottomBar = {
                NavigationBar {
                    viewModel.topLevelRoutes.forEachIndexed { index, topLevelRoute ->
                        NavigationBarItem(
                            selected = index == selectedTab,
                            onClick = {
                                viewModel.changeSelectedTab(index)
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
                    HomePage(navController, viewModel)
                }

                composable<ReaderScreen>
                {
                    ReadPage(navController, viewModel)
                }

                composable<BookmarkScreen> {
                    BookmarkPage(navController, viewModel)
                }

                composable<ErrorScreen> {
                    ErrorPage()
                }
            }
        }
    }

}

fun topTitle(selectedValue: UserSelection, string: String) : String
{
//    val selectedValue = viewModel.topSelectionState.value
    when(string)
    {
        "Dom" -> return "Bible Researcher"
        "Pismo" -> return "${selectedValue.translation?.name?: ""}, ${selectedValue.book?.abbrName?: ""}, ${selectedValue.chapter}"
        "Zakładki" -> return string
        "Ustawienia" -> return string
    }
    return string
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(viewModel: MainViewModel, selectedValue: UserSelection, selection: Int, comparisonModeOn: Boolean) {
    val title = topTitle(selectedValue, viewModel.topLevelRoutes[selection].bottomNavigationItem.title)
    Log.d("title", title)
    TopAppBar(
        title = {
            Text(title)
        },
        actions = {
            if (selection == 0 || selection == 1) {
                IconButton(onClick = viewModel.topBarOptions[0].onClickEvent) {
                    Icon(imageVector = Icons.Filled.Search, contentDescription = "Search")
                }
            }
            if (selection == 1 && !comparisonModeOn) {
                IconButton(onClick = viewModel.topBarOptions[1].onClickEvent) {
                    Icon(imageVector = Icons.Filled.Bookmark, contentDescription = "Bookmark")
                }
            }
            // Always shown settings button
            IconButton(onClick = viewModel.topBarOptions[2].onClickEvent) {
                Icon(imageVector = Icons.Filled.Settings, contentDescription = "Settings")
            }
        }
    )
}