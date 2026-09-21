
package com.example.quackmove

import android.util.Patterns
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val FondoRegistro = Color(0xFF101928)
private val TextoRegistro = Color(0xFFF5F6FF)
private val TextoSecundarioRegistro = Color(0xFFB6C2DF)
private val VioletaRegistro = Color(0xFF8066F5)

@Composable
fun RegistroScreen(
    onVolver: () -> Unit = {}
) {
    var nombreUsuario by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var confirmarContrasena by remember { mutableStateOf("") }
    var aceptarTerminos by remember { mutableStateOf(false) }

    var mostrarContrasena by remember { mutableStateOf(false) }
    var mostrarConfirmacion by remember { mutableStateOf(false) }
    var registroCompletado by remember { mutableStateOf(false) }

    // Validaciones en tiempo real
    val nombreValido =
        nombreUsuario.trim().length >= 3

    val correoValido =
        Patterns.EMAIL_ADDRESS.matcher(correo.trim()).matches()

    val contrasenaValida =
        contrasena.length >= 8

    val confirmacionValida =
        confirmarContrasena.isNotEmpty() &&
                confirmarContrasena == contrasena

    // El botón solo se habilita si todo es válido
    val formularioValido =
        nombreValido &&
                correoValido &&
                contrasenaValida &&
                confirmacionValida &&
                aceptarTerminos

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = FondoRegistro
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            TextButton(
                onClick = onVolver,
                modifier = Modifier.align(Alignment.Start)
            ) {
                Text(
                    text = "← Volver",
                    color = VioletaRegistro
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Regístrate",
                color = TextoRegistro,
                style = MaterialTheme.typography.headlineLarge
            )

            Text(
                text = "Crea tu cuenta para comenzar.",
                color = TextoSecundarioRegistro,
                fontSize = 15.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            // NOMBRE DE USUARIO
            OutlinedTextField(
                value = nombreUsuario,
                onValueChange = { nombreUsuario = it },
                label = { Text("Nombre de usuario") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = nombreUsuario.isNotEmpty() && !nombreValido,
                supportingText = {
                    if (nombreUsuario.isNotEmpty() && !nombreValido) {
                        Text("Debe tener al menos 3 caracteres")
                    }
                },
                shape = RoundedCornerShape(14.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // CORREO ELECTRÓNICO
            OutlinedTextField(
                value = correo,
                onValueChange = { correo = it },
                label = { Text("Correo electrónico") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email
                ),
                isError = correo.isNotEmpty() && !correoValido,
                supportingText = {
                    if (correo.isNotEmpty() && !correoValido) {
                        Text("Ingresa un correo electrónico válido")
                    }
                },
                shape = RoundedCornerShape(14.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // CONTRASEÑA
            OutlinedTextField(
                value = contrasena,
                onValueChange = { contrasena = it },
                label = { Text("Contraseña") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                visualTransformation = if (mostrarContrasena) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation()
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password
                ),
                trailingIcon = {
                    IconButton(
                        onClick = {
                            mostrarContrasena = !mostrarContrasena
                        }
                    ) {
                        Icon(
                            imageVector = if (mostrarContrasena) {
                                Icons.Default.VisibilityOff
                            } else {
                                Icons.Default.Visibility
                            },
                            contentDescription = "Mostrar u ocultar contraseña"
                        )
                    }
                },
                isError = contrasena.isNotEmpty() && !contrasenaValida,
                supportingText = {
                    if (contrasena.isNotEmpty() && !contrasenaValida) {
                        Text("Debe tener al menos 8 caracteres")
                    }
                },
                shape = RoundedCornerShape(14.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // CONFIRMAR CONTRASEÑA
            OutlinedTextField(
                value = confirmarContrasena,
                onValueChange = { confirmarContrasena = it },
                label = { Text("Confirmar contraseña") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                visualTransformation = if (mostrarConfirmacion) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation()
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password
                ),
                trailingIcon = {
                    IconButton(
                        onClick = {
                            mostrarConfirmacion = !mostrarConfirmacion
                        }
                    ) {
                        Icon(
                            imageVector = if (mostrarConfirmacion) {
                                Icons.Default.VisibilityOff
                            } else {
                                Icons.Default.Visibility
                            },
                            contentDescription = "Mostrar u ocultar confirmación"
                        )
                    }
                },
                isError = confirmarContrasena.isNotEmpty() &&
                        !confirmacionValida,
                supportingText = {
                    if (confirmarContrasena.isNotEmpty() &&
                        !confirmacionValida
                    ) {
                        Text("Las contraseñas no coinciden")
                    }
                },
                shape = RoundedCornerShape(14.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // TÉRMINOS Y CONDICIONES
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = aceptarTerminos,
                    onCheckedChange = { aceptarTerminos = it }
                )

                Text(
                    text = "Acepto los términos y condiciones",
                    color = TextoSecundarioRegistro,
                    fontSize = 13.sp
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // BOTÓN CONDICIONADO
            Button(
                onClick = {
                    registroCompletado = true
                },
                enabled = formularioValido,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = VioletaRegistro
                )
            ) {
                Text(
                    text = "Crear cuenta",
                    fontSize = 16.sp
                )
            }

            if (registroCompletado) {
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Registro de prueba completado correctamente.",
                    color = Color(0xFF43E6B3),
                    fontSize = 14.sp
                )
            }
        }
    }
}