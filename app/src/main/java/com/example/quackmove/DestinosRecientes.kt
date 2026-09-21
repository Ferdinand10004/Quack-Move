
package com.example.quackmove

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Colores de QuackMove
private val TextoPrincipalReciente = Color(0xFFF5F6FF)
private val TextoSecundarioReciente = Color(0xFFB6C2DF)
private val VioletaReciente = Color(0xFFB6A0FF)
private val FondoIconoReciente = Color(0xFF1B2941)
private val BordeReciente = Color(0xFF405174)

// Modelo de datos para los destinos de ejemplo
data class DestinoReciente(
    val nombre: String,
    val direccion: String
)

// Datos mock: todavía no proceden de un historial real
private val destinosDePrueba = listOf(
    DestinoReciente(
        nombre = "Universidad del Valle de Guatemala",
        direccion = "18 Avenida 11-95, Zona 15, Ciudad de Guatemala"
    ),
    DestinoReciente(
        nombre = "Puerta 8 UVG",
        direccion = "Acceso a la Universidad del Valle de Guatemala"
    ),
    DestinoReciente(
        nombre = "Campo Marte GUA022",
        direccion = "3a Calle, Zona 10, Guatemala"
    ),
    DestinoReciente(
        nombre = "Pollo Campero, Las Américas",
        direccion = "Zona 13, Guatemala"
    ),
    DestinoReciente(
        nombre = "Parque Erick Barrondo García",
        direccion = "Zona 7, Guatemala"
    )
)

@Composable
fun DestinosRecientes(
    onDestinoSeleccionado: (DestinoReciente) -> Unit,
    onVerTodos: () -> Unit
) {

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {

        // Encabezado de la sección
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),

            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Text(
                text = "Recientes",
                color = TextoPrincipalReciente,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )

            Text(
                text = "Ver todos ›",
                color = VioletaReciente,
                fontSize = 14.sp,
                modifier = Modifier
                    .clickable(onClick = onVerTodos)
                    .padding(8.dp)
            )
        }

        HorizontalDivider(
            color = BordeReciente,
            thickness = 1.dp
        )

        // Lista de destinos
        destinosDePrueba.forEach { destino ->

            FilaDestinoReciente(
                destino = destino,
                onClick = {
                    onDestinoSeleccionado(destino)
                }
            )

            HorizontalDivider(
                color = BordeReciente,
                thickness = 0.5.dp
            )
        }
    }
}

@Composable
private fun FilaDestinoReciente(
    destino: DestinoReciente,
    onClick: () -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),

        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        Surface(
            modifier = Modifier.size(42.dp),
            shape = CircleShape,
            color = FondoIconoReciente,
            border = BorderStroke(
                width = 0.5.dp,
                color = BordeReciente
            )
        ) {

            Box(
                contentAlignment = Alignment.Center
            ) {

                Icon(
                    imageVector = Icons.Default.AccessTime,
                    contentDescription = null,
                    tint = TextoPrincipalReciente,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(3.dp)
        ) {

            Text(
                text = destino.nombre,
                color = TextoPrincipalReciente,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = destino.direccion,
                color = TextoSecundarioReciente,
                fontSize = 12.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }

        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            tint = VioletaReciente,
            modifier = Modifier.size(22.dp)
        )
    }
}
