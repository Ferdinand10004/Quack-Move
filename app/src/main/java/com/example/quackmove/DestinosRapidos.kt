
package com.example.quackmove

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Work
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.EvStation

import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val FondoTarjeta = Color(0xFF1B2941)
private val BordeTarjeta = Color(0xFF405174)
private val TextoBlanco = Color(0xFFF5F6FF)
private val TextoSecundario = Color(0xFFB6C2DF)
private val ColorVioleta = Color(0xFF8066F5)
private val ColorAzul = Color(0xFF38BDF8)
private val ColorMenta = Color(0xFF43E6B3)

@Composable
fun DestinosRapidos(
    onDestinoSeleccionado: (String) -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),

        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {

        TarjetaDestino(
            nombre = "Casa",
            detalle = "5 Calle",
            icono = Icons.Default.Home,
            colorIcono = ColorVioleta,
            onClick = {
                onDestinoSeleccionado("Casa")
            }
        )

        TarjetaDestino(
            nombre = "Trabajo",
            detalle = "Agregar dirección",
            icono = Icons.Default.Work,
            colorIcono = ColorVioleta,
            onClick = {
                onDestinoSeleccionado("Trabajo")
            }
        )

        TarjetaDestino(
            nombre = "UVG",
            detalle = "Universidad",
            icono = Icons.Default.School,
            colorIcono = ColorAzul,
            onClick = {
                onDestinoSeleccionado("UVG")
            }
        )

        TarjetaDestino(
            nombre = "Puerta 8",
            detalle = "Ingreso UVG",
            icono = Icons.Default.Place,
            colorIcono = ColorVioleta,
            onClick = {
                onDestinoSeleccionado("Puerta 8")
            }
        )

        TarjetaDestino(
            nombre = "Cargadores",
            detalle = "Buscar estaciones",
            icono = Icons.Default.EvStation,
            colorIcono = ColorMenta,
            onClick = {
                onDestinoSeleccionado("Cargadores")
            }
        )
    }
}

@Composable
private fun TarjetaDestino(
    nombre: String,
    detalle: String,
    icono: ImageVector,
    colorIcono: Color,
    onClick: () -> Unit
) {

    Surface(
        modifier = Modifier
            .width(84.dp)
            .height(112.dp)
            .clickable(onClick = onClick),

        shape = RoundedCornerShape(14.dp),
        color = FondoTarjeta,
        border = BorderStroke(
            width = 1.dp,
            color = BordeTarjeta
        )
    ) {

        Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.Start
        ) {

            Icon(
                imageVector = icono,
                contentDescription = null,
                tint = colorIcono,
                modifier = Modifier.size(24.dp)
            )

            Text(
                text = nombre,
                color = TextoBlanco,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = detalle,
                color = TextoSecundario,
                fontSize = 10.sp,
                lineHeight = 12.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}