package com.quackmove.app.feature.home

import android.view.ViewGroup
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
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
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.SheetValue
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.quackmove.app.data.repository.DemoDestinationsRepository
import kotlinx.coroutines.launch
import org.maplibre.android.maps.MapView

// Colores originales de QuackMove
private val ButtonBackground = Color(0xFF111827)
private val ButtonBorder = Color(0xFF405174)
private val MintGreen = Color(0xFF43E6B3)
private val Purple = Color(0xFFB6A0FF)
private val PrimaryText = Color(0xFFF5F6FF)
private val SecondaryText = Color(0xFFB6C2DF)
private val PanelBackground = Color(0xFF142039)

// =====================================
// PANTALLA PRINCIPAL
// =====================================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(
    mapView: MapView,
    onMapStyleChange: (Boolean) -> Unit,
    onReturnToCenter: () -> Unit,
    onOpenRegistration: () -> Unit,
    onOpenChargers: () -> Unit,
    onOpenRoute: (String) -> Unit,
    modifier: Modifier = Modifier
) {

    val scope = rememberCoroutineScope()

    val drawerState = rememberDrawerState(
        initialValue = DrawerValue.Closed
    )

    val scaffoldState = rememberBottomSheetScaffoldState()

    val snackbarHostState = remember {
        SnackbarHostState()
    }

    // ViewModel con StateFlow
    val factory = remember {
        HomeViewModelFactory(
            repository = DemoDestinationsRepository
        )
    }

    val model: HomeViewModel = viewModel(
        factory = factory
    )

    // Observar el estado de la pantalla principal
    val state by model.uiState.collectAsStateWithLifecycle()

    val searchText = state.searchText

    val searchExpanded =
        scaffoldState.bottomSheetState.currentValue ==
                SheetValue.Expanded

    // =====================================
    // MENÚ LATERAL
    // =====================================

    ModalNavigationDrawer(
        modifier = modifier,
        drawerState = drawerState,
        gesturesEnabled = false,

        drawerContent = {

            ModalDrawerSheet(
                modifier = Modifier.width(300.dp),
                drawerContainerColor = PanelBackground,
                drawerContentColor = PrimaryText
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
                        color = PrimaryText
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
                            tint = PrimaryText
                        )
                    }
                }

                HorizontalDivider(
                    color = ButtonBorder
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
                            onOpenRegistration()
                        }
                    },

                    modifier = Modifier.padding(
                        horizontal = 12.dp
                    )
                )

                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 16.dp),
                    color = ButtonBorder
                )

                Text(
                    text = "Mi vehículo, favoritos y configuración " +
                            "estarán disponibles próximamente.",
                    modifier = Modifier.padding(20.dp),
                    color = SecondaryText,
                    fontSize = 14.sp
                )
            }
        }
    ) {

        // =====================================
        // PANEL INFERIOR DE BÚSQUEDA
        // =====================================

        BottomSheetScaffold(
            scaffoldState = scaffoldState,

            sheetPeekHeight = 104.dp,

            sheetContainerColor = PanelBackground,

            sheetContentColor = PrimaryText,

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
                        .verticalScroll(rememberScrollState())
                        .padding(
                            start = 18.dp,
                            end = 18.dp,
                            top = 12.dp,
                            bottom = 20.dp
                        )
                ) {

                    // Barra para expandir el panel
                    Box(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
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

                    // =====================================
                    // BÚSQUEDA DE DESTINOS
                    // =====================================

                    DestinationSearchBar(
                        text = searchText,
                        expanded = searchExpanded,

                        onTextChange = { newText ->
                            model.updateSearchText(newText)
                        },

                        onExpand = {
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

                    // =====================================
                    // DESTINOS Y RECIENTES
                    // =====================================

                    if (searchText.isBlank()) {

                        Text(
                            text = "Tus destinos",
                            style = MaterialTheme.typography.titleMedium,
                            color = PrimaryText
                        )

                        Spacer(
                            modifier = Modifier.height(12.dp)
                        )

                        QuickDestinations(
                            onDestinationSelected = { destination ->

                                if (destination == "Cargadores") {
                                    onOpenChargers()
                                } else {
                                    onOpenRoute(destination)
                                }
                            }
                        )

                        Spacer(
                            modifier = Modifier.height(24.dp)
                        )

                        RecentDestinations(
                            destinations = state.recentDestinations,

                            onDestinationSelected = { destination ->
                                onOpenRoute(destination.name)
                            },

                            onViewAll = {
                                scope.launch {
                                    snackbarHostState.showSnackbar(
                                        "Mostrando todos los destinos recientes"
                                    )
                                }
                            }
                        )

                    } else {

                        // =====================================
                        // RESULTADOS DE BÚSQUEDA
                        // =====================================

                        val results = state.searchResults

                        Text(
                            text = "Resultados de búsqueda",
                            style = MaterialTheme.typography.titleMedium,
                            color = PrimaryText
                        )

                        Spacer(
                            modifier = Modifier.height(12.dp)
                        )

                        if (results.isEmpty()) {

                            Text(
                                text = "No se encontraron destinos.",
                                color = SecondaryText,
                                modifier = Modifier.padding(
                                    vertical = 16.dp
                                )
                            )

                        } else {

                            results.forEach { place ->

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            onOpenRoute(place.first)
                                        }
                                        .padding(vertical = 12.dp),

                                    verticalAlignment =
                                        Alignment.CenterVertically
                                ) {

                                    Icon(
                                        imageVector = Icons.Default.Place,
                                        contentDescription = null,
                                        tint = Purple,
                                        modifier = Modifier.size(24.dp)
                                    )

                                    Spacer(
                                        modifier = Modifier.width(14.dp)
                                    )

                                    Column(
                                        modifier = Modifier.weight(1f)
                                    ) {

                                        Text(
                                            text = place.first,
                                            color = PrimaryText,
                                            fontSize = 15.sp
                                        )

                                        Text(
                                            text = place.second,
                                            color = SecondaryText,
                                            fontSize = 12.sp
                                        )
                                    }
                                }

                                HorizontalDivider(
                                    color = ButtonBorder
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

            // =====================================
            // MAPA PRINCIPAL
            // =====================================

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

                // Botones flotantes del mapa
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

                    // Abrir menú lateral
                    MapActionButton(
                        icon = Icons.Default.Menu,
                        description = "Abrir menú",
                        iconColor = PrimaryText,
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

                        // =====================================
                        // CAMBIAR ESTILO DEL MAPA
                        // =====================================

                        MapActionButton(
                            icon = Icons.Default.Layers,
                            description = "Cambiar estilo del mapa",
                            iconColor = PrimaryText,

                            onClick = {
                                onMapStyleChange(
                                    model.toggleDarkMap()
                                )
                            }
                        )

                        // =====================================
                        // ABRIR CARGADORES
                        // =====================================

                        MapActionButton(
                            icon = Icons.Default.FlashOn,
                            description = "Mostrar cargadores",
                            iconColor = MintGreen,

                            onClick = {
                                onOpenChargers()
                            }
                        )

                        // =====================================
                        // CENTRAR MAPA
                        // =====================================

                        MapActionButton(
                            icon = Icons.Default.Navigation,
                            description = "Volver a Villa Nueva",
                            iconColor = Purple,

                            onClick = {

                                onReturnToCenter()

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

// =====================================
// COMPONENTE REUTILIZABLE
// BOTÓN FLOTANTE DEL MAPA
// =====================================

@Composable
fun MapActionButton(
    icon: ImageVector,
    description: String,
    iconColor: Color,
    circular: Boolean = true,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    val buttonShape = if (circular) {
        CircleShape
    } else {
        RoundedCornerShape(16.dp)
    }

    Surface(
        modifier = modifier.size(52.dp),
        shape = buttonShape,
        color = ButtonBackground,
        border = BorderStroke(
            width = 1.dp,
            color = ButtonBorder
        ),
        shadowElevation = 6.dp,
        onClick = onClick
    ) {

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {

            Icon(
                imageVector = icon,
                contentDescription = description,
                tint = iconColor,
                modifier = Modifier.size(26.dp)
            )
        }
    }
}
