
package com.quackmove.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.quackmove.app.ui.theme.QuackMoveTheme
import com.quackmove.app.feature.home.MapScreen
import com.quackmove.app.navigation.QuackMoveNavigation
import org.maplibre.android.MapLibre
import org.maplibre.android.camera.CameraPosition
import org.maplibre.android.camera.CameraUpdateFactory
import org.maplibre.android.geometry.LatLng
import org.maplibre.android.maps.MapLibreMap
import org.maplibre.android.maps.MapView

private const val DARK_MAP_STYLE =
    "https://tiles.openfreemap.org/styles/dark"

private const val LIGHT_MAP_STYLE =
    "https://tiles.openfreemap.org/styles/bright"

private val villaNuevaCenter = LatLng(
    14.5269,
    -90.5877
)

class MainActivity : ComponentActivity() {

    private lateinit var mapView: MapView

    private var mapInstance: MapLibreMap? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        MapLibre.getInstance(this)

        mapView = MapView(this)

        mapView.onCreate(savedInstanceState)

        mapView.getMapAsync { map ->

            mapInstance = map

            map.setStyle(DARK_MAP_STYLE)

            map.cameraPosition =
                CameraPosition.Builder()
                    .target(villaNuevaCenter)
                    .zoom(15.0)
                    .build()

            map.uiSettings.isScrollGesturesEnabled = true

            map.uiSettings.isZoomGesturesEnabled = true

            map.uiSettings.isRotateGesturesEnabled = true

            map.uiSettings.isCompassEnabled = false
        }

        setContent {

            QuackMoveTheme {

                val navController = rememberNavController()

                QuackMoveNavigation(

                    navController = navController,

                    mapScreen = {
                            onOpenRegistration,
                            onOpenChargers,
                            onOpenRoute ->

                        MapScreen(

                            mapView = mapView,

                            onMapStyleChange = { darkMap ->

                                val style = if (darkMap) {
                                    DARK_MAP_STYLE
                                } else {
                                    LIGHT_MAP_STYLE
                                }

                                mapInstance?.setStyle(style)
                            },

                            onReturnToCenter = {

                                mapInstance?.animateCamera(

                                    CameraUpdateFactory.newLatLngZoom(
                                        villaNuevaCenter,
                                        16.0
                                    )
                                )
                            },

                            onOpenRegistration = onOpenRegistration,

                            onOpenChargers = onOpenChargers,

                            onOpenRoute = onOpenRoute
                        )
                    }
                )
            }
        }
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        mapView.onPause()
        super.onPause()
    }

    override fun onStop() {
        mapView.onStop()
        super.onStop()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        mapView.onSaveInstanceState(outState)
        super.onSaveInstanceState(outState)
    }

    override fun onDestroy() {
        mapInstance = null
        mapView.onDestroy()
        super.onDestroy()
    }
}

