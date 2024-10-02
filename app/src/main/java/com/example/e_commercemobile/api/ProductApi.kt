package com.example.e_commercemobile.api

import com.example.e_commercemobile.data.model.Product
import retrofit2.Call
import retrofit2.http.GET

interface ProductApi {
    @GET("api/product")
    fun getProducts(): Call<List<Product>>
}