package com.pef.mendelu.novotnysamuel.spendradar.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem
import java.io.Serializable
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
): Serializable, ClusterItem {
    override fun getPosition(): LatLng {
        return LatLng(latitude?: 0.0 , longitude?: 0.0)
    }

    override fun getTitle(): String? {
        return type.toString()
    }

    override fun getSnippet(): String? {
        return type.toString()
    }

    override fun getZIndex(): Float? {
        return 0.0f
    }

}
