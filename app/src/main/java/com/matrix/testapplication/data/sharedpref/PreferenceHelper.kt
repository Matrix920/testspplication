package com.matrix.testapplication.data.sharedpref

import android.content.Context
import android.content.SharedPreferences
import com.matrix.testapplication.data.model.User

class PreferenceHelper(context: Context) {

    val ID = "id"
    val PASSWORD = "password"
    val NAME = "name"
    val EMAIL = "email"
    val TOKEN = "token"
    val PHONE = "phone"
    val IS_LOGIN="is_login"

    val CUSTOM_PREF_NAME = "user_data"

    val sharedPref = context.getSharedPreferences(CUSTOM_PREF_NAME, Context.MODE_PRIVATE)


    fun login(user: User) {
        with(sharedPref.edit()) {
            putBoolean(IS_LOGIN, true)
            putInt(ID, user.id.toInt())
            putString(TOKEN, "Bearer " + user.token)

            commit()
        }
    }

    fun logout() {
        with(sharedPref.edit()) {
            clear()
            commit()
        }
    }

    var isLogin: Boolean
    get() = sharedPref.getBoolean(IS_LOGIN, false)
    set(value) {
        sharedPref.edit().putBoolean(IS_LOGIN, value)
    }

    var id: Int
    get() = sharedPref.getInt(ID, 0)
    set(value) {
        sharedPref.edit().putInt(ID, value)
    }

    var token: String
    get() = sharedPref.getString(TOKEN, "")!!
    set(value) {
        sharedPref.edit().putString(TOKEN, value)
    }

//    inline fun SharedPreferences.editMe(operation:
//                                        (SharedPreferences.Editor) -> Unit) {
//        val editMe = edit()
//        operation(editMe)
//        editMe.apply()
//    }
//
//    var SharedPreferences.id
//    get() = getInt(ID, 0)
//    set(value) {
//        editMe {
//            it.putInt(ID, value)
//        }
//    }
//
//    var SharedPreferences.name
//    get() = getString(NAME, "")
//    set(value) {editMe {
//        it.putString(NAME, value)
//    }}
//
//    var SharedPreferences.email
//        get() = getString(EMAIL, "")
//        set(value) {editMe {
//            it.putString(EMAIL, value)
//        }}
//
//    var SharedPreferences.password
//        get() = getString(PASSWORD, "")
//        set(value) {editMe {
//            it.putString(PASSWORD, value)
//        }}
//
//    var SharedPreferences.token
//        get() = getString(TOKEN, "")
//        set(value) {editMe {
//            it.putString(TOKEN, value)
//        }}
//
//    var SharedPreferences.phone
//        get() = getInt(PHONE, 0)
//        set(value) {editMe {
//            it.putInt(PHONE, value)
//        }}
//
//    var SharedPreferences.isLogin
//    get() = getBoolean(IS_LOGIN, false)
//    set(value) {
//        editMe {
//            it.putBoolean(IS_LOGIN, value)
//        }
//    }

}