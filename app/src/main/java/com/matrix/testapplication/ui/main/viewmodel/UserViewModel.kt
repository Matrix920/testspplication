package com.matrix.testapplication.ui.main.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.matrix.testapplication.data.model.User
import com.matrix.testapplication.data.model.UserResponse
import com.matrix.testapplication.data.model.UsersResponse
import com.matrix.testapplication.data.repository.UserRepository
import kotlinx.coroutines.launch
import java.io.IOException

val TAG = "UserViewModel"
class UserViewModel(val repository: UserRepository): ViewModel() {

    val userState = repository.isLogin
    val user = repository.user
    val userData = repository.userData
    val userResponse = MutableLiveData<UserResponse>()
    val usersResponse = MutableLiveData<UsersResponse>()
    val users = MutableLiveData<List<User>>()

    init {
//        refreshState()
//        refreshUser()
    }

    fun refreshState() {
        viewModelScope.launch {
            try {
                repository.refreshState()
            }catch (exception: IOException) {

            }
        }
    }

    fun refreshUser() {
        viewModelScope.launch {
            try {
                repository.refreshUser()
            } catch (exception: Exception) {
                Log.e(TAG, exception.localizedMessage)
            }
        }
    }

    fun register(user: User) {
        viewModelScope.launch {
            try {
                val response = repository.register(user)
                userResponse.postValue(response)
            } catch (exp: Exception) {
                Log.e(TAG, exp.localizedMessage)
                userResponse.postValue(
                    UserResponse(
                    false,
                    exp.message.toString(),
                    null
                )
                )
            }
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                val response = repository.login(email, password)
                userResponse.postValue(response)
                refreshState()
                refreshUser()
            } catch (networkError: Exception) {
                userResponse.postValue(
                    UserResponse(
                        false,
                        networkError.message.toString(),
                        null
                    )
                )
            }
        }
    }

    fun updateUserInformation(user: User) {
        viewModelScope.launch {
            try {
                val response = repository.updateUser(user)
                userResponse.postValue(response)
            }catch (exp: Exception) {
                Log.e(TAG, exp.localizedMessage)
                userResponse.postValue(
                    UserResponse(
                        false,
                        exp.localizedMessage,
                        null
                    )
                )
            }
        }
    }

    fun changePassword(password: String, currentPassword: String, confirmPassword: String) {
        viewModelScope.launch {
            try{
                val response  = repository.changePassword(password, confirmPassword, currentPassword)
                usersResponse.postValue(response)
            }catch (exp: Exception) {
                Log.e(TAG, exp.localizedMessage)
                usersResponse.postValue(UsersResponse(
                    null,
                    exp.localizedMessage,
                    false
                ))
            }
        }
    }

    fun deleteUser() {
        viewModelScope.launch {
            try {
                val response = repository.deleteUser()
                usersResponse.postValue(response)
            } catch (exp: Exception) {
                usersResponse.postValue(
                    UsersResponse(
                        null,
                        exp.localizedMessage,
                        false
                    )
                )
            }
        }
    }

    fun getUsers() {
        viewModelScope.launch {
            try {
                val response = repository.getUsers()
                response.data?.let {
                    users.postValue(it)
                }
                usersResponse.postValue(response)
            } catch (exp: java.lang.Exception) {
                Log.e(TAG, exp.localizedMessage)
                usersResponse.postValue(
                    UsersResponse(
                        null,
                        exp.message.toString(),
                        false
                    )
                )
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            try {
                repository.logout()
            } catch (exp: Exception ) {

            }
        }
    }

    class UserViewModelFactory(private val repository: UserRepository): ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if(modelClass.isAssignableFrom(UserViewModel::class.java)) {
                @Suppress("UNCHECKED_CASE")
                return UserViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel Class")
        }
    }
}