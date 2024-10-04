package com.example.e_commercemobile.api

import com.example.e_commercemobile.data.model.Cart
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Path

interface CartApi {
    @GET("/api/ProductListing/user/{userId}")
    fun getCartItems(
        @Path("userId") userId: String
    ): Call<List<Cart>>

    @DELETE("/api/ProductListing/{id}")
    fun deleteCartItem(
        @Path("id") id: String
    ): Call<ResponseBody>
}