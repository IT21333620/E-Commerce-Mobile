package com.example.e_commercemobile.data.model

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
data class LoggedInUser(
    val id: String,
    val name: String,
    val email: String,
    val role: String,
    val active_status: Boolean
)