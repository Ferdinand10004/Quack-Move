package com.quackmove.app.feature.registration

import androidx.lifecycle.ViewModel
import com.quackmove.app.domain.RegistrationValidator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

// =====================================
// ESTADO DEL FORMULARIO
// =====================================

data class RegistrationUiState(
    val username: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val acceptedTerms: Boolean = false,
    val showPassword: Boolean = false,
    val showConfirmation: Boolean = false,
    val registrationComplete: Boolean = false
) {

    val validUsername: Boolean
        get() = RegistrationValidator.validUsername(username)

    val validEmail: Boolean
        get() = RegistrationValidator.validEmail(email)

    val validPassword: Boolean
        get() = RegistrationValidator.validPassword(password)

    val validConfirmation: Boolean
        get() = RegistrationValidator.validConfirmation(
            password,
            confirmPassword
        )

    val validForm: Boolean
        get() = validUsername &&
                validEmail &&
                validPassword &&
                validConfirmation &&
                acceptedTerms
}

// =====================================
// VIEWMODEL
// =====================================

class RegistrationViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(
        RegistrationUiState()
    )

    val uiState = _uiState.asStateFlow()

    // Actualizar nombre de usuario.
    fun updateUsername(value: String) {
        _uiState.update {
            it.copy(
                username = value,
                registrationComplete = false
            )
        }
    }

    // Actualizar correo electrónico.
    fun updateEmail(value: String) {
        _uiState.update {
            it.copy(
                email = value,
                registrationComplete = false
            )
        }
    }

    // Actualizar contraseña.
    fun updatePassword(value: String) {
        _uiState.update {
            it.copy(
                password = value,
                registrationComplete = false
            )
        }
    }

    // Actualizar confirmación.
    fun updateConfirmPassword(value: String) {
        _uiState.update {
            it.copy(
                confirmPassword = value,
                registrationComplete = false
            )
        }
    }

    // Aceptar o rechazar términos.
    fun updateAcceptedTerms(value: Boolean) {
        _uiState.update {
            it.copy(
                acceptedTerms = value,
                registrationComplete = false
            )
        }
    }

    // Mostrar u ocultar contraseña.
    fun toggleShowPassword() {
        _uiState.update {
            it.copy(
                showPassword = !it.showPassword
            )
        }
    }

    // Mostrar u ocultar confirmación.
    fun toggleShowConfirmation() {
        _uiState.update {
            it.copy(
                showConfirmation = !it.showConfirmation
            )
        }
    }

    // Envío de demostración del formulario.
    fun submit() {
        _uiState.update { state ->
            if (state.validForm) {
                state.copy(registrationComplete = true)
            } else {
                state.copy(registrationComplete = false)
            }
        }
    }
}
