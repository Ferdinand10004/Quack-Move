
package com.example.quackmove

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.Route
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val FondoRuta = Color(0xFF101928)
private val PanelRuta = Color(0xFF192940)
private val BordeRuta = Color(0xFF405174)
private val TextoRuta = Color(0xFFF5F6FF)
private val TextoSecundarioRuta = Color(0xFFB6C2DF)
private val VioletaRuta = Color(0xFF8066F5)
private val AzulRuta = Color(0xFF2563EB)

@Composable
fun RutasScreen(
    destino: String,
    onVolver: () -> Unit = {}
) {
    Scaffold(
        containerColor = FondoRuta,
        bottomBar = {
            Surface(
                color = FondoRuta,
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
            ) {
                Button(
                    onClick = onVolver,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                        .height(56.dp),
                    shape = RoundedCornerShape(18.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = VioletaRuta
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = null
                    )

                    Spacer(
                        modifier = Modifier.width(12.dp)
                    )

                    Text(
                        text = "Volver al mapa",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .statusBarsPadding()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            Surface(
                color = PanelRuta,
                shape = RoundedCornerShape(20.dp),
                border = BorderStroke(
                    1.dp,
                    BordeRuta
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onVolver
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Volver",
                            tint = TextoRuta
                        )
                    }

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "Tu ubicación",
                            color = TextoSecundarioRuta,
                            fontSize = 12.sp
                        )

                        Text(
                            text = destino,
                            color = TextoRuta,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFF152744),
                                FondoRuta
                            )
                        ),
                        shape = RoundedCornerShape(24.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {

                    Icon(
                        imageVector = Icons.Default.Route,
                        contentDescription = null,
                        tint = VioletaRuta,
                        modifier = Modifier.size(90.dp)
                    )

                    Spacer(
                        modifier = Modifier.height(12.dp)
                    )

                    Text(
                        text = "¡Estamos trabajando en las rutas!",
                        color = TextoRuta,
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )

                    Text(
                        text = "Próximamente podrás consultar rutas, tiempos de llegada, autonomía de tu vehículo y cargadores durante el recorrido.",
                        color = TextoSecundarioRuta,
                        fontSize = 15.sp,
                        lineHeight = 23.sp,
                        textAlign = TextAlign.Center
                    )

                    Spacer(
                        modifier = Modifier.height(16.dp)
                    )

                    Surface(
                        color = PanelRuta,
                        shape = RoundedCornerShape(50.dp),
                        border = BorderStroke(
                            1.dp,
                            VioletaRuta
                        )
                    ) {
                        Text(
                            text = "Próximamente",
                            color = VioletaRuta,
                            modifier = Modifier.padding(
                                horizontal = 22.dp,
                                vertical = 12.dp
                            ),
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}
