package com.example.e_commerceapplication.adapter

import com.example.e_commerceapplication.models.Product

interface AdapterAddToCartClickListener {

    fun addToCartItemClickListener(itemPosition: Int, item: Product)
}