package com.quackmove.app.domain

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RegistrationValidatorInstrumentedTest {

    // Usuario válido.
    @Test
    fun validUsernameIsAccepted() {
        assertTrue(
            RegistrationValidator.validUsername("usuario123")
        )
    }

    // Usuario demasiado corto.
    @Test
    fun shortUsernameIsRejected() {
        assertFalse(
            RegistrationValidator.validUsername("ab")
        )
    }

    // Correo electrónico válido.
    @Test
    fun validEmailIsAccepted() {
        assertTrue(
            RegistrationValidator.validEmail(
                "persona@example.com"
            )
        )
    }

    // Correo electrónico incorrecto.
    @Test
    fun invalidEmailIsRejected() {
        assertFalse(
            RegistrationValidator.validEmail(
                "correo-invalido"
            )
        )
    }

    // Contraseña demasiado corta.
    @Test
    fun shortPasswordIsRejected() {
        assertFalse(
            RegistrationValidator.validPassword("12345")
        )
    }

    // Contraseña válida.
    @Test
    fun validPasswordIsAccepted() {
        assertTrue(
            RegistrationValidator.validPassword("12345678")
        )
    }

    // Contraseñas iguales.
    @Test
    fun matchingPasswordsAreAccepted() {
        assertTrue(
            RegistrationValidator.validConfirmation(
                "password123",
                "password123"
            )
        )
    }

    // Contraseñas diferentes.
    @Test
    fun differentPasswordsAreRejected() {
        assertFalse(
            RegistrationValidator.validConfirmation(
                "password123",
                "password456"
            )
        )
    }
}
