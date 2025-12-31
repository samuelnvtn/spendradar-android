package com.pef.mendelu.novotnysamuel.spendradar.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "receipt_items",
    foreignKeys = [
        ForeignKey(
            entity = Receipt::class,
            parentColumns = ["id"],
            childColumns = ["receiptId"],
            onDelete = ForeignKey.CASCADE
        )
    ])
data class ReceiptItem(
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null,
    var receiptId: Long,
    var itemName: String,
    var quantity: Int,
    var pricePerUnit: Long
) {
}