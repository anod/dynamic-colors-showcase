package info.anodsplace.dynamiccolors.appwidget

import android.content.res.Resources
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceModifier
import androidx.glance.LocalContext
import androidx.glance.appwidget.appWidgetBackground
import androidx.glance.appwidget.cornerRadius
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding

@Composable
fun appWidgetBackgroundModifier() = GlanceModifier
    .fillMaxSize()
    .padding(16.dp)
    .appWidgetBackground()
    .appWidgetBackgroundCornerRadius()

fun GlanceModifier.appWidgetBackgroundCornerRadius(): GlanceModifier =
    cornerRadius(android.R.dimen.system_app_widget_background_radius)

fun GlanceModifier.appWidgetInnerCornerRadius(): GlanceModifier =
    cornerRadius(android.R.dimen.system_app_widget_inner_radius)

@Composable
fun stringResource(@StringRes id: Int, vararg args: Any): String {
    return LocalContext.current.getString(id, args)
}

val Float.toPx get() = this * Resources.getSystem().displayMetrics.density