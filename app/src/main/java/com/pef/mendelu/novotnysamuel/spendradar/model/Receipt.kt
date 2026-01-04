package com.pef.mendelu.novotnysamuel.spendradar.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant

@Entity(tableName = "receipts")
data class Receipt(
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null,
    var storeName: String,
    var type: String? = null,
    var latitude: Double,
    var longitude: Double,
    var timestamp: Instant,
    var totalPrice: Long,
    var currency: String,
    var items: List<ReceiptItem> = emptyList()
)
