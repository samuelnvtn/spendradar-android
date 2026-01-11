package com.pef.mendelu.novotnysamuel.spendradar.ui.theme

import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val barColor = Color(0xFF000000)
val textColor = Color(0xFFFFFFFF)
val barButton = Color(0xFF156739)
val lightBlack = Color(0xFF1E1F1F)

val backgroundColor = Color(0xFF000000)
val greenBackground = Color(0xFF12251A)
val darkBackground = Color(0xFF16201D)


//12251A

val lightButton = Color(0xFF156739)

val darkGreen = Color(0xFF1A2C23)
val ultraGreen = Color(0xFF1A2C23)


val accentColor = Color(0xFF0EB957)

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

val selectionColors = TextSelectionColors(
    handleColor = accentColor,
    backgroundColor = barButton
)

@Composable
fun textFieldColors(
    trailingIconColor: Color = textColor
) = TextFieldDefaults.colors(
    focusedContainerColor = barColor,
    unfocusedContainerColor = barColor,
    focusedTextColor = textColor,
    unfocusedTextColor = textColor,
    focusedIndicatorColor = accentColor,
    unfocusedIndicatorColor = barButton,
    cursorColor = accentColor,
    focusedLabelColor = accentColor,
    unfocusedLabelColor = accentColor,
    focusedTrailingIconColor = trailingIconColor,
    unfocusedTrailingIconColor = trailingIconColor,
    selectionColors = selectionColors,
)
