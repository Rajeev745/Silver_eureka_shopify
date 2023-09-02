package com.example.e_commerceapplication.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CartProduct(
    val product: Product,
    val color: String? = null,
    val quantity: Int
) : Parcelable {
    constructor() : this(Product(), null, 1)
}