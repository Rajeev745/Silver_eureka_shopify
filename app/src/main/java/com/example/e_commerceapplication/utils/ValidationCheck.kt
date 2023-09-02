package com.example.e_commerceapplication.utils

import com.example.e_commerceapplication.models.Address


fun validateEmail(email: String): LoginAndRegisterValidation {

    return if (email.isEmpty()) {
        LoginAndRegisterValidation.Failed("Please enter your email")
//    } else if (!Patterns.EMAIL_ADDRESS.equals(email)) {
//        LoginAndRegisterValidation.Failed("Please enter the correct email")
    } else {
        LoginAndRegisterValidation.Success
    }
}

fun validatePassword(password: String): LoginAndRegisterValidation {

    return if (password.isEmpty()) {
        LoginAndRegisterValidation.Failed("Please enter your password")
    } else if (password.length < 4) {
        LoginAndRegisterValidation.Failed("Your password must be of atleast 4 characters")
    } else {
        LoginAndRegisterValidation.Success
    }
}

fun validateAddress(address: Address): Boolean {
    return address.city.trim().isNotEmpty() &&
            address.localArea.trim().isNotEmpty() &&
            address.mobileNumber.trim().isNotEmpty() &&
            address.houseNumbre.trim().isNotEmpty() &&
            address.street.trim().isNotEmpty() &&
            address.state.trim().isNotEmpty() &&
            address.country.trim().isNotEmpty() &&
            address.pinCode.trim().isNotEmpty()
}