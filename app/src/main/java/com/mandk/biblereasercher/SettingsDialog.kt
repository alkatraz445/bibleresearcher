package com.mandk.biblereasercher

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun SettingsDialog(
    onDismiss: () -> Unit,
    viewModel: MainViewModel = viewModel(),
) {
    val settingsUiState by viewModel.settingsUiState.collectAsStateWithLifecycle()
    SettingsDialog(
        viewModel = viewModel,
        settingsUiState = settingsUiState,
        onDismiss = onDismiss,
        onChangeDynamicColorPreference = viewModel::updateDynamicColorPreference,
        onChangeDarkThemeConfig = viewModel::updateDarkTheme
    )
}

@Composable
fun SettingsDialog(
    viewModel: MainViewModel,
    settingsUiState: Boolean,
    onDismiss: () -> Unit,
    onChangeDynamicColorPreference: (useDynamicColor: Boolean) -> Unit,
    onChangeDarkThemeConfig: (darkThemeConfig: Boolean) -> Unit,
) {
    val configuration = LocalConfiguration.current

    /**
     * usePlatformDefaultWidth = false is use as a temporary fix to allow
     * height recalculation during recomposition. This, however, causes
     * Dialog's to occupy full width in Compact mode. Therefore max width
     * is configured below. This should be removed when there's fix to
     * https://issuetracker.google.com/issues/221643630
     */
    AlertDialog(
        properties = DialogProperties(usePlatformDefaultWidth = false),
        modifier = Modifier.widthIn(max = configuration.screenWidthDp.dp - 80.dp),
        onDismissRequest = { onDismiss() },
        title = {
            Text(
                text = "Ustawienia",
                style = MaterialTheme.typography.titleLarge,
            )
        },
        text = {
            // TODO Import it well
//            HorizontalDivider()
            Column(Modifier.verticalScroll(rememberScrollState())) {
                when (settingsUiState) {
                    false -> {
                        Text(
                            text = "Loading",
                            modifier = Modifier.padding(vertical = 16.dp),
                        )
                    }

                    true -> {
                        SettingsPanel(
                            viewModel,
                            onChangeDynamicColorPreference = onChangeDynamicColorPreference,
                            onChangeDarkThemeConfig = onChangeDarkThemeConfig,
                        )
                    }
                }
            }
        },
        confirmButton = {
            Text(
                text = "OK",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .clickable { onDismiss() },
            )
        },
    )
}

// [ColumnScope] is used for using the [ColumnScope.AnimatedVisibility] extension overload composable.
@Composable
private fun SettingsPanel(
    viewModel: MainViewModel,
    onChangeDynamicColorPreference: (useDynamicColor: Boolean) -> Unit,
    onChangeDarkThemeConfig: (darkThemeConfig: Boolean) -> Unit,
) {
    SettingsDialogSectionTitle(text = "Tryb wyświetlania")
    Column(Modifier.selectableGroup()) {
        SettingsDialogThemeChooserRow(
            text = "Jasny",
            selected = !viewModel.darkMode.collectAsStateWithLifecycle().value,
            onClick = { onChangeDarkThemeConfig(false) },
        )
        SettingsDialogThemeChooserRow(
            text = "Ciemny",
            selected = viewModel.darkMode.collectAsStateWithLifecycle().value,
            onClick = { onChangeDarkThemeConfig(true) },
        )
    }
    SettingsDialogSectionTitle(text = "Dynamiczny kolor")
    Column(Modifier.selectableGroup()) {
        SettingsDialogThemeChooserRow(
            text = "Tak",
            selected = viewModel.dynamicColor.collectAsStateWithLifecycle().value,
            onClick = { onChangeDynamicColorPreference(true) },
        )
        SettingsDialogThemeChooserRow(
            text = "Nie",
            selected = !viewModel.dynamicColor.collectAsStateWithLifecycle().value,
            onClick = { onChangeDynamicColorPreference(false) },
        )
    }
}

@Composable
private fun SettingsDialogSectionTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp),
    )
}

@Composable
fun SettingsDialogThemeChooserRow(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Row(
        Modifier
            .fillMaxWidth()
            .selectable(
                selected = selected,
                role = Role.RadioButton,
                onClick = onClick,
            )
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        RadioButton(
            selected = selected,
            onClick = null,
        )
        Spacer(Modifier.width(8.dp))
        Text(text)
    }
}