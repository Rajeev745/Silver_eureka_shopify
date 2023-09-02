package com.example.e_commerceapplication.api

import com.example.e_commerceapplication.models.Product
import retrofit2.http.GET

interface ApiService {

    @GET("products")
    suspend fun getProducts(): List<Product>

}