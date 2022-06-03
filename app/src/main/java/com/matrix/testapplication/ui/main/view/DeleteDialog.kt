package com.matrix.testapplication.ui.main.view

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.view.Window
import androidx.lifecycle.ViewModelProvider
import com.matrix.testapplication.R
import com.matrix.testapplication.application.UsersApplication
import com.matrix.testapplication.databinding.DialogDeleteAccountBinding
import com.matrix.testapplication.ui.main.viewmodel.UserViewModel

class DeleteDialog(activity: Activity, style: Int): Dialog(activity, style) {

    private lateinit var binding: DialogDeleteAccountBinding

    lateinit var onDialogButtonClick: OnDialogButtonClick

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE)

        binding = DialogDeleteAccountBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.btnYes.setOnClickListener {
            onDialogButtonClick.onYesClick()
        }

        binding.btnNo.setOnClickListener {
            onDialogButtonClick.onNoClick()
        }
    }

    fun addOnButtonsClickListener(onDialogButtonClick: OnDialogButtonClick) {
        this.onDialogButtonClick = onDialogButtonClick
    }

    interface OnDialogButtonClick {
        fun onYesClick()
        fun onNoClick()
    }
}