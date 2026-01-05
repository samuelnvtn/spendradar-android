package com.pef.mendelu.novotnysamuel.spendradar.ui.screens.stats

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TextButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pef.mendelu.novotnysamuel.spendradar.R
import com.pef.mendelu.novotnysamuel.spendradar.navigation.Destination
import com.pef.mendelu.novotnysamuel.spendradar.navigation.INavigationRouter
import com.pef.mendelu.novotnysamuel.spendradar.ui.elements.BaseScreen
import com.pef.mendelu.novotnysamuel.spendradar.ui.elements.BottomNavBar
import com.pef.mendelu.novotnysamuel.spendradar.ui.elements.PlaceholderScreenContent
import com.pef.mendelu.novotnysamuel.spendradar.ui.elements.formatCurrency
import com.pef.mendelu.novotnysamuel.spendradar.ui.theme.accentColor
import com.pef.mendelu.novotnysamuel.spendradar.ui.theme.barButton
import com.pef.mendelu.novotnysamuel.spendradar.ui.theme.barColor
import com.pef.mendelu.novotnysamuel.spendradar.ui.theme.basicMargin
import com.pef.mendelu.novotnysamuel.spendradar.ui.theme.darkGreen
import com.pef.mendelu.novotnysamuel.spendradar.ui.theme.halfMargin
import com.pef.mendelu.novotnysamuel.spendradar.ui.theme.textColor
import ir.ehsannarmani.compose_charts.PieChart
import ir.ehsannarmani.compose_charts.models.Pie
import com.pef.mendelu.novotnysamuel.spendradar.ui.elements.categoryLabel
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign

@Composable
fun StatsScreen(
    navigation: INavigationRouter
){
    val viewModel = hiltViewModel<StatsViewModel>()
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val hasData = state.summary.totalSpent > 0L ||
        state.summary.mostVisitedStore != null ||
        state.summary.topSpendStore != null ||
        state.pieItems.isNotEmpty()

    BaseScreen(
        bottomBarContent = {
            BottomNavBar(
                selectedDestination = Destination.StatsScreen,
                navigation = navigation
            )
        },
        placeholderScreenContent = if (!state.isLoading && !hasData) {
            PlaceholderScreenContent(
                image = R.drawable.undraw_graph,
                text = stringResource(R.string.stats_empty)
            )
        } else null,
        actions = {
            IconButton(onClick = {
                navigation.navigateToSettings()
            }) {
                Icon(
                    imageVector = Icons.Outlined.Settings,
                    contentDescription = "Settings",
                    tint = textColor
                )
            }},
        topBarText = (stringResource(R.string.stats_title)),
        showLoading = state.isLoading
    ) {
        StatsScreenContent(
            paddingValues = it,
            state = state,
            onRangeChange = viewModel::onRangeChange
        )
    }
}

@Composable
fun StatsScreenContent(
    paddingValues: PaddingValues,
    state: StatsUIState,
    onRangeChange: (StatsRange) -> Unit
){
    val scrollState = rememberScrollState()
    var chartData by remember(state.pieItems) {
        mutableStateOf(state.pieItems.map {
            Pie(
                //label = categoryLabel(it.category),
                data = it.value,
                color = it.color,
                selectedColor = it.color.copy(alpha = 0.7f)
            )
        })
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(paddingValues)
            .padding(horizontal = basicMargin, vertical = halfMargin),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        StatsTabs(
            selected = state.range,
            onSelected = onRangeChange
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = stringResource(R.string.stats_section_title),
                style = MaterialTheme.typography.titleMedium,
                color = textColor
            )

            Surface(
                shape = RoundedCornerShape(20.dp),
                color = darkGreen,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StatLabelValue(
                        label = statsTotalLabel(state.range),
                        value = formatCurrency(state.summary.totalSpent, stringResource(R.string.currency_czk))
                    )
                    StatLabelValue(
                        label = stringResource(R.string.stats_most_visited),
                        value = state.summary.mostVisitedStore ?: stringResource(R.string.stats_empty)
                    )
                    StatLabelValue(
                        label = stringResource(R.string.stats_top_spend),
                        value = state.summary.topSpendStore ?: stringResource(R.string.stats_empty)
                    )
                }
            }

            Text(
                text = stringResource(R.string.stats_graph_title),
                style = MaterialTheme.typography.titleMedium,
                color = textColor
            )

            Surface(
                shape = RoundedCornerShape(20.dp),
                color = barColor,
                border = BorderStroke(1.dp, barButton),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (state.pieItems.isEmpty()) {
                        Text(
                            text = stringResource(R.string.stats_empty),
                            style = MaterialTheme.typography.bodyMedium,
                            color = textColor.copy(alpha = 0.7f)
                        )
                    } else {
                        PieChart(
                            modifier = Modifier.size(200.dp),
                            data = chartData,
                            onPieClick = { clicked ->
                                val idx = chartData.indexOf(clicked)
                                chartData = chartData.mapIndexed { mapIndex, pie ->
                                    pie.copy(selected = mapIndex == idx)
                                }
                            },
                            selectedScale = 1.15f,
                            spaceDegree = 6f,
                            selectedPaddingDegree = 3f,
                            style = Pie.Style.Stroke(width = 36.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = statsTotalLabel(state.range),
                            style = MaterialTheme.typography.bodySmall,
                            color = textColor.copy(alpha = 0.7f)
                        )
                        Text(
                            text = formatCurrency(state.summary.totalSpent, stringResource(R.string.currency_czk)),
                            style = MaterialTheme.typography.titleMedium,
                            color = textColor,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        StatsLegend(items = state.pieItems)
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun StatsTabs(
    selected: StatsRange,
    onSelected: (StatsRange) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        StatsTabItem(
            label = stringResource(R.string.stats_tab_day),
            selected = selected == StatsRange.DAY,
            onClick = { onSelected(StatsRange.DAY) }
        )
        StatsTabItem(
            label = stringResource(R.string.stats_tab_week),
            selected = selected == StatsRange.WEEK,
            onClick = { onSelected(StatsRange.WEEK) }
        )
        StatsTabItem(
            label = stringResource(R.string.stats_tab_month),
            selected = selected == StatsRange.MONTH,
            onClick = { onSelected(StatsRange.MONTH) }
        )
        StatsTabItem(
            label = stringResource(R.string.stats_tab_year),
            selected = selected == StatsRange.YEAR,
            onClick = { onSelected(StatsRange.YEAR) }
        )
    }
}

@Composable
private fun RowScope.StatsTabItem(
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .weight(1f)
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextButton(
            onClick = onClick,
            contentPadding = PaddingValues(horizontal = 6.dp, vertical = 0.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = label,
                color = if (selected) accentColor else textColor.copy(alpha = 0.7f),
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                maxLines = 1
            )
        }
        Divider(
            thickness = 2.dp,
            color = if (selected) accentColor else Color.Transparent
        )
    }
}

@Composable
private fun StatLabelValue(label: String, value: String) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = textColor.copy(alpha = 0.7f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.titleLarge,
            color = textColor
        )
    }
}

@Composable
private fun StatsLegend(items: List<StatsPieItem>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items.chunked(2).forEach { rowItems ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                rowItems.forEach { item ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Surface(
                                color = item.color,
                                shape = RoundedCornerShape(4.dp),
                                modifier = Modifier.size(8.dp)
                            ) {}
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = categoryLabel(item.category),
                            style = MaterialTheme.typography.bodySmall,
                            color = textColor
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "${item.percent}%",
                            style = MaterialTheme.typography.bodySmall,
                            color = textColor.copy(alpha = 0.7f)
                        )
                    }
                }
                if (rowItems.size == 1) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
private fun statsTotalLabel(range: StatsRange): String {
    return when (range) {
        StatsRange.DAY -> stringResource(R.string.stats_total_day)
        StatsRange.WEEK -> stringResource(R.string.stats_total_week)
        StatsRange.MONTH -> stringResource(R.string.stats_total_month)
        StatsRange.YEAR -> stringResource(R.string.stats_total_year)
    }
}
