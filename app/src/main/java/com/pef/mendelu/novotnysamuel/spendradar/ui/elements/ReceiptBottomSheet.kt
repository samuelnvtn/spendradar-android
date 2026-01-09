package com.pef.mendelu.novotnysamuel.spendradar.ui.elements

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.pef.mendelu.novotnysamuel.spendradar.testing.TestTagMapBottomSheetDetail
import com.pef.mendelu.novotnysamuel.spendradar.ui.theme.accentColor
import com.pef.mendelu.novotnysamuel.spendradar.ui.theme.textColor

@Composable
fun ReceiptBottomSheetContent(
    receipt: Receipt,
    onDetailClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp)
    ) {
        val category = ReceiptCategory.fromStorage(receipt.type)
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CategoryIconBadge(category = category, size = 40.dp)
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = receipt.storeName,
                    style = MaterialTheme.typography.titleLarge,
                    color = textColor
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = categoryLabel(category),
                    style = MaterialTheme.typography.bodyMedium,
                    color = textColor.copy(alpha = 0.7f)
                )
            }
            IconButton(
                onClick = onDetailClick,
                modifier = Modifier.testTag(TestTagMapBottomSheetDetail)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Info,
                    contentDescription = stringResource(R.string.detail),
                    tint = accentColor
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(R.string.total_label),
            style = MaterialTheme.typography.bodySmall,
            color = textColor.copy(alpha = 0.7f)
        )
        Text(
            text = formatCurrency(receipt.totalPrice, stringResource(R.string.currency_czk)),
            style = MaterialTheme.typography.titleMedium,
            color = textColor
        )
        Spacer(modifier = Modifier.height(12.dp))

        if (receipt.items.isEmpty()) {
            Text(
                text = stringResource(R.string.no_items),
                style = MaterialTheme.typography.bodySmall,
                color = textColor.copy(alpha = 0.7f)
            )
        } else {
            receipt.items.forEach { item ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = item.itemName,
                            style = MaterialTheme.typography.bodyMedium,
                            color = textColor
                        )
                        Text(
                            text = "${item.quantity} ks",
                            style = MaterialTheme.typography.bodySmall,
                            color = textColor.copy(alpha = 0.7f)
                        )
                    }
                    Text(
                        text = formatCurrency(item.pricePerUnit, stringResource(R.string.currency_czk)),
                        style = MaterialTheme.typography.bodyMedium,
                        color = textColor
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
}
