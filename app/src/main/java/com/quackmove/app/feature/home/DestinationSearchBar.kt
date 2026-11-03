package com.quackmove.app.feature.home

import androidx.compose.foundation.BorderStroke
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
private val SearchBackground = Color(0xFF202D47)
private val PrimaryText = Color(0xFFF5F6FF)
private val SecondaryText = Color(0xFFB6C2DF)
private val Purple = Color(0xFFB6A0FF)
private val SearchBorder = Color(0xFF405174)

@Composable
fun DestinationSearchBar(
    text: String,
    expanded: Boolean,
    onTextChange: (String) -> Unit,
    onExpand: () -> Unit,
    modifier: Modifier = Modifier
) {

    if (!expanded) {

        // ESTADO COMPACTO
        // Se muestra una barra visual que no abre
        // el teclado al recibir el primer toque.

        Surface(
            modifier = modifier
                .fillMaxWidth()
                .height(56.dp),

            shape = RoundedCornerShape(28.dp),

            color = SearchBackground,

            border = BorderStroke(
                width = 1.dp,
                color = SearchBorder
            ),

            onClick = onExpand
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
                    tint = PrimaryText
                )

                Text(
                    text = text.ifEmpty {
                        "¿A dónde vas?"
                    },
                    color = if (text.isEmpty()) {
                        SecondaryText
                    } else {
                        PrimaryText
                    },
                    fontSize = 17.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )

                Icon(
                    imageVector = Icons.Default.Mic,
                    contentDescription = "Búsqueda por voz",
                    tint = PrimaryText
                )
            }
        }

    } else {

        // ESTADO EXPANDIDO
        // El campo es editable y el teclado
        // aparece cuando el usuario lo toca.

        OutlinedTextField(
            value = text,

            onValueChange = onTextChange,

            modifier = modifier
                .fillMaxWidth()
                .height(56.dp),

            placeholder = {
                Text(
                    text = "¿A dónde vas?",
                    color = SecondaryText
                )
            },

            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Buscar destino",
                    tint = PrimaryText
                )
            },

            trailingIcon = {

                if (text.isNotEmpty()) {

                    IconButton(
                        onClick = {
                            onTextChange("")
                        }
                    ) {

                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Borrar búsqueda",
                            tint = SecondaryText
                        )
                    }

                } else {

                    Icon(
                        imageVector = Icons.Default.Mic,
                        contentDescription = "Búsqueda por voz",
                        tint = PrimaryText
                    )
                }
            },

            singleLine = true,

            shape = RoundedCornerShape(28.dp),

            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = SearchBackground,
                unfocusedContainerColor = SearchBackground,

                focusedTextColor = PrimaryText,
                unfocusedTextColor = PrimaryText,

                focusedBorderColor = Purple,
                unfocusedBorderColor = SearchBorder,

                cursorColor = Purple
            )
        )
    }
}