package com.matrix.testapplication.application

import android.app.Application
import com.matrix.testapplication.data.api.UserService
import com.matrix.testapplication.data.database.UsersDatabase
import com.matrix.testapplication.data.repository.UserRepository
import com.matrix.testapplication.data.sharedpref.PreferenceHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class UsersApplication: Application() {

    val applicationScope = CoroutineScope(SupervisorJob())

    val database by lazy {
        UsersDatabase.getDatabase(this, applicationScope).usersDao
    }

    val sharedPreferences by lazy {
        PreferenceHelper(this)
    }

    val network by lazy {
        UserService.usersApi
    }

    val repository by lazy {
        UserRepository(
            sharedPreferences,
            database,
            network
        )
    }
}