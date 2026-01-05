package com.pef.mendelu.novotnysamuel.spendradar.map

import android.content.Context
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.GoogleMap
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer
import com.pef.mendelu.novotnysamuel.spendradar.model.Receipt
import com.pef.mendelu.novotnysamuel.spendradar.model.ReceiptCategory
import com.pef.mendelu.novotnysamuel.spendradar.ui.elements.categoryColor
import com.pef.mendelu.novotnysamuel.spendradar.ui.elements.categoryIcon
import com.pef.mendelu.novotnysamuel.spendradar.ui.elements.categoryIconTint
import com.pef.mendelu.novotnysamuel.spendradar.ui.elements.customMarkerIcon

class ClusterMapRenderer(
    private val context: Context,
    map: GoogleMap,
    clusterManager: ClusterManager<Receipt>
) : DefaultClusterRenderer<Receipt>(context, map, clusterManager) {

    private val markerCache = mutableMapOf<ReceiptCategory, BitmapDescriptor>()

    override fun shouldRenderAsCluster(cluster: Cluster<Receipt?>): Boolean {
        return cluster.size > 5
    }

    override fun onBeforeClusterItemRendered(item: Receipt, markerOptions: MarkerOptions) {
        val category = ReceiptCategory.fromStorage(item.type)
        val icon = markerCache.getOrPut(category) {
            val baseColor = categoryColor(category)
            customMarkerIcon(
                context = context,
                iconRes = categoryIcon(category),
                backgroundColor = baseColor,
                iconTint = categoryIconTint(category, baseColor)
            )
        }
        markerOptions.icon(icon)
    }
}
