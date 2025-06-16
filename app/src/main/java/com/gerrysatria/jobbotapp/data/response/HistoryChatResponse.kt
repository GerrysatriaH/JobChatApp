package com.gerrysatria.jobbotapp.data.response

import com.google.gson.annotations.SerializedName

data class HistoryChatResponse(
    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("user_id")
    val userId: Int,

    @field:SerializedName("created_at")
    val createdAt: String
)

data class DeleteHistoryChatResponse(
    @field:SerializedName("message")
    val message: String
)