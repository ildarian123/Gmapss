package com.example.gmaps

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem

class MapItem(
    lat: Double,
    lng: Double,
    private val title: String,
    private val snippet: String
) : ClusterItem {

    private val position: LatLng = LatLng(lat, lng)

    override fun getPosition(): LatLng {
        return this.position
    }

    override fun getTitle(): String {
        return this.title
    }

    override fun getSnippet(): String {
        return this.snippet
    }


}