package com.matrix.testapplication.utils

object TextUtil {
    fun getTitle(success: Boolean): String {
        return if (success)
            "Success"
        else
            "Failed"
    }
}