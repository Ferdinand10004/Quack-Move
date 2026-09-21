
package com.example.quackmove

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Search

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Colores de QuackMove
private val FondoBuscador = Color(0xFF202D47)
private val TextoPrincipal = Color(0xFFF5F6FF)
private val TextoSecundario = Color(0xFFB6C2DF)
private val Violeta = Color(0xFFB6A0FF)
private val BordeBuscador = Color(0xFF405174)

@Composable
fun BuscadorDestinos(
    texto: String,
    expandido: Boolean,
    onTextoCambiado: (String) -> Unit,
    onExpandir: () -> Unit,
    modifier: Modifier = Modifier
) {

    if (!expandido) {

        // ESTADO COMPACTO
        // Se muestra una barra visual que no abre
        // el teclado al recibir el primer toque.

        Surface(
            modifier = modifier
                .fillMaxWidth()
                .height(56.dp),

            shape = RoundedCornerShape(28.dp),

            color = FondoBuscador,

            border = BorderStroke(
                width = 1.dp,
                color = BordeBuscador
            ),

            onClick = onExpandir
        ) {

            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),

                verticalAlignment = Alignment.CenterVertically,

                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    tint = TextoPrincipal
                )

                Text(
                    text = texto.ifEmpty {
                        "¿A dónde vas?"
                    },
                    color = if (texto.isEmpty()) {
                        TextoSecundario
                    } else {
                        TextoPrincipal
                    },
                    fontSize = 17.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )

                Icon(
                    imageVector = Icons.Default.Mic,
                    contentDescription = "Búsqueda por voz",
                    tint = TextoPrincipal
                )
            }
        }

    } else {

        // ESTADO EXPANDIDO
        // El campo es editable y el teclado
        // aparece cuando el usuario lo toca.

        OutlinedTextField(
            value = texto,

            onValueChange = onTextoCambiado,

            modifier = modifier
                .fillMaxWidth()
                .height(56.dp),

            placeholder = {
                Text(
                    text = "¿A dónde vas?",
                    color = TextoSecundario
                )
            },

            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Buscar destino",
                    tint = TextoPrincipal
                )
            },

            trailingIcon = {

                if (texto.isNotEmpty()) {

                    IconButton(
                        onClick = {
                            onTextoCambiado("")
                        }
                    ) {

                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Borrar búsqueda",
                            tint = TextoSecundario
                        )
                    }

                } else {

                    Icon(
                        imageVector = Icons.Default.Mic,
                        contentDescription = "Búsqueda por voz",
                        tint = TextoPrincipal
                    )
                }
            },

            singleLine = true,

            shape = RoundedCornerShape(28.dp),

            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = FondoBuscador,
                unfocusedContainerColor = FondoBuscador,

                focusedTextColor = TextoPrincipal,
                unfocusedTextColor = TextoPrincipal,

                focusedBorderColor = Violeta,
                unfocusedBorderColor = BordeBuscador,

                cursorColor = Violeta
            )
        )
    }
}