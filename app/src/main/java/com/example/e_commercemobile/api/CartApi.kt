package com.example.e_commercemobile.api

import com.example.e_commercemobile.data.model.Cart
import com.example.e_commercemobile.data.model.OrderHistory
import com.example.e_commercemobile.data.model.OrderRequest
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
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

    @POST("/api/Order")
    fun addCartItem(
        @Body orderRequest: OrderRequest
    ): Call<ResponseBody>

    @GET("/api/Order/customer/{customerId}")
    fun getOrderHistory(
        @Path("customerId") customerId: String
    ): Call<List<OrderHistory>>

    @PUT("/api/Order/cancel/{id}")
    fun cancelOrder(
        @Path("id") id: String
    ): Call<ResponseBody>

    @DELETE("/api/ProductListing/user/{userId}")
    fun clearCart(
        @Path("userId") userId: String
    ): Call<ResponseBody>


}