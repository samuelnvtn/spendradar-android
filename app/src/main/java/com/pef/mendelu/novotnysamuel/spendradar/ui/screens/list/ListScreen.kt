package com.pef.mendelu.novotnysamuel.spendradar.ui.screens.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Divider
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pef.mendelu.novotnysamuel.spendradar.R
import com.pef.mendelu.novotnysamuel.spendradar.model.Receipt
import com.pef.mendelu.novotnysamuel.spendradar.navigation.Destination
import com.pef.mendelu.novotnysamuel.spendradar.navigation.INavigationRouter
import com.pef.mendelu.novotnysamuel.spendradar.ui.elements.AddReceiptDialog
import com.pef.mendelu.novotnysamuel.spendradar.ui.elements.BaseScreen
import com.pef.mendelu.novotnysamuel.spendradar.ui.elements.BottomNavBar
import com.pef.mendelu.novotnysamuel.spendradar.ui.elements.PlaceholderScreenContent
import com.pef.mendelu.novotnysamuel.spendradar.ui.elements.ReceiptRow
import com.pef.mendelu.novotnysamuel.spendradar.ui.elements.formatCurrency
import com.pef.mendelu.novotnysamuel.spendradar.ui.theme.accentColor
import com.pef.mendelu.novotnysamuel.spendradar.ui.theme.barButton
import com.pef.mendelu.novotnysamuel.spendradar.ui.theme.basicMargin
import com.pef.mendelu.novotnysamuel.spendradar.ui.theme.darkGreen
import com.pef.mendelu.novotnysamuel.spendradar.ui.theme.halfMargin
import com.pef.mendelu.novotnysamuel.spendradar.ui.theme.textColor
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun ListScreen(
    navigation: INavigationRouter
){
    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        AddReceiptDialog(
            onDismissRequest = { showDialog = false },
            navigation = navigation
        )
    }

    val viewModel = hiltViewModel<ListViewModel>()

    val state = viewModel.listUIState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.loadReceipts()
    }

    val receipts = (state.value as? ListUIState.Success)?.receipts.orEmpty()
    val monthlyTotal = (state.value as? ListUIState.Success)?.monthlyTotal ?: 0L

    BaseScreen(
        bottomBarContent = {
            BottomNavBar(
                selectedDestination = Destination.ListScreen,
                navigation = navigation
            )
        },
        placeholderScreenContent = if (receipts.isEmpty()) {
            PlaceholderScreenContent(
                image = R.drawable.undraw_empty_void,
                text = (stringResource(R.string.emptylist))
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
        topBarText = (stringResource(R.string.receipts)),
        floatingActionButton = {
            FloatingActionButton(onClick = {
                showDialog = true
            }, containerColor = barButton, contentColor = accentColor,
                modifier = Modifier.offset(y = (-16).dp)

            ) {
                Icon(
                    painter = painterResource(id = R.drawable.outline_barcode_scanner),
                    contentDescription = null
                )
            }
        }
    ) {
        ListScreenContent(
            paddingValues = it,
            monthlyTotal = monthlyTotal,
            receipts = receipts
        )
    }
}

@Composable
fun ListScreenContent(
    paddingValues: PaddingValues,
    monthlyTotal: Long,
    receipts: List<Receipt>
){
    val items = remember(receipts) { buildListItems(receipts) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(horizontal = basicMargin, vertical = halfMargin),
    ) {
        MonthlySummaryCard(total = monthlyTotal)
        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 80.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(items, key = { it.key }) { item ->
                when (item) {
                    is ListItem.DateHeader -> DateHeader(text = item.label)
                    is ListItem.ReceiptRow -> {
                        ReceiptRow(receipt = item.receipt)
                        Divider(color = barButton.copy(alpha = 0.4f))
                    }
                }
            }
        }
    }

}

private sealed class ListItem(val key: String) {
    class DateHeader(val label: String) : ListItem("header-$label")
    class ReceiptRow(val receipt: Receipt) : ListItem("receipt-${receipt.id ?: receipt.hashCode()}")
}

private fun buildListItems(receipts: List<Receipt>): List<ListItem> {
    val formatter = DateTimeFormatter.ofPattern("d. M. yyyy", Locale.getDefault())
    val items = mutableListOf<ListItem>()
    var lastDateLabel: String? = null
    receipts.forEach { receipt ->
        val dateLabel = receipt.timestamp
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
            .format(formatter)
        if (dateLabel != lastDateLabel) {
            items.add(ListItem.DateHeader(dateLabel))
            lastDateLabel = dateLabel
        }
        items.add(ListItem.ReceiptRow(receipt))
    }
    return items
}

@Composable
private fun MonthlySummaryCard(total: Long) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = darkGreen,
        //border = BorderStroke(1.dp, barButton),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = stringResource(R.string.monthly_total_label),
                color = textColor.copy(alpha = 0.7f),
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = formatCurrency(total),
                color = textColor,
                style = MaterialTheme.typography.headlineMedium
            )
        }
    }
}

@Composable
private fun DateHeader(text: String) {
    Text(
        text = text,
        color = textColor,
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
    )
}
