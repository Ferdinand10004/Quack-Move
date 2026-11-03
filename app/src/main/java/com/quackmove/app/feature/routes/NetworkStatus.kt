package com.quackmove.app.feature.routes

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Handler
import android.os.Looper
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext

// Comprueba si Android detecta acceso a Internet.
private fun ConnectivityManager.hasInternet(): Boolean {
    val network = activeNetwork ?: return false
    val capabilities = getNetworkCapabilities(network)
        ?: return false

    return capabilities.hasCapability(
        NetworkCapabilities.NET_CAPABILITY_INTERNET
    ) && capabilities.hasCapability(
        NetworkCapabilities.NET_CAPABILITY_VALIDATED
    )
}

@Composable
fun rememberInternetAvailable(): Boolean {

    val context = LocalContext.current.applicationContext

    val manager = remember(context) {
        context.getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
    }

    var isOnline by remember(manager) {
        mutableStateOf(manager.hasInternet())
    }

    DisposableEffect(manager) {

        val callback =
            object : ConnectivityManager.NetworkCallback() {

                override fun onCapabilitiesChanged(
                    network: Network,
                    capabilities: NetworkCapabilities
                ) {
                    isOnline = capabilities.hasCapability(
                        NetworkCapabilities.NET_CAPABILITY_INTERNET
                    ) && capabilities.hasCapability(
                        NetworkCapabilities.NET_CAPABILITY_VALIDATED
                    )
                }

                override fun onLost(network: Network) {
                    isOnline = false
                }
            }

        manager.registerDefaultNetworkCallback(
            callback,
            Handler(Looper.getMainLooper())
        )

        onDispose {
            manager.unregisterNetworkCallback(callback)
        }
    }

    return isOnline
}
