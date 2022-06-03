package com.matrix.testapplication.ui.main.view

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import com.matrix.testapplication.R
import com.matrix.testapplication.application.UsersApplication
import com.matrix.testapplication.databinding.ActivityChangePasswordBinding
import com.matrix.testapplication.ui.main.viewmodel.UserViewModel
import com.matrix.testapplication.utils.TextUtil
import com.matrix.testapplication.utils.ViewUtil

class ChangePasswordActivity : AppCompatActivity() {

    private val viewModel: UserViewModel by lazy {
        ViewModelProvider(this, UserViewModel.UserViewModelFactory(
            (application as UsersApplication).repository
        ))
            .get(UserViewModel::class.java)
    }

    lateinit var binding: ActivityChangePasswordBinding

    lateinit var progressBar: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val toolbar: Toolbar = binding.toolbar.root
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Change Password"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        progressBar = ProgressDialog(this)

        binding.btnUpdate.setOnClickListener { updatePassword() }

        viewModel.usersResponse.observe(this) { response ->
            progressBar.dismiss()
            ViewUtil.showSnackbar(view, this, TextUtil.getTitle(response.success), response.message, response.success)
            if(response.success)
                finish()
        }

        binding.ivPassword.setOnClickListener{ViewUtil.togglePassword(binding.edtPassword)}
        binding.ivConfirmPassword.setOnClickListener { ViewUtil.togglePassword(binding.edtConfirmPassword) }
        binding.ivCurrentPassword.setOnClickListener { ViewUtil.togglePassword(binding.edtCurrentPassword) }
    }

    private fun updatePassword() {
        if (TextUtils.isEmpty(binding.edtPassword.text) ||
            TextUtils.isEmpty(binding.edtConfirmPassword.text) ||
            TextUtils.isEmpty(binding.edtCurrentPassword.text)
        ) {
            ViewUtil.showSnackbar(binding.root, this, "Error", "Please fill all fields", false)
        } else {
            showProgress("updating", "Please wait...")

            viewModel.changePassword(binding.edtPassword.text.toString(),
            binding.edtCurrentPassword.text.toString(), binding.edtConfirmPassword.text.toString())
        }
    }

        fun showProgress(title: String, message: String) {
            progressBar.setTitle(title)
            progressBar.setMessage(message)
            progressBar.show()
        }
}