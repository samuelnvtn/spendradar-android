package com.pef.mendelu.novotnysamuel.spendradar.ui.screens.add

import com.google.android.gms.maps.model.LatLng
import java.time.LocalTime

data class AddReceiptItemInput(
    val name: String = "",
    val quantity: String = "",
    val price: String = ""
)

data class AddUIState(
    val storeName: String = "",
    val dateMillis: Long? = System.currentTimeMillis(),
    val time: LocalTime = LocalTime.now(),
    val location: LatLng = LatLng(49.2098028, 16.6145219),
    val items: List<AddReceiptItemInput> = emptyList(),
    val isSaving: Boolean = false,
    val saveCompleted: Boolean = false
)
