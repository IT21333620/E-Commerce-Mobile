package com.example.e_commercemobile.api

import com.example.e_commercemobile.data.model.LoggedInUser
import com.example.e_commercemobile.data.model.LoginRequest
import com.example.e_commercemobile.data.model.RegisterRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {

    @POST("api/User/login")
    fun Login(
        @Body loginRequest: LoginRequest,
    ): Call<LoggedInUser>

    @POST("api/User")
    fun Register(
        @Body registerRequest: RegisterRequest,
    ): Call<LoggedInUser>
}