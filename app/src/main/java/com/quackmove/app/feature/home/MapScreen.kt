package com.quackmove.app.feature.home

import android.view.ViewGroup
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import org.maplibre.android.maps.MapView

private val ButtonBackground = Color(0xFF111827)
private val ButtonBorder = Color(0xFF405174)
private val MintGreen = Color(0xFF43E6B3)
private val Purple = Color(0xFFB6A0FF)

private val PrimaryText = Color(0xFFF5F6FF)
private val SecondaryText = Color(0xFFB6C2DF)
private val PanelBackground = Color(0xFF142039)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(

    mapView: MapView,

    onMapStyleChange: (Boolean) -> Unit,

    onReturnToCenter: () -> Unit,

    onOpenRegistration: () -> Unit,

    onOpenChargers: () -> Unit,

    onOpenRoute: (String) -> Unit

) {

    val scope = rememberCoroutineScope()

    val drawerState = rememberDrawerState(
        initialValue = DrawerValue.Closed
    )

    val scaffoldState = rememberBottomSheetScaffoldState()

    val snackbarHostState = remember {
        SnackbarHostState()
    }

    val model: HomeViewModel = viewModel()
    val searchText = model.searchText

    val searchExpanded =
        scaffoldState.bottomSheetState.currentValue ==
                SheetValue.Expanded

    ModalNavigationDrawer(

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

                    modifier = Modifier.padding(
                        vertical = 16.dp
                    ),

                    color = ButtonBorder
                )

                Text(

                    text = "Mi vehículo, favoritos y configuración estarán disponibles próximamente.",

                    modifier = Modifier.padding(20.dp),

                    color = SecondaryText,

                    fontSize = 14.sp
                )
            }
        }

    ) {

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

                    if (searchText.isBlank()) {

                        Text(

                            text = "Tus destinos",

                            style = MaterialTheme
                                .typography
                                .titleMedium,

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
                            destinations = model.recentDestinations,

                            onDestinationSelected = { destination ->

                                onOpenRoute(
                                    destination.name
                                )
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

                        val results = model.searchResults

                        Text(

                            text = "Resultados de búsqueda",

                            style = MaterialTheme
                                .typography
                                .titleMedium,

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

                                            onOpenRoute(
                                                place.first
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

                    MapActionButton(

                        icon = Icons.Default.Menu,

                        descripcion = "Abrir menú",

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

                        MapActionButton(

                            icon = Icons.Default.Layers,

                            descripcion = "Cambiar estilo del mapa",

                            iconColor = PrimaryText,

                            onClick = {

                                model.toggleDarkMap()

                                onMapStyleChange(model.darkMap)
                            }
                        )

                        MapActionButton(

                            icon = Icons.Default.FlashOn,

                            descripcion = "Mostrar cargadores",

                            iconColor = MintGreen,

                            onClick = {

                                onOpenChargers()
                            }
                        )

                        MapActionButton(

                            icon = Icons.Default.Navigation,

                            descripcion = "Volver a Villa Nueva",

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

@Composable
fun MapActionButton(

    icon: ImageVector,

    descripcion: String,

    iconColor: Color,

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

                contentDescription = descripcion,

                tint = iconColor,

                modifier = Modifier.size(26.dp)
            )
        }
    }
}