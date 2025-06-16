package com.gerrysatria.jobbotapp.data.response

import com.google.gson.annotations.SerializedName

data class ModelResponse(
    @field:SerializedName("cv_detected")
    val cvDetected: Boolean,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("recommendation_text")
    val recommendationText: String?
)