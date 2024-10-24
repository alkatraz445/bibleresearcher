package com.mandk.biblereasercher

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.padding
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mandk.biblereasercher.pages.BookmarkPage

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
    val viewModel = remember { MainViewModel(context) }
    val selectedTab by viewModel.selectedTab.collectAsStateWithLifecycle()

    Scaffold(
        modifier = Modifier.padding(top = 50.dp),
        topBar = {
            Text(
                modifier = Modifier.padding(top = 40.dp),
                text = topTitle(viewModel, viewModel.topLevelRoutes[selectedTab].bottomNavigationItem.title),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
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

        }
    }
}

fun topTitle(viewModel: MainViewModel, string: String) : String
{
    val selectedValue = viewModel.topSelectionState.value

    when(string)
    {
        "Dom" -> return "Bible Researcher"
        "Pismo" -> return "${selectedValue.translation?.name?: ""}, ${selectedValue.book?.abbrName?: ""}, ${selectedValue.chapter}"
        "Zakładki" -> return string
        "Ustawienia" -> return string
    }
    return string
}

