package info.anodsplace.dynamiccolors

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.android.material.color.MaterialColors
import info.anodsplace.dynamiccolors.appwidget.ColorsAppWidget
import kotlinx.coroutines.launch
import java.util.Date

data class ColorDesc(
    val title: String,
    val value: Color,
    val contentColor: Color
)

data class ScreenState(
    val darkTheme: Boolean,
    val selectedColor: ColorDesc? = null
)

sealed interface ScreenEvent {
    object Back : ScreenEvent
    class SwitchTheme(val darkTheme: Boolean) : ScreenEvent
    class SelectColor(val colorDesc: ColorDesc?) : ScreenEvent
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val darkTheme = isSystemInDarkTheme()
            val navController = rememberNavController()
            var screenState by remember {
                mutableStateOf(ScreenState(
                    darkTheme = darkTheme
                ))
            }
            AppTheme(darkTheme = screenState.darkTheme) {
                NavHost(navController = navController, startDestination = "list") {
                    composable("list") {
                        ListScreen(screenState, onEvent = { event -> onEvent(event, screenState, navController) { newState -> screenState = newState } })
                    }
                    composable("details") {
                        it.arguments
                        SelectedColorScreen(screenState, onEvent = { event -> onEvent(event, screenState, navController) { newState -> screenState = newState } })
                    }
                }
            }
        }

        lifecycleScope.launch {
            val manager = GlanceAppWidgetManager(applicationContext)
            val glanceIds = manager.getGlanceIds(ColorsAppWidget::class.java)
            glanceIds.forEach { glanceId ->
                updateAppWidgetState(
                    context = applicationContext,
                    glanceId = glanceId,
                    updateState = { prefs -> prefs[longPreferencesKey("updateTime")] = System.currentTimeMillis() }
                )
            }
        }
    }

    private fun onEvent(
        event: ScreenEvent,
        screenState: ScreenState,
        navController: NavHostController,
        onNewState: (ScreenState) -> Unit
    ) {
        when (event) {
            is ScreenEvent.SwitchTheme -> onNewState(screenState.copy(darkTheme = event.darkTheme))
            is ScreenEvent.SelectColor -> {
                onNewState(screenState.copy(selectedColor = event.colorDesc))
                navController.navigate("details") {
                    popUpTo("list")
                }
            }
            ScreenEvent.Back -> {
                navController.popBackStack()
            }
        }
    }
}

@Composable
fun ListScreen(screenState: ScreenState, onEvent: (ScreenEvent) -> Unit) {
    val clipboard = LocalClipboardManager.current

    Surface(modifier = Modifier.fillMaxSize(), color = if (screenState.darkTheme) Color.Black else Color.White) {
        ColorDescriptions { colors ->
            LazyVerticalGrid(
                columns = GridCells.Fixed(count = 2),
                modifier = Modifier
                    .fillMaxSize()
            ) {
                item(
                    key = "top",
                    span = { GridItemSpan(maxLineSpan) }
                ) {
                    TopRow(darkTheme = screenState.darkTheme, onEvent = onEvent)
                }
                items(colors.size, key = { it }) { index ->
                    ColorItem(
                        colorDesc = colors[index],
                        onClick = { colorDesc -> onEvent(ScreenEvent.SelectColor(colorDesc)) },
                        onLongClick = { colorDesc -> copyColorDesc(colorDesc, clipboard) },
                        modifier = Modifier
                            .padding(all = 4.dp)
                            .fillMaxWidth()
                    )
                }
            }
        }
    }
}

private fun copyColorDesc(colorDesc: ColorDesc, clipboard: ClipboardManager) {
    val hexCode = "#" + colorDesc.value.toColorHex()
    clipboard.setText(AnnotatedString(hexCode))
}

@Composable
private fun SelectedColorScreen(screenState: ScreenState, onEvent: (ScreenEvent) -> Unit) {
    val selectedColor = screenState.selectedColor ?: return
    val roles = remember(screenState) {
        MaterialColors.getColorRoles(selectedColor.value.toArgb(), screenState.darkTheme).let { roles ->
            listOf(
                ColorDesc(title = "accent", value = Color(roles.accent), contentColor = Color(roles.onAccent) ),
                ColorDesc(title = "onAccent", value = Color(roles.onAccent), contentColor = Color(roles.accent) ),
                ColorDesc(title = "accentContainer", value = Color(roles.accentContainer), contentColor = Color(roles.onAccentContainer) ),
                ColorDesc(title = "onAccentContainer", value = Color(roles.onAccentContainer), contentColor = Color(roles.accentContainer) ),
            )
        }
    }

    val clipboard = LocalClipboardManager.current
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = if (screenState.darkTheme) Color.Black else Color.White,
        contentColor = if (screenState.darkTheme) Color.White else Color.Black,
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TopRow(darkTheme = screenState.darkTheme, onEvent = onEvent, showBack = true)
            LazyVerticalGrid(
                columns = GridCells.Fixed(count = 2),
                modifier = Modifier
                    .fillMaxSize()
            ) {
                item(
                    key = "title",
                    span = { GridItemSpan(maxLineSpan) }
                ) {
                    Text(text = selectedColor.title, style = MaterialTheme.typography.headlineLarge, textAlign = TextAlign.Center)
                }
                item(
                    key = "selected",
                    span = { GridItemSpan(maxLineSpan) }
                ) {
                    ColorItem(
                        colorDesc = selectedColor,
                        onClick = { },
                        onLongClick = { colorDesc -> copyColorDesc(colorDesc, clipboard) },
                        modifier = Modifier
                            .padding(all = 4.dp)
                            .defaultMinSize(minWidth = 128.dp)
                    )
                }
                item(
                    key = "roles",
                    span = { GridItemSpan(maxLineSpan) }
                ) {
                    Text(text = "Roles", style = MaterialTheme.typography.headlineLarge, textAlign = TextAlign.Center)
                }
                items(roles.size, key = { it }) { index ->
                    ColorItem(
                        colorDesc = roles[index],
                        onClick = { colorDesc -> onEvent(ScreenEvent.SelectColor(colorDesc)) },
                        onLongClick = { colorDesc -> copyColorDesc(colorDesc, clipboard) },
                        modifier = Modifier
                            .padding(all = 4.dp)
                            .fillMaxWidth()
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopRow(darkTheme: Boolean, onEvent: (ScreenEvent) -> Unit, modifier: Modifier = Modifier, showBack: Boolean = false) {
    TopAppBar(
        modifier = modifier.fillMaxWidth(),
        navigationIcon = {
             if (showBack) {
                 IconButton(onClick = { onEvent(ScreenEvent.Back) }) {
                     Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                 }
             }
        },
        actions = {
            if (showBack) {
                IconButton(onClick = { }) {
                }
            }
        },
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                ElevatedAssistChip(
                    onClick = { onEvent(ScreenEvent.SwitchTheme(darkTheme = false)) },
                    label = { Text(text = "Light theme") },
                    enabled = darkTheme
                )
                ElevatedAssistChip(
                    modifier = Modifier.padding(start = 16.dp),
                    onClick = { onEvent(ScreenEvent.SwitchTheme(darkTheme = true)) },
                    label = { Text(text = "Dark theme") },
                    enabled = !darkTheme
                )
            }
        }
    )

}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ListPreview() {
    AppTheme {
        ListScreen(screenState = ScreenState(darkTheme = false), onEvent = { })
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DetailsPreview() {
    AppTheme {
        SelectedColorScreen(screenState = ScreenState(
            darkTheme = false,
            selectedColor = ColorDesc(title = "primary", value = MaterialTheme.colorScheme.primary, contentColor = MaterialTheme.colorScheme.onPrimary)
        ), onEvent = { })
    }
}