package com.pef.mendelu.novotnysamuel.spendradar.model

import androidx.room.Embedded
import androidx.room.Relation

data class ReceiptWithItems(
    @Embedded val receipt: Receipt,
    @Relation(
        parentColumn = "id",
        entityColumn = "receiptId"
    )
    val items: List<ReceiptItem>
)