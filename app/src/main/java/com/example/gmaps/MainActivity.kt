package com.example.gmaps

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.algo.NonHierarchicalDistanceBasedAlgorithm
import com.google.maps.android.ui.IconGenerator
import kotlin.random.Random

@Suppress("SameParameterValue")
class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private var minLat: Double = 0.0
    private var minLng: Double = 0.0
    private var maxLat: Double = 0.0
    private var maxLng: Double = 0.0
    private var mapReady: Boolean = false
    private var camerazoom = 0.0f
    private lateinit var clusterManager: ClusterManager<MapItem>
    private var items = mutableListOf<MapItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        minLat = 48.417304
        maxLat = 51.170849
        minLng = 24.025420
        maxLng = 34.132841

        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            if (mapReady) {
                mMap.clear()
                setUpClusterer()
            }
        }

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.setOnCameraIdleListener {
            clusterManager.onCameraIdle()
        }

        clusterManager = ClusterManager(this, mMap)
        camerazoom = mMap.cameraPosition.zoom
        mapReady = true
    }

    @SuppressLint("PotentialBehaviorOverride")
    private fun setUpClusterer() {

        clusterManager = ClusterManager<MapItem>(this, mMap)

        mMap.setOnMarkerClickListener(clusterManager)
        mMap.setOnCameraIdleListener(clusterManager)

        mMap.setOnCameraIdleListener {
            if (camerazoom > mMap.cameraPosition.zoom) {
                mMap.clear()
                val algorithm: NonHierarchicalDistanceBasedAlgorithm<MapItem> =
                    NonHierarchicalDistanceBasedAlgorithm<MapItem>()
                algorithm.maxDistanceBetweenClusteredItems = 50

                clusterManager.algorithm = algorithm
                clusterManager.addItems(items)
                val customRenderer = ClusterRenderer(this, mMap, clusterManager)
                customRenderer.setMarkersToClaster(true)
                clusterManager.renderer = customRenderer
                clusterManager.cluster()

            } else if (camerazoom < mMap.cameraPosition.zoom) {
                val customRenderer = ClusterRenderer(this, mMap, clusterManager)
                customRenderer.setMarkersToClaster(false)
                clusterManager.renderer = customRenderer
            }
            camerazoom = mMap.cameraPosition.zoom
            clusterManager.onCameraIdle()
        }
        addItems()
    }

    private fun addItems() {
        val renderer = ClusterRenderer(this, mMap, ClusterManager(this, mMap))
        renderer.setMarkersToClaster(false)
        clusterManager.renderer = renderer
        items.clear()
        repeat(100) {

            val lat = Random.nextDouble(minLat, maxLat)
            val lng = Random.nextDouble(minLng, maxLng)
            val icg = IconGenerator(this)
            icg.setTextAppearance(this, R.style.iconGenText)
            icg.setBackground(
                ContextCompat.getDrawable(
                    this,
                    R.drawable.ic_baseline_add_circle_24
                )
            )
            val title = Random.nextInt(1, 10).toString()
            icg.makeIcon(title)
            val snippet = ""
            val infoWindowItem = MapItem(lat, lng, title, snippet)
            items.add(infoWindowItem)
        }
        clusterManager.addItems(items)
        mMap.moveCamera(
            CameraUpdateFactory.newLatLng(
                LatLng(
                    items.last().position.latitude,
                    items.last().position.longitude
                )
            )
        )
    }
}