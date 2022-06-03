package com.matrix.testapplication.data.model

import androidx.annotation.Nullable
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "user")
data class DbUser(
    @PrimaryKey
    val id: Int,
    val name: String,
    val email : String,
    val password: String?,
    val token: String?,
    val phone: String,
    val country_code: String,
    val token_expiry: String?
)

fun DbUser.asNetworkUser() =
    User(
        id = id.toString(),
        name = name,
        password = password,
        email = email,
        country_code = country_code,
        phone = phone,
        token = token,
        token_expiry = token_expiry,
        password_confirm = password
    )
