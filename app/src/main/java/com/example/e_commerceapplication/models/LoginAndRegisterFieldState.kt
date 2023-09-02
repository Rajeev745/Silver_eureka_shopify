package com.example.e_commerceapplication.models

import com.example.e_commerceapplication.utils.LoginAndRegisterValidation

data class LoginAndRegisterFieldState(
    val email: LoginAndRegisterValidation,
    val password: LoginAndRegisterValidation
)
