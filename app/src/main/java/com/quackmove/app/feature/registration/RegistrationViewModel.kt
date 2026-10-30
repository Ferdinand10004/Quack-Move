package com.quackmove.app.feature.registration

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.quackmove.app.domain.RegistrationValidator

class RegistrationViewModel : ViewModel() {
    var username by mutableStateOf("")
        private set
    var email by mutableStateOf("")
        private set
    var password by mutableStateOf("")
        private set
    var confirmPassword by mutableStateOf("")
        private set
    var acceptedTerms by mutableStateOf(false)
        private set
    var showPassword by mutableStateOf(false)
        private set
    var showConfirmation by mutableStateOf(false)
        private set
    var registrationComplete by mutableStateOf(false)
        private set

    val validUsername get() = RegistrationValidator.validUsername(username)
    val validEmail get() = RegistrationValidator.validEmail(email)
    val validPassword get() = RegistrationValidator.validPassword(password)
    val validConfirmation get() = RegistrationValidator.validConfirmation(password, confirmPassword)
    val validForm get() = validUsername && validEmail && validPassword && validConfirmation && acceptedTerms

    fun updateUsername(value: String) { username = value }
    fun updateEmail(value: String) { email = value }
    fun updatePassword(value: String) { password = value }
    fun updateConfirmPassword(value: String) { confirmPassword = value }
    fun updateAcceptedTerms(value: Boolean) { acceptedTerms = value }
    fun toggleShowPassword() { showPassword = !showPassword }
    fun toggleShowConfirmation() { showConfirmation = !showConfirmation }
    fun submit() { if (validForm) registrationComplete = true }
}
