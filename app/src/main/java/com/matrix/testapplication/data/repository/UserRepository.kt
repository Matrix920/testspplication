package com.matrix.testapplication.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.matrix.testapplication.data.api.IUsersService
import com.matrix.testapplication.data.api.UserService
import com.matrix.testapplication.data.database.UsersDao
import com.matrix.testapplication.data.model.*
import com.matrix.testapplication.data.sharedpref.PreferenceHelper
import kotlinx.coroutines.Dispatchers

val TAG = "UserRepository"
class UserRepository(
    val sharedPref: PreferenceHelper,
    val usersDb: UsersDao,
    val userNetwork: IUsersService) {

    val isLogin = MutableLiveData<Boolean>()
    val user: LiveData<DbUser> = usersDb.getUser(sharedPref.id)
    val userData: MutableLiveData<DbUser> = MutableLiveData()

    suspend fun register(user: User): UserResponse {
        with(Dispatchers.IO) {

            return UserService.usersApi.register(
                user.name,
                user.email,
                user.phone,
                user.country_code,
                user.password!!,
                user.password_confirm!!)
        }
    }

    suspend fun refreshState() {
        with(Dispatchers.IO) {
            val isLoginState = sharedPref.isLogin
            isLogin.postValue(isLoginState)
        }
    }

    suspend fun updateUser() {
        Log.e(TAG, sharedPref.token)
        Log.e(TAG, "id :" + sharedPref.id.toString())
        with(Dispatchers.IO) {
            val userResponse = userNetwork.getUser(sharedPref.token, sharedPref.id)
            Log.e(TAG, userResponse.data.toString())
            if (userResponse.success) {
                userResponse.data?.let {
                    updateUserDatabase(it)
                }
            }
        }
    }

    suspend fun refreshUser() {
        with(Dispatchers.IO) {
            if(sharedPref.id !=0 ) {
                val userApi = UserService.usersApi.getUser(sharedPref.token, sharedPref.id)
                if(userApi.data != null && userApi.success) {
                    userData.postValue(userApi.data.asDatabaseUser())
                    usersDb.insertUser(userApi.data.asDatabaseUser())
                } else {
                    val userDb = usersDb.getUser(sharedPref.id)
                    userDb?.let {
                        userData.postValue(it.value)
                    }
                }
            }
        }
    }

    suspend fun updateUserDatabase(user: User) {
        with(Dispatchers.IO) {
            usersDb.updateUser(user.asDatabaseUser())
        }
    }

    suspend fun saveLogin(user: User) {
        with(Dispatchers.IO) {
            usersDb.insertUser(user.asDatabaseUser())
            sharedPref.login(user)
        }
    }

    suspend fun login(email: String, password: String): UserResponse{
        with(Dispatchers.IO) {
            val response = UserService.usersApi.login(email, password)
            if(response.success) {
                response.data?.let {  saveLogin(it) }
            }
            return response
        }
    }

    suspend fun getUser(id: Int): UserResponse {
        with(Dispatchers.IO) {
            return UserService.usersApi.getUser(sharedPref.token, id)
        }
    }

    suspend fun updateUser(user: User): UserResponse {
        with(Dispatchers.IO) {
            return UserService.usersApi.update(sharedPref.token, user.name, user.email, user.phone, user.country_code)
        }
    }

    suspend fun changePassword(password: String, password_confirm: String, current_password: String): UsersResponse {
        with(Dispatchers.IO) {
            return UserService.usersApi.changePassword(sharedPref.token, password, password_confirm, current_password)
        }
    }

    suspend fun deleteUser(): UsersResponse {
        with(Dispatchers.IO) {
            val response = UserService.usersApi.deleteUser(sharedPref.token)
            response.success?.let {
                if(response.success){
                    sharedPref.logout()
                    refreshState()
                }
            }

            return response
        }
    }

    suspend fun getUsers(): UsersResponse {
        with(Dispatchers.IO) {
            return UserService.usersApi.getUsers(sharedPref.token)
        }
    }

    suspend fun logout() {
        with(Dispatchers.IO) {
            sharedPref.logout()
            refreshState()
        }
    }
}