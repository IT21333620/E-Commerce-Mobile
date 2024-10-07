package com.example.e_commercemobile.api

import com.example.e_commercemobile.data.model.AddToCartRequest
import com.example.e_commercemobile.data.model.Category
import com.example.e_commercemobile.data.model.OrderItem
import com.example.e_commercemobile.data.model.Product
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ProductApi {
    @GET("api/product")
    fun getProducts(): Call<List<Product>>

    @GET("api/product/{id}")
    fun getProductById(@Path("id") id:String): Call<Product>

    @POST("/api/ProductListing")
    fun addProduct(
        @Body addToCartRequest : AddToCartRequest
    ): Call<OrderItem>

    @GET("/api/Category")
    fun getCategories(): Call<List<Category>>

    @GET("/api/Product/search")
    fun getProductFilters(
        @Query("productName") productName: String,
        @Query("categoryId") categoryId: String
    ): Call<List<Product>>
}