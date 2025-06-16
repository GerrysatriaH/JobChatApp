package com.gerrysatria.jobbotapp.data.response

import com.google.gson.annotations.SerializedName

data class UserResponse (

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("user")
    val user: User
)

data class User (

    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("username")
    val username: String,

    @field:SerializedName("email")
    val email: String,

    @field:SerializedName("created_at")
    val createdAt: String? = null
)

