package info.anodsplace.dynamiccolors

import androidx.compose.runtime.Composable

@Composable
fun ColorDescriptions(content: @Composable (List<ColorDesc>) -> Unit) {
    val colors = listOf(
        ColorDesc(title = "primary", value = ColorsTheme.colorScheme.primary, contentColor = ColorsTheme.colorScheme.onPrimary),
        ColorDesc(title = "onPrimary", value = ColorsTheme.colorScheme.onPrimary, contentColor = ColorsTheme.colorScheme.primary),
        ColorDesc(title = "primaryContainer", value = ColorsTheme.colorScheme.primaryContainer, contentColor = ColorsTheme.colorScheme.onPrimaryContainer),
        ColorDesc(title = "onPrimaryContainer", value = ColorsTheme.colorScheme.onPrimaryContainer, contentColor = ColorsTheme.colorScheme.primaryContainer),

        ColorDesc(title = "inversePrimary", value = ColorsTheme.colorScheme.inversePrimary, contentColor = ColorsTheme.colorScheme.primary),
        ColorDesc(title = "outline", value = ColorsTheme.colorScheme.outline, contentColor = ColorsTheme.colorScheme.primary),

        ColorDesc(title = "secondary", value = ColorsTheme.colorScheme.secondary, contentColor = ColorsTheme.colorScheme.onSecondary),
        ColorDesc(title = "onSecondary", value = ColorsTheme.colorScheme.onSecondary, contentColor = ColorsTheme.colorScheme.secondary),
        ColorDesc(title = "secondaryContainer", value = ColorsTheme.colorScheme.secondaryContainer, contentColor = ColorsTheme.colorScheme.onSecondaryContainer),
        ColorDesc(title = "onSecondaryContainer", value = ColorsTheme.colorScheme.onSecondaryContainer, contentColor = ColorsTheme.colorScheme.secondaryContainer),

        ColorDesc(title = "tertiary", value = ColorsTheme.colorScheme.tertiary, contentColor = ColorsTheme.colorScheme.onTertiary),
        ColorDesc(title = "onTertiary", value = ColorsTheme.colorScheme.onTertiary, contentColor = ColorsTheme.colorScheme.tertiary),
        ColorDesc(title = "tertiaryContainer", value = ColorsTheme.colorScheme.tertiaryContainer, contentColor = ColorsTheme.colorScheme.onTertiaryContainer),
        ColorDesc(title = "onTertiaryContainer", value = ColorsTheme.colorScheme.onTertiaryContainer, contentColor = ColorsTheme.colorScheme.tertiaryContainer),

        ColorDesc(title = "background", value = ColorsTheme.colorScheme.background, contentColor = ColorsTheme.colorScheme.onBackground),
        ColorDesc(title = "onBackground", value = ColorsTheme.colorScheme.onBackground, contentColor = ColorsTheme.colorScheme.background),

        ColorDesc(title = "surface", value = ColorsTheme.colorScheme.surface, contentColor = ColorsTheme.colorScheme.onSurface),
        ColorDesc(title = "onSurface", value = ColorsTheme.colorScheme.onSurface, contentColor = ColorsTheme.colorScheme.surface),

        ColorDesc(title = "surfaceVariant", value = ColorsTheme.colorScheme.surfaceVariant, contentColor = ColorsTheme.colorScheme.onSurfaceVariant),
        ColorDesc(title = "onSurfaceVariant", value = ColorsTheme.colorScheme.onSurfaceVariant, contentColor = ColorsTheme.colorScheme.surfaceVariant),

        ColorDesc(title = "inverseSurface", value = ColorsTheme.colorScheme.inverseSurface, contentColor = ColorsTheme.colorScheme.inverseOnSurface),
        ColorDesc(title = "inverseOnSurface", value = ColorsTheme.colorScheme.inverseOnSurface, contentColor = ColorsTheme.colorScheme.inverseSurface),

        ColorDesc(title = "error", value = ColorsTheme.colorScheme.error, contentColor = ColorsTheme.colorScheme.onError),
        ColorDesc(title = "onError", value = ColorsTheme.colorScheme.onError, contentColor = ColorsTheme.colorScheme.error),

        ColorDesc(title = "errorContainer", value = ColorsTheme.colorScheme.errorContainer, contentColor = ColorsTheme.colorScheme.onErrorContainer),
        ColorDesc(title = "onErrorContainer", value = ColorsTheme.colorScheme.onErrorContainer, contentColor = ColorsTheme.colorScheme.errorContainer),
    )
    content(colors)
}