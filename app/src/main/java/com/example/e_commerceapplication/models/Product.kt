package com.example.e_commerceapplication.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Product(
    val id: String,
    val name: String,
    val company: String,
    val price: Int,
    val colors: List<String>,
    val image: String,
    val description: String,
    val category: String,
    val featured: Boolean = false,
    val shipping: Boolean = false,
    val liked: Boolean = false
) : Parcelable {
    constructor() : this("", "", "", 0, ArrayList<String>(), "", "", "", false, false, false)
}