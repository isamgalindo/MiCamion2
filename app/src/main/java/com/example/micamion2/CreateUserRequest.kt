package com.example.micamion2

data class CreateUserRequest(
    val email: String,
    val password1: String,
    val password2: String,
    val name: String,
    val lastName: String,
    val userType: String,
    val phone: String
)