package com.example.e_commerceapplication.utils

sealed class LoginAndRegisterValidation() {
    object Success : LoginAndRegisterValidation()
    data class Failed(val message: String): LoginAndRegisterValidation()
}

