
package com.example.quackmove

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.EvStation
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val FondoCarga = Color(0xFF101928)
private val SuperficieCarga = Color(0xFF192940)
private val BordeCarga = Color(0xFF405174)
private val TextoCarga = Color(0xFFF5F6FF)
private val TextoSecundarioCarga = Color(0xFFB6C2DF)
private val VerdeCarga = Color(0xFF43E6B3)
private val VioletaCarga = Color(0xFFB6A0FF)

data class EstacionCarga(
    val nombre: String,
    val ubicacion: String,
    val distancia: String,
    val conector: String,
    val potencia: Int,
    val disponible: Boolean
)

private val estacionesMock = listOf(
    EstacionCarga(
        "Estación de carga A",
        "Zona 10, Guatemala",
        "5.2 km",
        "CCS2",
        50,
        true
    ),
    EstacionCarga(
        "Estación de carga B",
        "Zona 13, Guatemala",
        "8.4 km",
        "Tipo 2",
        22,
        true
    ),
    EstacionCarga(
        "Estación de carga C",
        "Villa Nueva, Guatemala",
        "3.1 km",
        "CCS2",
        150,
        false
    ),
    EstacionCarga(
        "Estación de carga D",
        "Zona 15, Guatemala",
        "12.6 km",
        "CCS2",
        100,
        true
    )
)

@Composable
fun CargadoresScreen(
    onVolver: () -> Unit = {}
) {
    var soloDisponibles by remember {
        mutableStateOf(false)
    }

    var soloCCS2 by remember {
        mutableStateOf(false)
    }

    var soloRapidos by remember {
        mutableStateOf(false)
    }

    val estacionesFiltradas = estacionesMock.filter { estacion ->
        (!soloDisponibles || estacion.disponible) &&
                (!soloCCS2 || estacion.conector == "CCS2") &&
                (!soloRapidos || estacion.potencia >= 50)
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = FondoCarga
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onVolver
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Volver al mapa",
                        tint = TextoCarga
                    )
                }

                Column {
                    Text(
                        text = "Cargadores cercanos",
                        color = TextoCarga,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    Text(
                        text = "Estaciones de carga de prueba",
                        color = TextoSecundarioCarga,
                        fontSize = 12.sp
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = soloDisponibles,
                    onClick = {
                        soloDisponibles = !soloDisponibles
                    },
                    label = {
                        Text("Disponibles")
                    }
                )

                FilterChip(
                    selected = soloCCS2,
                    onClick = {
                        soloCCS2 = !soloCCS2
                    },
                    label = {
                        Text("CCS2")
                    }
                )

                FilterChip(
                    selected = soloRapidos,
                    onClick = {
                        soloRapidos = !soloRapidos
                    },
                    label = {
                        Text("Rápidos")
                    }
                )
            }

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            Text(
                text = "${estacionesFiltradas.size} estaciones encontradas",
                color = TextoSecundarioCarga,
                fontSize = 13.sp,
                modifier = Modifier.padding(horizontal = 20.dp)
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 24.dp
                ),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(estacionesFiltradas) { estacion ->
                    TarjetaEstacion(estacion)
                }

                if (estacionesFiltradas.isEmpty()) {
                    item {
                        Text(
                            text = "No hay estaciones que coincidan con los filtros.",
                            color = TextoSecundarioCarga,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TarjetaEstacion(
    estacion: EstacionCarga
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = SuperficieCarga,
        shape = RoundedCornerShape(18.dp),
        border = BorderStroke(
            width = 1.dp,
            color = BordeCarga
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.EvStation,
                    contentDescription = null,
                    tint = VerdeCarga,
                    modifier = Modifier.size(32.dp)
                )

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = estacion.nombre,
                        color = TextoCarga,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp
                    )

                    Text(
                        text = "${estacion.ubicacion} · ${estacion.distancia}",
                        color = TextoSecundarioCarga,
                        fontSize = 12.sp
                    )
                }
            }

            HorizontalDivider(color = BordeCarga)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = estacion.conector,
                    color = VioletaCarga
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Bolt,
                        contentDescription = null,
                        tint = VerdeCarga,
                        modifier = Modifier.size(18.dp)
                    )

                    Text(
                        text = "${estacion.potencia} kW",
                        color = VerdeCarga
                    )
                }

                Text(
                    text = if (estacion.disponible) {
                        "Disponible"
                    } else {
                        "Ocupado"
                    },
                    color = if (estacion.disponible) {
                        VerdeCarga
                    } else {
                        Color(0xFFFFB86B)
                    },
                    fontSize = 12.sp
                )
            }
        }
    }
}