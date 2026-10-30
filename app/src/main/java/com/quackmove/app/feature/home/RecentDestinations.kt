
package com.quackmove.app.feature.home

import com.quackmove.app.data.model.RecentDestination
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
private val RecentPrimaryText = Color(0xFFF5F6FF)
private val RecentSecondaryText = Color(0xFFB6C2DF)
private val RecentPurple = Color(0xFFB6A0FF)
private val RecentIconBackground = Color(0xFF1B2941)
private val RecentBorder = Color(0xFF405174)





@Composable
fun RecentDestinations(
    destinations: List<RecentDestination>,
    onDestinationSelected: (RecentDestination) -> Unit,
    onViewAll: () -> Unit
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
                color = RecentPrimaryText,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )

            Text(
                text = "Ver todos ›",
                color = RecentPurple,
                fontSize = 14.sp,
                modifier = Modifier
                    .clickable(onClick = onViewAll)
                    .padding(8.dp)
            )
        }

        HorizontalDivider(
            color = RecentBorder,
            thickness = 1.dp
        )

        // Lista de destinos
        destinations.forEach { destination ->

            RecentDestinationRow(
                destination = destination,
                onClick = {
                    onDestinationSelected(destination)
                }
            )

            HorizontalDivider(
                color = RecentBorder,
                thickness = 0.5.dp
            )
        }
    }
}

@Composable
private fun RecentDestinationRow(
    destination: RecentDestination,
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
            color = RecentIconBackground,
            border = BorderStroke(
                width = 0.5.dp,
                color = RecentBorder
            )
        ) {

            Box(
                contentAlignment = Alignment.Center
            ) {

                Icon(
                    imageVector = Icons.Default.AccessTime,
                    contentDescription = null,
                    tint = RecentPrimaryText,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(3.dp)
        ) {

            Text(
                text = destination.name,
                color = RecentPrimaryText,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = destination.address,
                color = RecentSecondaryText,
                fontSize = 12.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }

        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            tint = RecentPurple,
            modifier = Modifier.size(22.dp)
        )
    }
}
