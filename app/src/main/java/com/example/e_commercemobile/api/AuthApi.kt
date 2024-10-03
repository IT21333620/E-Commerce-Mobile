package com.example.e_commercemobile.api

import com.example.e_commercemobile.data.model.LoggedInUser
import com.example.e_commercemobile.data.model.LoginRequest
import com.example.e_commercemobile.data.model.RegisterRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface AuthApi {

    @POST("api/User/login")
    fun Login(
        @Body loginRequest: LoginRequest,
    ): Call<LoggedInUser>

    @POST("api/User")
    fun Register(
        @Body registerRequest: RegisterRequest,
    ): Call<LoggedInUser>

    @GET("api/User/{id}")
    fun getUserById(
        @Path("id") id: String
    ): Call<LoggedInUser>

    @PUT("api/User/{id}")
    fun updateUser(
        @Path("id") id: String,
        @Body user: LoggedInUser
    ): Call<LoggedInUser>

    @PUT("api/User/status/{id}")
    fun deactivateUser(
        @Path("id") id: String
    ): Call<String>
}