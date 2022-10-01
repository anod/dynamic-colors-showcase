package info.anodsplace.dynamiccolors.appwidget

import android.content.ComponentName
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.*
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.lazy.LazyColumn
import androidx.glance.layout.*
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import info.anodsplace.dynamiccolors.ColorDesc
import info.anodsplace.dynamiccolors.ColorDescriptions
import info.anodsplace.dynamiccolors.MainActivity
import info.anodsplace.dynamiccolors.toColorHex

class ColorsAppWidget : GlanceAppWidget() {


    companion object {
        private val smallMode = DpSize(120.dp, 120.dp)
        private val largeMode = DpSize(210.dp, 200.dp)
    }

    override val sizeMode: SizeMode = SizeMode.Responsive(setOf(smallMode, largeMode))

    @Composable
    override fun Content() {
        val size = LocalSize.current
        val isLarge = (size.width > 210.dp)
        Log.d("ColorsAppWidget", "Recompose $size $isLarge")
        GlanceTheme {
            ColorDescriptions { colors ->
                if (isLarge) {
                    LargeWidget(colors)
                } else {
                    RegularWidget(colors)
                }
            }
        }
    }
}

class ColorsAppWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = ColorsAppWidget()
}

@Composable
private fun LargeWidget(colors: List<ColorDesc>) {
    LazyColumn(
        modifier = GlanceModifier.wrapContentHeight().fillMaxWidth()
    ) {
        val size = (colors.size / 2)
        items(size, itemId = { index ->
            val (first, second) = colorsPair(index, colors)
            val key = "${first.value.toColorHex()}:${second?.value?.toColorHex()}"
            key.hashCode().toLong()
        }) { index ->
            val (first, second) = colorsPair(index, colors)
            Row(
                modifier = GlanceModifier.fillMaxWidth().padding(4.dp)
            ) {
                ColorItem(colorDesc = first, modifier = GlanceModifier.defaultWeight())
                if (second != null) {
                    ColorItem(colorDesc = second, modifier = GlanceModifier.defaultWeight())
                }
            }
        }
    }
}
private fun colorsPair(index: Int, colors: List<ColorDesc>): Pair<ColorDesc, ColorDesc?> {
    val first = index * 2
    val second = first + 1
    if (second < colors.size) {
        return Pair(colors[first], colors[second])
    }
    return Pair(colors[first], null)
}

@Composable
private fun RegularWidget(colors: List<ColorDesc>) {
    LazyColumn(
        modifier = GlanceModifier.wrapContentHeight().fillMaxWidth()
    ) {
        items(colors.size, itemId = { index ->
            colors[index].value.toColorHex().hashCode().toLong()
        }) { index ->
            ColorItem(colorDesc = colors[index], modifier = GlanceModifier.fillMaxWidth())
        }
    }
}

@Composable
fun ColorItem(colorDesc: ColorDesc, modifier: GlanceModifier) {
    val hexCode = "#" + colorDesc.value.toColorHex()
    val context = LocalContext.current
    Box(
        modifier = modifier
            .height(64.dp)
            .appWidgetInnerCornerRadius()
            .background(color = colorDesc.value)
            .clickable(onClick = actionStartActivity(
                componentName = ComponentName(context, MainActivity::class.java)
            ))
    ) {
        Column(modifier = GlanceModifier.padding(start = 16.dp, top = 4.dp)) {
            Text(text = colorDesc.title, style = TextStyle(
                color = ColorProvider(colorDesc.contentColor),
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal
            ))
            Text(text = hexCode, style = TextStyle(
                color = ColorProvider(colorDesc.contentColor),
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal
            ))
        }
    }
}
