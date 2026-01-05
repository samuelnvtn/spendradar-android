package com.pef.mendelu.novotnysamuel.spendradar.ui.elements

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory

fun customMarkerIcon(
    context: Context,
    @DrawableRes iconRes: Int,
    backgroundColor: Color,
    iconTint: Color
): BitmapDescriptor {
    val sizePx = (46 * context.resources.displayMetrics.density).toInt()
    val bitmap = Bitmap.createBitmap(sizePx, sizePx, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)

    val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = backgroundColor.toArgb()
    }

    val centerX = sizePx / 2f
    val centerY = sizePx * 0.34f
    val radius = sizePx * 0.32f
    val tipY = sizePx * 0.82f

    val path = Path().apply {
        moveTo(centerX, centerY - radius)
        cubicTo(
            centerX + radius * 1.15f,
            centerY - radius,
            centerX + radius * 1.2f,
            centerY + radius * 0.42f,
            centerX,
            tipY
        )
        cubicTo(
            centerX - radius * 1.2f,
            centerY + radius * 0.42f,
            centerX - radius * 1.15f,
            centerY - radius,
            centerX,
            centerY - radius
        )
        close()
    }

    canvas.drawPath(path, paint)

    val drawable = ContextCompat.getDrawable(context, iconRes)
        ?: return BitmapDescriptorFactory.fromBitmap(bitmap)
    val wrapped = DrawableCompat.wrap(drawable).mutate()
    DrawableCompat.setTint(wrapped, iconTint.toArgb())

    val iconSize = (radius * 1.2f).toInt()
    val left = (centerX - iconSize / 2f).toInt()
    val top = (centerY - iconSize / 2f).toInt()
    wrapped.setBounds(left, top, left + iconSize, top + iconSize)
    wrapped.draw(canvas)

    return BitmapDescriptorFactory.fromBitmap(bitmap)
}
