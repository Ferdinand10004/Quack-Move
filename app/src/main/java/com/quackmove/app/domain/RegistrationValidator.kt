package com.quackmove.app.domain

import android.util.Patterns

/** Mantiene las mismas reglas del registro de prueba de la Entrega 3. */
object RegistrationValidator {
    fun validUsername(username: String) = username.trim().length >= 3
    fun validEmail(email: String) = Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches()
    fun validPassword(password: String) = password.length >= 8
    fun validConfirmation(password: String, confirmation: String) =
        confirmation.isNotEmpty() && confirmation == password
}
