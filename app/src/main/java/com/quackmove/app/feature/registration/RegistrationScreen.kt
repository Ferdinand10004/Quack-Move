package com.quackmove.app.feature.registration

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

// =====================================
// COLORES DE QUACKMOVE
// =====================================

private val RegistrationBackground = Color(0xFF101928)
private val RegistrationText = Color(0xFFF5F6FF)
private val RegistrationSecondaryText = Color(0xFFB6C2DF)
private val RegistrationPurple = Color(0xFF8066F5)
private val RegistrationGreen = Color(0xFF43E6B3)

// =====================================
// PANTALLA DE REGISTRO
// =====================================

@Composable
fun RegistrationScreen(
    onBack: () -> Unit = {},
    modifier: Modifier = Modifier
) {

    val model: RegistrationViewModel = viewModel()

    // Observar StateFlow del ViewModel.
    val state by model.uiState.collectAsStateWithLifecycle()

    Surface(
        modifier = modifier.fillMaxSize(),
        color = RegistrationBackground
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(
                    horizontal = 24.dp,
                    vertical = 32.dp
                ),
            horizontalAlignment =
                Alignment.CenterHorizontally
        ) {

            // =====================================
            // NAVEGACIÓN
            // =====================================

            TextButton(
                onClick = onBack,
                modifier = Modifier.align(Alignment.Start)
            ) {

                Text(
                    text = "← Volver",
                    color = RegistrationPurple
                )
            }

            Spacer(
                modifier = Modifier.height(24.dp)
            )

            // =====================================
            // ENCABEZADO
            // =====================================

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

            Spacer(
                modifier = Modifier.height(32.dp)
            )

            // =====================================
            // NOMBRE DE USUARIO
            // =====================================

            OutlinedTextField(
                value = state.username,
                onValueChange = model::updateUsername,
                label = {
                    Text("Nombre de usuario")
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,

                isError = state.username.isNotEmpty() &&
                        !state.validUsername,

                supportingText = {

                    if (
                        state.username.isNotEmpty() &&
                        !state.validUsername
                    ) {

                        Text(
                            "Debe tener al menos 3 caracteres"
                        )
                    }
                },

                shape = RoundedCornerShape(14.dp)
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            // =====================================
            // CORREO ELECTRÓNICO
            // =====================================

            OutlinedTextField(
                value = state.email,
                onValueChange = model::updateEmail,
                label = {
                    Text("Correo electrónico")
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,

                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email
                ),

                isError = state.email.isNotEmpty() &&
                        !state.validEmail,

                supportingText = {

                    if (
                        state.email.isNotEmpty() &&
                        !state.validEmail
                    ) {

                        Text(
                            "Ingresa un correo electrónico válido"
                        )
                    }
                },

                shape = RoundedCornerShape(14.dp)
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            // =====================================
            // CONTRASEÑA
            // =====================================

            OutlinedTextField(
                value = state.password,
                onValueChange = model::updatePassword,
                label = {
                    Text("Contraseña")
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,

                visualTransformation =
                    if (state.showPassword) {
                        VisualTransformation.None
                    } else {
                        PasswordVisualTransformation()
                    },

                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password
                ),

                trailingIcon = {

                    IconButton(
                        onClick = model::toggleShowPassword
                    ) {

                        Icon(
                            imageVector =
                                if (state.showPassword) {
                                    Icons.Default.VisibilityOff
                                } else {
                                    Icons.Default.Visibility
                                },

                            contentDescription =
                                "Mostrar u ocultar contraseña"
                        )
                    }
                },

                isError = state.password.isNotEmpty() &&
                        !state.validPassword,

                supportingText = {

                    if (
                        state.password.isNotEmpty() &&
                        !state.validPassword
                    ) {

                        Text(
                            "Debe tener al menos 8 caracteres"
                        )
                    }
                },

                shape = RoundedCornerShape(14.dp)
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            // =====================================
            // CONFIRMAR CONTRASEÑA
            // =====================================

            OutlinedTextField(
                value = state.confirmPassword,
                onValueChange = model::updateConfirmPassword,

                label = {
                    Text("Confirmar contraseña")
                },

                modifier = Modifier.fillMaxWidth(),
                singleLine = true,

                visualTransformation =
                    if (state.showConfirmation) {
                        VisualTransformation.None
                    } else {
                        PasswordVisualTransformation()
                    },

                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password
                ),

                trailingIcon = {

                    IconButton(
                        onClick = model::toggleShowConfirmation
                    ) {

                        Icon(
                            imageVector =
                                if (state.showConfirmation) {
                                    Icons.Default.VisibilityOff
                                } else {
                                    Icons.Default.Visibility
                                },

                            contentDescription =
                                "Mostrar u ocultar confirmación"
                        )
                    }
                },

                isError =
                    state.confirmPassword.isNotEmpty() &&
                            !state.validConfirmation,

                supportingText = {

                    if (
                        state.confirmPassword.isNotEmpty() &&
                        !state.validConfirmation
                    ) {

                        Text(
                            "Las contraseñas no coinciden"
                        )
                    }
                },

                shape = RoundedCornerShape(14.dp)
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            // =====================================
            // TÉRMINOS Y CONDICIONES
            // =====================================

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Checkbox(
                    checked = state.acceptedTerms,
                    onCheckedChange =
                        model::updateAcceptedTerms
                )

                Text(
                    text = "Acepto los términos y condiciones",
                    color = RegistrationSecondaryText,
                    fontSize = 13.sp
                )
            }

            Spacer(
                modifier = Modifier.height(24.dp)
            )

            // =====================================
            // BOTÓN CREAR CUENTA
            // =====================================

            Button(
                onClick = model::submit,
                enabled = state.validForm,

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

            // =====================================
            // CONFIRMACIÓN DEL FORMULARIO
            // =====================================

            if (state.registrationComplete) {

                Spacer(
                    modifier = Modifier.height(16.dp)
                )

                Text(
                    text = "Registro de prueba completado correctamente.",
                    color = RegistrationGreen,
                    fontSize = 14.sp
                )
            }
        }
    }
}
