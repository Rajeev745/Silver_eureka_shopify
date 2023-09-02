package com.example.e_commerceapplication.utils

sealed class OrderStatus(val status: String) {

    object Ordered : OrderStatus("Ordered")
    object Shipped : OrderStatus("Shipped")
    object Returned : OrderStatus("Returned")
    object Canceled : OrderStatus("Canceled")
    object Confirmed : OrderStatus("Confirmed")
    object Delievered : OrderStatus("Delievered")

}

fun getOrderStatus(status: String): OrderStatus {
    return when (status) {
        "Ordered" -> OrderStatus.Ordered
        "Shipped" -> OrderStatus.Shipped
        "Returned" -> OrderStatus.Returned
        "Canceled" -> OrderStatus.Canceled
        "Confirmed" -> OrderStatus.Confirmed
        else -> OrderStatus.Delievered
    }
}