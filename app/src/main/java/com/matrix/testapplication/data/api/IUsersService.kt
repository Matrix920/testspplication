package com.matrix.testapplication.data.api

import com.matrix.testapplication.data.model.User
import com.matrix.testapplication.data.model.UserResponse
import com.matrix.testapplication.data.model.UsersResponse
import retrofit2.http.*

interface IUsersService {

    @GET("users")
    suspend fun getUsers(@Header("Authorization") token: String): UsersResponse

    @GET("user/{id}")
    suspend fun getUser(@Header("Authorization") token: String, @Path("id") id: Int): UserResponse

    @FormUrlEncoded
    @POST("user/register")
    suspend fun register(@Field("name") name: String,
                         @Field("email") email: String,
                         @Field("phone") phone: String,
                         @Field("country_code") country_code: String,
                         @Field("password") password: String,
                         @Field("password_confirm") password_confirm: String): UserResponse


    @FormUrlEncoded
    @POST("login")
    suspend fun login(@Field("email") email: String,
                      @Field("password") password: String): UserResponse

//    pass the authorization with data
//    name & email & phone & country_code
    @FormUrlEncoded
    @POST("user/update")
    suspend fun update(@Header("Authorization") token: String,
                       @Field("name") name: String,
                       @Field("email") email: String,
                       @Field("phone") phone: String,
                       @Field("country_code") country_code: String): UserResponse

//    pass the authorization
    @FormUrlEncoded
    @POST("user/changepassword")
    suspend fun changePassword(@Header("Authorization") token: String,
                               @Field("password") password: String,
                               @Field("password_confirm") password_confirm: String,
                               @Field("current_password") current_password: String): UsersResponse

//    pass the authorization bearer key
    @DELETE("user/delete")
    suspend fun deleteUser(@Header("Authorization") token: String): UsersResponse
}