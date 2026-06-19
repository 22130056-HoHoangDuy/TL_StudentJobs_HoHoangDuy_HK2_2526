package com.studentjobs.app.feature.location

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import org.osmdroid.config.Configuration
import org.osmdroid.events.MapListener
import org.osmdroid.events.ScrollEvent
import org.osmdroid.events.ZoomEvent
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView

@SuppressLint("RememberReturnType")
@Composable
fun MapLocationPicker(
    initialLatitude: Double = 10.8735,
    initialLongitude: Double = 106.7851,
    onLocationChanged: (Double, Double) -> Unit
) {

    val context = LocalContext.current

    AndroidView(

        factory = {

            Configuration
                .getInstance()
                .userAgentValue = context.packageName

            val mapView = MapView(context)

            mapView.setTileSource(
                TileSourceFactory.MAPNIK
            )

            mapView.setMultiTouchControls(true)

            mapView.controller.setZoom(16.0)

            val startPoint = GeoPoint(
                initialLatitude,
                initialLongitude
            )

            mapView.controller.setCenter(
                startPoint
            )

            onLocationChanged(
                initialLatitude,
                initialLongitude
            )

            mapView.addMapListener(

                object : MapListener {

                    override fun onScroll(
                        event: ScrollEvent?
                    ): Boolean {

                        val center =
                            mapView.mapCenter as GeoPoint

                        onLocationChanged(
                            center.latitude,
                            center.longitude
                        )

                        return true
                    }

                    override fun onZoom(
                        event: ZoomEvent?
                    ): Boolean {

                        val center =
                            mapView.mapCenter as GeoPoint

                        onLocationChanged(
                            center.latitude,
                            center.longitude
                        )

                        return true
                    }
                }
            )

            mapView
        }
    )
}