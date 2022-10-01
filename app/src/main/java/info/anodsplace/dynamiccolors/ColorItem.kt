package info.anodsplace.dynamiccolors

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ColorItem(colorDesc: ColorDesc, onClick: (ColorDesc) -> Unit, onLongClick: (ColorDesc) -> Unit, modifier: Modifier = Modifier) {
    val hexCode = "#" + colorDesc.value.toColorHex()
    ColorItemBase(
        colorDesc = colorDesc,
        modifier = modifier
            .height(64.dp)
            .combinedClickable(
                enabled = true,
                onClickLabel = "${colorDesc.title} details",
                role = null,
                onLongClickLabel = "copy $hexCode",
                onLongClick = { onLongClick(colorDesc) },
                onDoubleClick = null,
                onClick = { onClick(colorDesc) }
            )
    )
}

@Composable
fun ColorItemBase(colorDesc: ColorDesc, modifier: Modifier = Modifier) {
    val shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp, bottomStart = 16.dp, bottomEnd = 16.dp)
    val hexCode = "#" + colorDesc.value.toColorHex()

    Surface(
        modifier = modifier,
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