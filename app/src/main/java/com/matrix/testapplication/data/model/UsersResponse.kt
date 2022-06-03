package com.matrix.testapplication.data.model

import com.google.gson.annotations.SerializedName
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UsersResponse(
    @SerializedName("data")
    val data: List<User>?,
    @SerializedName("message")
    val message: String,
    @SerializedName("success")
    val success: Boolean
)
