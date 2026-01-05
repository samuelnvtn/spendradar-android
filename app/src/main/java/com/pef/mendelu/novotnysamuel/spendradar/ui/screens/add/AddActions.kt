package com.pef.mendelu.novotnysamuel.spendradar.ui.screens.add

import com.google.android.gms.maps.model.LatLng
import com.pef.mendelu.novotnysamuel.spendradar.model.ReceiptCategory
import java.time.LocalTime

interface AddActions {
    fun onStoreNameChange(value: String)
    fun onDateChange(value: Long?)
    fun onTimeChange(value: LocalTime)
    fun onLocationChange(value: LatLng)
    fun onCategoryChange(category: ReceiptCategory)
    fun onAddItem()
    fun onRemoveItem(index: Int)
    fun onItemNameChange(index: Int, value: String)
    fun onItemQuantityChange(index: Int, value: String)
    fun onItemPriceChange(index: Int, value: String)
    fun onSave()
    fun onSaveHandled()
}
