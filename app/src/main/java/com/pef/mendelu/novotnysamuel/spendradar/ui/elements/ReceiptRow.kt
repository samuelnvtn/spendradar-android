package com.pef.mendelu.novotnysamuel.spendradar.ui.elements

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.pef.mendelu.novotnysamuel.spendradar.R
import com.pef.mendelu.novotnysamuel.spendradar.model.Receipt
import com.pef.mendelu.novotnysamuel.spendradar.model.ReceiptCategory
import com.pef.mendelu.novotnysamuel.spendradar.testing.TestTagReceiptRowPrefix
import com.pef.mendelu.novotnysamuel.spendradar.ui.theme.textColor

@Composable
fun ReceiptRow(
    receipt: Receipt,
    onRowClick: () -> Unit
) {
    val category = ReceiptCategory.fromStorage(receipt.type)
    val rowTag = TestTagReceiptRowPrefix + (receipt.id ?: receipt.hashCode())

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable(onClick = onRowClick)
            .testTag(rowTag),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CategoryIconBadge(category = category, size = 48.dp)

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = receipt.storeName,
                color = textColor,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,

                )
            Text(
                text = categoryLabel(category),
                color = textColor.copy(alpha = 0.7f),
                style = MaterialTheme.typography.bodySmall
            )
        }

        Text(
            text = "-${formatCurrency(receipt.totalPrice, stringResource(R.string.currency_czk))}",
            color = textColor,
            style = MaterialTheme.typography.titleMedium
        )
    }
}
