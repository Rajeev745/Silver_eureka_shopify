package com.example.e_commerceapplication.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.text.SimpleDateFormat
import java.util.*
import kotlin.random.Random.Default.nextLong

@Parcelize
data class Order(
    val orderStatus: String? = null,
    val productList: List<CartProduct> = emptyList(),
    val address: Address = Address(),
    val totalPrice: Float = 0f,
    val date: String = SimpleDateFormat("DD-MM-yyyy", Locale.ENGLISH).format(Date()),
    val orderId: Long = nextLong(0, 100000000)
): Parcelable {
    constructor() : this("", emptyList(), Address(), 0f)
}