package com.example.e_commerceapplication.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Address(
    val localArea: String,
    val mobileNumber: String,
    val street: String,
    val houseNumbre: String,
    val city: String,
    val state: String,
    val country: String,
    val pinCode: String
) : Parcelable {
    constructor() : this("", "", "", "", "", "", "", "")
}