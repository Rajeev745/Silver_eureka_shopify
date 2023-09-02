package com.example.e_commerceapplication.models

data class User(
    val userName: String, val mobile: String, val email: String, val userImage: String? = null
) {
    constructor() : this(
        /*userName =*/"",
        /*mobile =*/ "",
        /*email =*/ "",
        /*userImage =*/ null
    )
}