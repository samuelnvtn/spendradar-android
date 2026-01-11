package com.pef.mendelu.novotnysamuel.spendradar.ui.elements

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.pef.mendelu.novotnysamuel.spendradar.ui.theme.basicMargin
import com.pef.mendelu.novotnysamuel.spendradar.ui.theme.textColor


data class PlaceholderScreenContent(
    val image: Int? = null,
    val text: String? = null,
    val testTag: String? = null
)


@Composable
fun PlaceholderScreen(
    paddingValues: PaddingValues,
    placeholderScreenContent: PlaceholderScreenContent
){
    val tagModifier = placeholderScreenContent.testTag?.let { Modifier.testTag(it) } ?: Modifier
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                top = paddingValues.calculateTopPadding(),
                start = basicMargin,
                end = basicMargin,
                bottom = 0.dp
            )
            .then(tagModifier)
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally // Vycentrování obsahu sloupce
        ) {
            if (placeholderScreenContent.image != null){
                Image(
                    painter = painterResource(placeholderScreenContent.image),
                    contentDescription = null,
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier.width(300.dp)
                )

            }
            if (placeholderScreenContent.text != null){
                Spacer(modifier = Modifier.height(16.dp)) // Odsazení mezi obrázkem a textem
                Text(
                    text = placeholderScreenContent.text,
                    color = textColor,
                    textAlign = TextAlign.Center // Vycentrování samotného textu
                )
            }
        }
    }

}
