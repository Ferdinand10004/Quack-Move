package com.quackmove.app.feature.routes

import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun GoogleMapPreview(
    modifier: Modifier = Modifier
) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            WebView(context).apply {

                // Permitir el funcionamiento de Google Maps
                settings.javaScriptEnabled = true
                settings.domStorageEnabled = true

                webViewClient = WebViewClient()
                webChromeClient = WebChromeClient()

                // Servidor local de QuackMove
                loadUrl(
                    "http://127.0.0.1:8765/map"
                )
            }
        }
    )
}
