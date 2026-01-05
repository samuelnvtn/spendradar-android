package com.pef.mendelu.novotnysamuel.spendradar.ui.elements

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.pef.mendelu.novotnysamuel.spendradar.R
import com.pef.mendelu.novotnysamuel.spendradar.model.ReceiptCategory
import com.pef.mendelu.novotnysamuel.spendradar.ui.theme.accentColor

@Composable
fun categoryLabel(category: ReceiptCategory): String {
    return when (category) {
        ReceiptCategory.FOOD_DRINK -> stringResource(R.string.category_food_drink)
        ReceiptCategory.GROCERIES -> stringResource(R.string.category_groceries)
        ReceiptCategory.PHARMACY -> stringResource(R.string.category_pharmacy)
        ReceiptCategory.ELECTRONICS -> stringResource(R.string.category_electronics)
        ReceiptCategory.FASHION -> stringResource(R.string.category_fashion)
        ReceiptCategory.OTHER -> stringResource(R.string.category_other)
    }
}

@DrawableRes
fun categoryIcon(category: ReceiptCategory) = when (category) {
    ReceiptCategory.FOOD_DRINK -> R.drawable.fork
    ReceiptCategory.GROCERIES -> R.drawable.shop
    ReceiptCategory.PHARMACY -> R.drawable.medic
    ReceiptCategory.ELECTRONICS -> R.drawable.electro
    ReceiptCategory.FASHION -> R.drawable.fashion
    ReceiptCategory.OTHER -> R.drawable.other
}

fun categoryColor(category: ReceiptCategory) = when (category) {
    ReceiptCategory.FOOD_DRINK -> Color(0xFFB07D22)
    ReceiptCategory.GROCERIES -> Color(0xFF1F8F4E)
    ReceiptCategory.PHARMACY -> Color(0xFF2A7DA0)
    ReceiptCategory.ELECTRONICS -> Color(0xFF4F6CE2)
    ReceiptCategory.FASHION -> Color(0xFF8A3E9E)
    ReceiptCategory.OTHER -> Color(0xFF6E6E6E)
}

fun categoryIconTint(category: ReceiptCategory, baseColor: Color): Color {
    return when (category) {
        ReceiptCategory.FOOD_DRINK -> Color(0xFFEBBF11)
        ReceiptCategory.GROCERIES -> accentColor
        else -> lighten(baseColor, 0.55f)
    }
}

@Composable
fun CategoryIconBadge(
    category: ReceiptCategory,
    size: Dp,
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(24.dp)
) {
    val backgroundColor = categoryColor(category)
    Surface(
        shape = shape,
        color = backgroundColor,
        modifier = modifier.size(size)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Icon(
                painter = painterResource(id = categoryIcon(category)),
                contentDescription = null,
                tint = categoryIconTint(category, backgroundColor)
            )
        }
    }
}

private fun lighten(color: Color, factor: Float): Color {
    val clamped = factor.coerceIn(0f, 1f)
    return Color(
        red = color.red + (1f - color.red) * clamped,
        green = color.green + (1f - color.green) * clamped,
        blue = color.blue + (1f - color.blue) * clamped,
        alpha = color.alpha
    )
}
