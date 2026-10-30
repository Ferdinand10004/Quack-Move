
package com.quackmove.app.feature.registration

import androidx.lifecycle.viewmodel.compose.viewModel
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

private val RegistrationBackground = Color(0xFF101928)
private val RegistrationText = Color(0xFFF5F6FF)
private val RegistrationSecondaryText = Color(0xFFB6C2DF)
private val RegistrationPurple = Color(0xFF8066F5)

@Composable
fun RegistrationScreen(
    onBack: () -> Unit = {}
) {
    val model: RegistrationViewModel = viewModel()
    val username = model.username
    val email = model.email
    val password = model.password
    val confirmPassword = model.confirmPassword
    val acceptedTerms = model.acceptedTerms
    val showPassword = model.showPassword
    val showConfirmation = model.showConfirmation
    val registrationComplete = model.registrationComplete
    val validUsername = model.validUsername
    val validEmail = model.validEmail
    val validPassword = model.validPassword
    val validConfirmation = model.validConfirmation
    val validForm = model.validForm

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = RegistrationBackground
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            TextButton(
                onClick = onBack,
                modifier = Modifier.align(Alignment.Start)
            ) {
                Text(
                    text = "← Volver",
                    color = RegistrationPurple
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Regístrate",
                color = RegistrationText,
                style = MaterialTheme.typography.headlineLarge
            )

            Text(
                text = "Crea tu cuenta para comenzar.",
                color = RegistrationSecondaryText,
                fontSize = 15.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            // NOMBRE DE USUARIO
            OutlinedTextField(
                value = username,
                onValueChange = { model.updateUsername(it) },
                label = { Text("Nombre de usuario") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = username.isNotEmpty() && !validUsername,
                supportingText = {
                    if (username.isNotEmpty() && !validUsername) {
                        Text("Debe tener al menos 3 caracteres")
                    }
                },
                shape = RoundedCornerShape(14.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // CORREO ELECTRÓNICO
            OutlinedTextField(
                value = email,
                onValueChange = { model.updateEmail(it) },
                label = { Text("Correo electrónico") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email
                ),
                isError = email.isNotEmpty() && !validEmail,
                supportingText = {
                    if (email.isNotEmpty() && !validEmail) {
                        Text("Ingresa un correo electrónico válido")
                    }
                },
                shape = RoundedCornerShape(14.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // CONTRASEÑA
            OutlinedTextField(
                value = password,
                onValueChange = { model.updatePassword(it) },
                label = { Text("Contraseña") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                visualTransformation = if (showPassword) {
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
                            model.toggleShowPassword()
                        }
                    ) {
                        Icon(
                            imageVector = if (showPassword) {
                                Icons.Default.VisibilityOff
                            } else {
                                Icons.Default.Visibility
                            },
                            contentDescription = "Mostrar u ocultar contraseña"
                        )
                    }
                },
                isError = password.isNotEmpty() && !validPassword,
                supportingText = {
                    if (password.isNotEmpty() && !validPassword) {
                        Text("Debe tener al menos 8 caracteres")
                    }
                },
                shape = RoundedCornerShape(14.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // CONFIRMAR CONTRASEÑA
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { model.updateConfirmPassword(it) },
                label = { Text("Confirmar contraseña") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                visualTransformation = if (showConfirmation) {
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
                            model.toggleShowConfirmation()
                        }
                    ) {
                        Icon(
                            imageVector = if (showConfirmation) {
                                Icons.Default.VisibilityOff
                            } else {
                                Icons.Default.Visibility
                            },
                            contentDescription = "Mostrar u ocultar confirmación"
                        )
                    }
                },
                isError = confirmPassword.isNotEmpty() &&
                        !validConfirmation,
                supportingText = {
                    if (confirmPassword.isNotEmpty() &&
                        !validConfirmation
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
                    checked = acceptedTerms,
                    onCheckedChange = { model.updateAcceptedTerms(it) }
                )

                Text(
                    text = "Acepto los términos y condiciones",
                    color = RegistrationSecondaryText,
                    fontSize = 13.sp
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // BOTÓN CONDICIONADO
            Button(
                onClick = {
                    model.submit()
                },
                enabled = validForm,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = RegistrationPurple
                )
            ) {
                Text(
                    text = "Crear cuenta",
                    fontSize = 16.sp
                )
            }

            if (registrationComplete) {
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