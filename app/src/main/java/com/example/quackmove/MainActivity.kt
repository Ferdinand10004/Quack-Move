
package com.example.quackmove

import android.os.Bundle
import android.view.ViewGroup

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Place

import androidx.compose.material3.*

import androidx.compose.runtime.*

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView

import androidx.navigation.compose.rememberNavController

import com.example.quackmove.ui.theme.QuackMoveTheme

import kotlinx.coroutines.launch

import org.maplibre.android.MapLibre
import org.maplibre.android.camera.CameraPosition
import org.maplibre.android.camera.CameraUpdateFactory
import org.maplibre.android.geometry.LatLng
import org.maplibre.android.maps.MapLibreMap
import org.maplibre.android.maps.MapView

private val FondoBoton = Color(0xFF111827)
private val BordeBoton = Color(0xFF405174)
private val VerdeMenta = Color(0xFF43E6B3)
private val Violeta = Color(0xFFB6A0FF)

private val TextoPrincipal = Color(0xFFF5F6FF)
private val TextoSecundario = Color(0xFFB6C2DF)
private val FondoPanel = Color(0xFF142039)

private const val MAPA_OSCURO =
    "https://tiles.openfreemap.org/styles/dark"

private const val MAPA_CLARO =
    "https://tiles.openfreemap.org/styles/bright"

private val villaNueva = LatLng(
    14.5269,
    -90.5877
)

class MainActivity : ComponentActivity() {

    private lateinit var mapView: MapView

    private var mapa: MapLibreMap? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        MapLibre.getInstance(this)

        mapView = MapView(this)

        mapView.onCreate(savedInstanceState)

        mapView.getMapAsync { map ->

            mapa = map

            map.setStyle(MAPA_OSCURO)

            map.cameraPosition =
                CameraPosition.Builder()
                    .target(villaNueva)
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

                    pantallaMapa = {
                            onAbrirRegistro,
                            onAbrirCargadores,
                            onAbrirRuta ->

                        PantallaMapa(

                            mapView = mapView,

                            onCapas = { modoOscuro ->

                                val estilo = if (modoOscuro) {
                                    MAPA_OSCURO
                                } else {
                                    MAPA_CLARO
                                }

                                mapa?.setStyle(estilo)
                            },

                            onUbicacion = {

                                mapa?.animateCamera(

                                    CameraUpdateFactory.newLatLngZoom(
                                        villaNueva,
                                        16.0
                                    )
                                )
                            },

                            onAbrirRegistro = onAbrirRegistro,

                            onAbrirCargadores = onAbrirCargadores,

                            onAbrirRuta = onAbrirRuta
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
        mapa = null
        mapView.onDestroy()
        super.onDestroy()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaMapa(

    mapView: MapView,

    onCapas: (Boolean) -> Unit,

    onUbicacion: () -> Unit,

    onAbrirRegistro: () -> Unit,

    onAbrirCargadores: () -> Unit,

    onAbrirRuta: (String) -> Unit

) {

    val scope = rememberCoroutineScope()

    val drawerState = rememberDrawerState(
        initialValue = DrawerValue.Closed
    )

    val scaffoldState = rememberBottomSheetScaffoldState()

    val snackbarHostState = remember {
        SnackbarHostState()
    }

    var modoOscuro by remember {
        mutableStateOf(true)
    }

    var textoBusqueda by remember {
        mutableStateOf("")
    }

    val buscadorExpandido =
        scaffoldState.bottomSheetState.currentValue ==
                SheetValue.Expanded

    ModalNavigationDrawer(

        drawerState = drawerState,

        gesturesEnabled = false,

        drawerContent = {

            ModalDrawerSheet(

                modifier = Modifier.width(300.dp),

                drawerContainerColor = FondoPanel,

                drawerContentColor = TextoPrincipal

            ) {

                Row(

                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),

                    verticalAlignment = Alignment.CenterVertically,

                    horizontalArrangement =
                        Arrangement.SpaceBetween

                ) {

                    Text(
                        text = "QuackMove",
                        fontSize = 22.sp,
                        color = TextoPrincipal
                    )

                    IconButton(

                        onClick = {

                            scope.launch {
                                drawerState.close()
                            }
                        }

                    ) {

                        Icon(

                            imageVector = Icons.Default.Close,

                            contentDescription = "Cerrar menú",

                            tint = TextoPrincipal
                        )
                    }
                }

                HorizontalDivider(
                    color = BordeBoton
                )

                NavigationDrawerItem(

                    label = {
                        Text("Mapa principal")
                    },

                    selected = true,

                    onClick = {

                        scope.launch {
                            drawerState.close()
                        }
                    },

                    modifier = Modifier.padding(12.dp)
                )

                NavigationDrawerItem(

                    label = {
                        Text("Crear cuenta")
                    },

                    icon = {

                        Icon(

                            imageVector = Icons.Default.PersonAdd,

                            contentDescription = null
                        )
                    },

                    selected = false,

                    onClick = {

                        scope.launch {

                            drawerState.close()

                            onAbrirRegistro()
                        }
                    },

                    modifier = Modifier.padding(
                        horizontal = 12.dp
                    )
                )

                HorizontalDivider(

                    modifier = Modifier.padding(
                        vertical = 16.dp
                    ),

                    color = BordeBoton
                )

                Text(

                    text = "Mi vehículo, favoritos y configuración estarán disponibles próximamente.",

                    modifier = Modifier.padding(20.dp),

                    color = TextoSecundario,

                    fontSize = 14.sp
                )
            }
        }

    ) {

        BottomSheetScaffold(

            scaffoldState = scaffoldState,

            sheetPeekHeight = 104.dp,

            sheetContainerColor = FondoPanel,

            sheetContentColor = TextoPrincipal,

            sheetShape = RoundedCornerShape(

                topStart = 28.dp,

                topEnd = 28.dp
            ),

            sheetDragHandle = null,

            sheetContent = {

                Column(

                    modifier = Modifier

                        .fillMaxWidth()

                        .heightIn(min = 430.dp)

                        .navigationBarsPadding()

                        .verticalScroll(
                            rememberScrollState()
                        )

                        .padding(

                            start = 18.dp,

                            end = 18.dp,

                            top = 12.dp,

                            bottom = 20.dp
                        )

                ) {

                    Box(

                        modifier = Modifier

                            .align(
                                Alignment.CenterHorizontally
                            )

                            .width(46.dp)

                            .height(4.dp)

                            .background(

                                color = Color(0xFF7985AC),

                                shape = CircleShape
                            )

                            .clickable {

                                scope.launch {

                                    scaffoldState
                                        .bottomSheetState
                                        .expand()
                                }
                            }
                    )

                    Spacer(
                        modifier = Modifier.height(16.dp)
                    )

                    BuscadorDestinos(

                        texto = textoBusqueda,

                        expandido = buscadorExpandido,

                        onTextoCambiado = { nuevoTexto ->

                            textoBusqueda = nuevoTexto
                        },

                        onExpandir = {

                            scope.launch {

                                scaffoldState
                                    .bottomSheetState
                                    .expand()
                            }
                        }
                    )

                    Spacer(
                        modifier = Modifier.height(26.dp)
                    )

                    if (textoBusqueda.isBlank()) {

                        Text(

                            text = "Tus destinos",

                            style = MaterialTheme
                                .typography
                                .titleMedium,

                            color = TextoPrincipal
                        )

                        Spacer(
                            modifier = Modifier.height(12.dp)
                        )

                        DestinosRapidos(

                            onDestinoSeleccionado = { destino ->

                                if (destino == "Cargadores") {

                                    onAbrirCargadores()

                                } else {

                                    onAbrirRuta(destino)
                                }
                            }
                        )

                        Spacer(
                            modifier = Modifier.height(24.dp)
                        )

                        DestinosRecientes(

                            onDestinoSeleccionado = { destino ->

                                onAbrirRuta(
                                    destino.nombre
                                )
                            },

                            onVerTodos = {

                                scope.launch {

                                    snackbarHostState.showSnackbar(
                                        "Mostrando todos los destinos recientes"
                                    )
                                }
                            }
                        )

                    } else {

                        val lugares = listOf(

                            "Casa" to
                                    "5 Calle, Fuentes del Valle 4",

                            "Trabajo" to
                                    "Agregar dirección",

                            "Universidad del Valle de Guatemala" to
                                    "18 Avenida 11-95, Zona 15, Guatemala",

                            "Puerta 8 UVG" to
                                    "Acceso a la Universidad del Valle de Guatemala",

                            "Campo Marte GUA022" to
                                    "3a Calle, Zona 10, Guatemala",

                            "Pollo Campero, Las Américas" to
                                    "Zona 13, Guatemala",

                            "Parque Erick Barrondo García" to
                                    "Zona 7, Guatemala"
                        )

                        val resultados = lugares.filter { lugar ->

                            lugar.first.contains(

                                textoBusqueda.trim(),

                                ignoreCase = true

                            ) || lugar.second.contains(

                                textoBusqueda.trim(),

                                ignoreCase = true
                            )
                        }

                        Text(

                            text = "Resultados de búsqueda",

                            style = MaterialTheme
                                .typography
                                .titleMedium,

                            color = TextoPrincipal
                        )

                        Spacer(
                            modifier = Modifier.height(12.dp)
                        )

                        if (resultados.isEmpty()) {

                            Text(

                                text = "No se encontraron destinos.",

                                color = TextoSecundario,

                                modifier = Modifier.padding(
                                    vertical = 16.dp
                                )
                            )

                        } else {

                            resultados.forEach { lugar ->

                                Row(

                                    modifier = Modifier

                                        .fillMaxWidth()

                                        .clickable {

                                            onAbrirRuta(
                                                lugar.first
                                            )
                                        }

                                        .padding(
                                            vertical = 12.dp
                                        ),

                                    verticalAlignment =
                                        Alignment.CenterVertically

                                ) {

                                    Icon(

                                        imageVector = Icons.Default.Place,

                                        contentDescription = null,

                                        tint = Violeta,

                                        modifier = Modifier.size(24.dp)
                                    )

                                    Spacer(
                                        modifier = Modifier.width(14.dp)
                                    )

                                    Column(

                                        modifier = Modifier.weight(1f)

                                    ) {

                                        Text(

                                            text = lugar.first,

                                            color = TextoPrincipal,

                                            fontSize = 15.sp
                                        )

                                        Text(

                                            text = lugar.second,

                                            color = TextoSecundario,

                                            fontSize = 12.sp
                                        )
                                    }
                                }

                                HorizontalDivider(
                                    color = BordeBoton
                                )
                            }
                        }
                    }
                }
            },

            snackbarHost = {

                SnackbarHost(
                    hostState = snackbarHostState
                )
            }

        ) { _ ->

            Box(
                modifier = Modifier.fillMaxSize()
            ) {

                AndroidView(

                    factory = {

                        (mapView.parent as? ViewGroup)
                            ?.removeView(mapView)

                        mapView
                    },

                    modifier = Modifier.fillMaxSize()
                )

                Row(

                    modifier = Modifier

                        .fillMaxWidth()

                        .align(Alignment.TopCenter)

                        .statusBarsPadding()

                        .padding(

                            start = 16.dp,

                            end = 16.dp,

                            top = 16.dp
                        ),

                    horizontalArrangement =
                        Arrangement.SpaceBetween,

                    verticalAlignment = Alignment.Top

                ) {

                    BotonMapa(

                        icono = Icons.Default.Menu,

                        descripcion = "Abrir menú",

                        colorIcono = TextoPrincipal,

                        circular = false,

                        onClick = {

                            scope.launch {
                                drawerState.open()
                            }
                        }
                    )

                    Column(

                        verticalArrangement =
                            Arrangement.spacedBy(12.dp)

                    ) {

                        BotonMapa(

                            icono = Icons.Default.Layers,

                            descripcion = "Cambiar estilo del mapa",

                            colorIcono = TextoPrincipal,

                            onClick = {

                                modoOscuro = !modoOscuro

                                onCapas(modoOscuro)
                            }
                        )

                        BotonMapa(

                            icono = Icons.Default.FlashOn,

                            descripcion = "Mostrar cargadores",

                            colorIcono = VerdeMenta,

                            onClick = {

                                onAbrirCargadores()
                            }
                        )

                        BotonMapa(

                            icono = Icons.Default.Navigation,

                            descripcion = "Volver a Villa Nueva",

                            colorIcono = Violeta,

                            onClick = {

                                onUbicacion()

                                scope.launch {

                                    snackbarHostState.showSnackbar(
                                        "Mapa centrado en Villa Nueva"
                                    )
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun BotonMapa(

    icono: ImageVector,

    descripcion: String,

    colorIcono: Color,

    circular: Boolean = true,

    onClick: () -> Unit

) {

    val forma = if (circular) {

        CircleShape

    } else {

        RoundedCornerShape(16.dp)
    }

    Surface(

        modifier = Modifier.size(52.dp),

        shape = forma,

        color = FondoBoton,

        border = BorderStroke(

            width = 1.dp,

            color = BordeBoton
        ),

        shadowElevation = 6.dp,

        onClick = onClick

    ) {

        Box(

            modifier = Modifier.fillMaxSize(),

            contentAlignment = Alignment.Center

        ) {

            Icon(

                imageVector = icono,

                contentDescription = descripcion,

                tint = colorIcono,

                modifier = Modifier.size(26.dp)
            )
        }
    }
}