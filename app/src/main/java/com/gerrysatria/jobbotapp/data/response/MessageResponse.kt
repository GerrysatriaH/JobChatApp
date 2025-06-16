package com.gerrysatria.jobbotapp.data.response

import com.google.gson.annotations.SerializedName

data class MessageResponse(
    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("chat_id")
    val chatId: Int,

    @field:SerializedName("sender")
    val sender: String,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("is_file")
    val isFile: Boolean?,

    @field:SerializedName("file_name")
    val fileName: String?,

    @field:SerializedName("file_url")
    val fileUrl: String?,

    @field:SerializedName("timestamp")
    val timestamp: String
)