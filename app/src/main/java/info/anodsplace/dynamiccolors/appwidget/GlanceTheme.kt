package info.anodsplace.dynamiccolors.appwidget

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import androidx.glance.LocalContext
import androidx.glance.color.dynamicThemeColorProviders
import androidx.glance.template.LocalTemplateColors
import info.anodsplace.dynamiccolors.ColorScheme
import info.anodsplace.dynamiccolors.LocalColorScheme

/**
 * Temporary implementation of Material3 theme for Glance.
 *
 * Note: This still requires manually setting the colors for all Glance components.
 */
@Composable
fun GlanceTheme(content: @Composable () -> Unit) {
    val colorProvider = dynamicThemeColorProviders()
    val context = LocalContext.current

    val colorScheme = ColorScheme(
         primary = colorProvider.primary.getColor(context),
         onPrimary = colorProvider.onPrimary.getColor(context),
         primaryContainer = colorProvider.primaryContainer.getColor(context),
         onPrimaryContainer = colorProvider.onPrimaryContainer.getColor(context),
         secondary = colorProvider.secondary.getColor(context),
         onSecondary = colorProvider.onSecondary.getColor(context),
         secondaryContainer = colorProvider.secondaryContainer.getColor(context),
         onSecondaryContainer = colorProvider.onSecondaryContainer.getColor(context),
         tertiary = colorProvider.tertiary.getColor(context),
         onTertiary = colorProvider.onTertiary.getColor(context),
         tertiaryContainer = colorProvider.tertiaryContainer.getColor(context),
         onTertiaryContainer = colorProvider.onTertiaryContainer.getColor(context),
         error = colorProvider.error.getColor(context),
         errorContainer = colorProvider.errorContainer.getColor(context),
         onError = colorProvider.onError.getColor(context),
         onErrorContainer = colorProvider.onErrorContainer.getColor(context),
         background = colorProvider.background.getColor(context),
         onBackground = colorProvider.onBackground.getColor(context),
         surface = colorProvider.surface.getColor(context),
         onSurface = colorProvider.onSurface.getColor(context),
         surfaceVariant = colorProvider.surfaceVariant.getColor(context),
         onSurfaceVariant = colorProvider.onSurfaceVariant.getColor(context),
         outline = colorProvider.outline.getColor(context),
         inverseOnSurface = colorProvider.inverseOnSurface.getColor(context),
         inverseSurface = colorProvider.inverseSurface.getColor(context),
         inversePrimary = colorProvider.inversePrimary.getColor(context),
    )

    CompositionLocalProvider(
        LocalColorScheme provides colorScheme,
        LocalTemplateColors provides colorProvider
    ) {
        content()
    }
}
