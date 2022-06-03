package com.matrix.testapplication.utils

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.graphics.Color
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import com.matrix.testapplication.R

object ViewUtil {

    fun showSnackbar(view: View, activity: Activity, title: String, message: String, success: Boolean) {
        val snackbar = Snackbar.make(view, "", Snackbar.LENGTH_LONG)

        val customView = activity.layoutInflater.inflate(R.layout.layout_snackbar, null)
        snackbar.view.setBackgroundColor(Color.TRANSPARENT)

        val snackbarLayout = snackbar.view as Snackbar.SnackbarLayout

        val txtTitle = customView.findViewById<TextView>(R.id.txtTitle)
        txtTitle.text = title
        val txtMessage = customView.findViewById<TextView>(R.id.txtMessage)
        txtMessage.text = message
        val llSnackbar = customView.findViewById<LinearLayout>(R.id.llSnackbar)
        if(! success)
            llSnackbar.setBackgroundResource(R.drawable.rounded_rect_red)
        else
            llSnackbar.setBackgroundResource(R.drawable.rounded_rect_green)

        snackbarLayout.addView(customView, 0)
        snackbar.show()
    }

    fun togglePassword(editText: EditText) {
        val transformationMethod = editText.transformationMethod
        if(transformationMethod is PasswordTransformationMethod)
            editText.transformationMethod = HideReturnsTransformationMethod.getInstance()
        else
            editText.transformationMethod = PasswordTransformationMethod.getInstance()
    }

}