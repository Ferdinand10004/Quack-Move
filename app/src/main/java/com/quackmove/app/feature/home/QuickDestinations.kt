
package com.quackmove.app.feature.home

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

private val CardBackground = Color(0xFF1B2941)
private val CardBorder = Color(0xFF405174)
private val WhiteText = Color(0xFFF5F6FF)
private val SecondaryText = Color(0xFFB6C2DF)
private val PurpleColor = Color(0xFF8066F5)
private val BlueColor = Color(0xFF38BDF8)
private val MintColor = Color(0xFF43E6B3)

@Composable
fun QuickDestinations(
    onDestinationSelected: (String) -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),

        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {

        DestinationCard(
            name = "Casa",
            detail = "5 Calle",
            icon = Icons.Default.Home,
            iconColor = PurpleColor,
            onClick = {
                onDestinationSelected("Casa")
            }
        )

        DestinationCard(
            name = "Trabajo",
            detail = "Agregar dirección",
            icon = Icons.Default.Work,
            iconColor = PurpleColor,
            onClick = {
                onDestinationSelected("Trabajo")
            }
        )

        DestinationCard(
            name = "UVG",
            detail = "Universidad",
            icon = Icons.Default.School,
            iconColor = BlueColor,
            onClick = {
                onDestinationSelected("UVG")
            }
        )

        DestinationCard(
            name = "Puerta 8",
            detail = "Ingreso UVG",
            icon = Icons.Default.Place,
            iconColor = PurpleColor,
            onClick = {
                onDestinationSelected("Puerta 8")
            }
        )

        DestinationCard(
            name = "Cargadores",
            detail = "Buscar estaciones",
            icon = Icons.Default.EvStation,
            iconColor = MintColor,
            onClick = {
                onDestinationSelected("Cargadores")
            }
        )
    }
}

@Composable
private fun DestinationCard(
    name: String,
    detail: String,
    icon: ImageVector,
    iconColor: Color,
    onClick: () -> Unit
) {

    Surface(
        modifier = Modifier
            .width(84.dp)
            .height(112.dp)
            .clickable(onClick = onClick),

        shape = RoundedCornerShape(14.dp),
        color = CardBackground,
        border = BorderStroke(
            width = 1.dp,
            color = CardBorder
        )
    ) {

        Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.Start
        ) {

            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(24.dp)
            )

            Text(
                text = name,
                color = WhiteText,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = detail,
                color = SecondaryText,
                fontSize = 10.sp,
                lineHeight = 12.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}