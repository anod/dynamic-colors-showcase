package info.anodsplace.dynamiccolors

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.android.material.color.MaterialColors

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
            DynamicColorsShowcaseTheme(darkTheme = screenState.darkTheme) {
                NavHost(navController = navController, startDestination = "list") {
                    composable("list") {
                        ListScreen(screenState, onEvent = { event -> onEvent(event, screenState, navController) { newState -> screenState = newState } })
                    }
                    composable("details") {
                        SelectedColorScreen(screenState, onEvent = { event -> onEvent(event, screenState, navController) { newState -> screenState = newState } })
                    }
                }
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
    // A surface container using the 'background' color from the theme
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
                        modifier = Modifier
                            .padding(all = 4.dp)
                            .fillMaxWidth()
                    )
                }
            }
        }
    }
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
                        modifier = Modifier
                            .padding(all = 4.dp)
                            .fillMaxWidth()
                    )
                }
            }
        }
    }
}


@Composable
fun ColorDescriptions(content: @Composable (List<ColorDesc>) -> Unit) {
    val colors = listOf(
        ColorDesc(title = "primary", value = MaterialTheme.colorScheme.primary, contentColor = MaterialTheme.colorScheme.onPrimary),
        ColorDesc(title = "onPrimary", value = MaterialTheme.colorScheme.onPrimary, contentColor = MaterialTheme.colorScheme.primary),
        ColorDesc(title = "primaryContainer", value = MaterialTheme.colorScheme.primaryContainer, contentColor = MaterialTheme.colorScheme.onPrimaryContainer),
        ColorDesc(title = "onPrimaryContainer", value = MaterialTheme.colorScheme.onPrimaryContainer, contentColor = MaterialTheme.colorScheme.primaryContainer),

        ColorDesc(title = "inversePrimary", value = MaterialTheme.colorScheme.inversePrimary, contentColor = MaterialTheme.colorScheme.primary),
        ColorDesc(title = "outline", value = MaterialTheme.colorScheme.outline, contentColor = MaterialTheme.colorScheme.primary),

        ColorDesc(title = "secondary", value = MaterialTheme.colorScheme.secondary, contentColor = MaterialTheme.colorScheme.onSecondary),
        ColorDesc(title = "onSecondary", value = MaterialTheme.colorScheme.onSecondary, contentColor = MaterialTheme.colorScheme.secondary),
        ColorDesc(title = "secondaryContainer", value = MaterialTheme.colorScheme.secondaryContainer, contentColor = MaterialTheme.colorScheme.onSecondaryContainer),
        ColorDesc(title = "onSecondaryContainer", value = MaterialTheme.colorScheme.onSecondaryContainer, contentColor = MaterialTheme.colorScheme.secondaryContainer),

        ColorDesc(title = "tertiary", value = MaterialTheme.colorScheme.tertiary, contentColor = MaterialTheme.colorScheme.onTertiary),
        ColorDesc(title = "onTertiary", value = MaterialTheme.colorScheme.onTertiary, contentColor = MaterialTheme.colorScheme.tertiary),
        ColorDesc(title = "tertiaryContainer", value = MaterialTheme.colorScheme.tertiaryContainer, contentColor = MaterialTheme.colorScheme.onTertiaryContainer),
        ColorDesc(title = "onTertiaryContainer", value = MaterialTheme.colorScheme.onTertiaryContainer, contentColor = MaterialTheme.colorScheme.tertiaryContainer),

        ColorDesc(title = "background", value = MaterialTheme.colorScheme.background, contentColor = MaterialTheme.colorScheme.onBackground),
        ColorDesc(title = "onBackground", value = MaterialTheme.colorScheme.onBackground, contentColor = MaterialTheme.colorScheme.background),

        ColorDesc(title = "surface", value = MaterialTheme.colorScheme.surface, contentColor = MaterialTheme.colorScheme.onSurface),
        ColorDesc(title = "onSurface", value = MaterialTheme.colorScheme.onSurface, contentColor = MaterialTheme.colorScheme.surface),

        ColorDesc(title = "surfaceVariant", value = MaterialTheme.colorScheme.surfaceVariant, contentColor = MaterialTheme.colorScheme.onSurfaceVariant),
        ColorDesc(title = "onSurfaceVariant", value = MaterialTheme.colorScheme.onSurfaceVariant, contentColor = MaterialTheme.colorScheme.surfaceVariant),

        ColorDesc(title = "inverseSurface", value = MaterialTheme.colorScheme.inverseSurface, contentColor = MaterialTheme.colorScheme.inverseOnSurface),
        ColorDesc(title = "inverseOnSurface", value = MaterialTheme.colorScheme.inverseOnSurface, contentColor = MaterialTheme.colorScheme.inverseSurface),

        ColorDesc(title = "error", value = MaterialTheme.colorScheme.error, contentColor = MaterialTheme.colorScheme.onError),
        ColorDesc(title = "onError", value = MaterialTheme.colorScheme.onError, contentColor = MaterialTheme.colorScheme.error),

        ColorDesc(title = "errorContainer", value = MaterialTheme.colorScheme.errorContainer, contentColor = MaterialTheme.colorScheme.onErrorContainer),
        ColorDesc(title = "onErrorContainer", value = MaterialTheme.colorScheme.onErrorContainer, contentColor = MaterialTheme.colorScheme.errorContainer),
    )
    content(colors)
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ColorItem(colorDesc: ColorDesc, onClick: (ColorDesc) -> Unit, modifier: Modifier = Modifier) {
    val shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp, bottomStart = 16.dp, bottomEnd = 16.dp)
    val hexCode = "#" + colorDesc.value.toColorHex()
    val clipboard = LocalClipboardManager.current

    Surface(
        modifier = modifier
            .height(64.dp)
            .combinedClickable(
                enabled = true,
                onClickLabel = "${colorDesc.title} details",
                role = null,
                onLongClickLabel = "copy $hexCode",
                onLongClick = { clipboard.setText(AnnotatedString(hexCode)) },
                onDoubleClick = null,
                onClick = { onClick(colorDesc) }
            )
        ,
        color = colorDesc.value,
        contentColor = colorDesc.contentColor,
        shape = shape,
        border = BorderStroke(width = 1.dp, color = colorDesc.contentColor)
    ) {
        Column(modifier = Modifier.padding(start = 16.dp, top = 8.dp)) {
            Text(text = colorDesc.title, style = MaterialTheme.typography.labelMedium)
            Text(text = hexCode, style = MaterialTheme.typography.labelLarge)
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
    DynamicColorsShowcaseTheme {
        ListScreen(screenState = ScreenState(darkTheme = false), onEvent = { })
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DetailsPreview() {
    DynamicColorsShowcaseTheme {
        SelectedColorScreen(screenState = ScreenState(
            darkTheme = false,
            selectedColor = ColorDesc(title = "primary", value = MaterialTheme.colorScheme.primary, contentColor = MaterialTheme.colorScheme.onPrimary)
        ), onEvent = { })
    }
}