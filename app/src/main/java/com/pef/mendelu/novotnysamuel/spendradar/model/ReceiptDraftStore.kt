package com.pef.mendelu.novotnysamuel.spendradar.model

import com.google.android.gms.maps.model.LatLng
import com.pef.mendelu.novotnysamuel.spendradar.ui.screens.add.AddReceiptItemInput
import java.time.LocalTime

data class ReceiptDraft(
    val storeName: String,
    val dateMillis: Long?,
    val time: LocalTime,
    val location: LatLng,
    val category: ReceiptCategory,
    val items: List<AddReceiptItemInput>,
    val totalPrice: Long?,
    val currency: String
)

object ReceiptDraftStore {
    private var draft: ReceiptDraft? = null

    fun set(value: ReceiptDraft) {
        draft = value
    }

    fun consume(): ReceiptDraft? {
        val value = draft
        draft = null
        return value
    }
}
