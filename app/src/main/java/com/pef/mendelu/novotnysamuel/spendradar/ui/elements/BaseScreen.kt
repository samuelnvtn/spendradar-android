package com.pef.mendelu.novotnysamuel.spendradar.ui.elements

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.pef.mendelu.novotnysamuel.spendradar.ui.theme.backgroundColor
import com.pef.mendelu.novotnysamuel.spendradar.ui.theme.barColor
import com.pef.mendelu.novotnysamuel.spendradar.ui.theme.textColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BaseScreen(
    topBarText: String,
    onBackClick: (() -> Unit)? = null,
    showLoading: Boolean = false,
    placeholderScreenContent: PlaceholderScreenContent? = null,
    showBottomBar: Boolean = true,
    bottomBarContent: @Composable () -> Unit = {},
    floatingActionButton: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
    content: @Composable (paddingValues: PaddingValues) -> Unit,
    ){

    Scaffold(
        containerColor = backgroundColor, // Aplikování barvy na Scaffold
        topBar = {
            TopAppBar(
                title = {
                    Text(text = topBarText, color = textColor)
                },
                navigationIcon = {
                    if (onBackClick != null){
                        IconButton(onClick = onBackClick) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = null,
                                tint = textColor
                            )
                        }
                    }
                },
                actions = actions,
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = barColor // Sladění TopBaru s tvým barevným schématem
                )
            )
        },
        bottomBar = {
            if (showBottomBar) {
                NavigationBar(containerColor = barColor) {
                    bottomBarContent()
                }
            }
        },
        floatingActionButton = floatingActionButton
    ) {
        if (showLoading) {
            LoadingScreen()
        } else if (placeholderScreenContent != null){
            PlaceholderScreen(
                paddingValues = it,
                placeholderScreenContent = placeholderScreenContent)
        } else {
            content(it)
        }
    }

}
