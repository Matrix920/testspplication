package com.matrix.testapplication.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.squareup.moshi.JsonClass
import java.util.*

@JsonClass(generateAdapter = true)
data class User(
    @SerializedName("id")
    @Expose
    val id: String,
    @SerializedName("name")
    @Expose
    val name: String,
    @SerializedName("password")
    @Expose
    val password: String?,
    @SerializedName("confirm_password")
    @Expose
    val password_confirm: String?,
    @SerializedName("country_code")
    @Expose
    val country_code: String,
    @SerializedName("phone")
    @Expose
    val phone: String,
    @SerializedName("email")
    @Expose
    val email: String,
    @SerializedName("token")
    @Expose
    val token: String?,
    @SerializedName("token_expiry")
    @Expose
    val token_expiry: String?
)

fun User.asDatabaseUser(): DbUser =
    DbUser(
        id = id.toInt(),
        name = name,
        password = password,
        email = email,
        country_code = country_code,
        phone = phone,
        token = token,
        token_expiry = token_expiry
    )


