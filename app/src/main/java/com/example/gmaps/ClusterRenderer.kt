package com.example.gmaps

import android.content.Context
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer
import com.google.maps.android.ui.IconGenerator
import kotlin.math.roundToInt

class ClusterRenderer(
    private val context: Context?,
    map: GoogleMap?,
    clusterManager: ClusterManager<MapItem>?
) : DefaultClusterRenderer<MapItem>(context, map, clusterManager) {

    private var shouldCluster = true
    private val icg = IconGenerator(context)

    override fun onBeforeClusterRendered(cluster: Cluster<MapItem>, markerOptions: MarkerOptions) {
        super.onBeforeClusterRendered(cluster, markerOptions)

        icg.setTextAppearance(context, R.style.iconGenText)
        icg.setBackground(
            ContextCompat.getDrawable(
                context!!,
                R.drawable.ic_baseline_add_circle_24
            )
        )
        var avarage = 0.0
        cluster.items.forEach {
            avarage = avarage.plus(it.title.toInt())
        }
        val bm = icg.makeIcon(((avarage / cluster.size).roundToInt()).toString())
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(bm))

    }

    override fun onBeforeClusterItemRendered(item: MapItem, markerOptions: MarkerOptions) {
        super.onBeforeClusterItemRendered(item, markerOptions)
        icg.setTextAppearance(context, R.style.iconGenText)
        icg.setBackground(
            ContextCompat.getDrawable(
                context!!,
                R.drawable.ic_baseline_add_circle_24
            )
        )
        val bm = icg.makeIcon(item.title)
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(bm))

    }

    fun setMarkersToClaster(shouldCluster: Boolean) {
        this.shouldCluster = shouldCluster

    }

    override fun shouldRenderAsCluster(cluster: Cluster<MapItem?>): Boolean {
        return this.shouldCluster

    }
}