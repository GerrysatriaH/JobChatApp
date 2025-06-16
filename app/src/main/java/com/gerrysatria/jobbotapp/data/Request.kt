package com.gerrysatria.jobbotapp.data

import com.google.gson.annotations.SerializedName

data class RegisterRequest(
    @SerializedName("username")
    val username: String,

    @SerializedName("email")
    val email: String,

    @SerializedName("password")
    val password: String
)

data class LoginRequest(
    @SerializedName("email")
    val email: String,

    @SerializedName("password")
    val password: String
)

data class ForgotPasswordRequest(
    @SerializedName("email")
    val email: String,

    @SerializedName("new_password")
    val newPassword: String
)

data class MessageChatRequest(
    @SerializedName("chat_id")
    val chatId: Int,

    @SerializedName("sender")
    val sender: String,

    @SerializedName("message")
    val message: String,

    @SerializedName("is_file")
    val isFile: Boolean? = false,

    @SerializedName("file_name")
    val fileName: String? = null,

    @SerializedName("file_url")
    val fileUrl: String? = null
)

data class HistoryChatRequest(
    @SerializedName("user_id")
    val userId: Int
)