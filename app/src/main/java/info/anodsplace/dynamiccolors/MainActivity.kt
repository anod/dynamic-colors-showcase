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
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.util.*

data class ScreenState(
    val darkTheme: Boolean
)

sealed interface ScreenEvent {
    class SwitchTheme(val darkTheme: Boolean) : ScreenEvent
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val darkTheme = isSystemInDarkTheme()
            var screenState by remember {
                mutableStateOf(ScreenState(
                    darkTheme = darkTheme
                ))
            }
            Screen(screenState, onEvent = { event ->
                when (event) {
                    is ScreenEvent.SwitchTheme -> {
                        screenState = screenState.copy(darkTheme = event.darkTheme)
                    }
                }
            })
        }
    }
}

data class ColorDesc(
    val title: String,
    val value: Color,
    val contentColor: Color
)

@Composable
fun Screen(screenState: ScreenState, onEvent: (ScreenEvent) -> Unit) {
    DynamicColorsShowcaseTheme(darkTheme = screenState.darkTheme) {
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
                        val item = colors[index]
                        ColorItem(title = item.title, value = item.value, contentColor = item.contentColor)
                    }
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

@Composable
fun ColorItem(title: String, value: Color, contentColor: Color) {
    val shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp, bottomStart = 16.dp, bottomEnd = 16.dp)
    Surface(
        modifier = Modifier
            .padding(all = 4.dp)
            .fillMaxWidth()
            .height(64.dp),
        color = value,
        contentColor = contentColor,
        shape = shape,
        border = BorderStroke(width = 1.dp, color = contentColor)
    ) {
        Column(modifier = Modifier.padding(start = 16.dp, top = 8.dp)) {
            Text(text = title, style = MaterialTheme.typography.labelMedium)
            Text(text = "#" + value.toColorHex(), style = MaterialTheme.typography.labelLarge)
        }
    }
}

fun Color.toColorHex(withAlpha: Boolean = true): String {
    var hexStr = "%08X".format(Locale.ROOT, (value shr 32).toLong())
    if (!withAlpha) {
        hexStr = hexStr.substring(2)
    }
    return hexStr
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopRow(darkTheme: Boolean, onEvent: (ScreenEvent) -> Unit, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
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

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DefaultPreview() {
    Screen(screenState = ScreenState(darkTheme = false), onEvent = { })
}