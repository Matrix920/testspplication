package com.matrix.testapplication.ui.main.view

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.Window
import android.view.WindowManager
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.matrix.testapplication.R
import com.matrix.testapplication.application.UsersApplication
import com.matrix.testapplication.data.model.User
import com.matrix.testapplication.databinding.ActivityRegisterBinding
import com.matrix.testapplication.ui.main.viewmodel.UserViewModel
import com.matrix.testapplication.utils.TextUtil
import com.matrix.testapplication.utils.ViewUtil
import kotlinx.coroutines.delay
import java.lang.Thread.sleep

class RegisterActivity : AppCompatActivity() {

    private val viewModel: UserViewModel by lazy {
        ViewModelProvider(this, UserViewModel.UserViewModelFactory(
            (application as UsersApplication).repository
        ))
            .get(UserViewModel::class.java)

    }

    lateinit var progressBar: ProgressDialog

    lateinit var binding : ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        progressBar = ProgressDialog(this)

        viewModel.userResponse.observe(this) { userResponse ->
            progressBar.dismiss()
            userResponse?.let {
                ViewUtil.showSnackbar(binding.root, this, TextUtil.getTitle(it.success), userResponse.message, userResponse.success)
            }
        }

        binding.btnRegister.setOnClickListener { registerUser() }
        binding.txtLogin.setOnClickListener{ goToLoginActivity() }
        binding.ivPassword.setOnClickListener { ViewUtil.togglePassword(binding.edtPassword) }
        binding.ivConfirmPassword.setOnClickListener { ViewUtil.togglePassword(binding.edtConfirmPassword) }
    }

    private fun goToLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }

    private fun registerUser() {
        if(TextUtils.isEmpty(binding.edtName.text) ||
                TextUtils.isEmpty(binding.edtEmail.text) ||
                TextUtils.isEmpty(binding.edtPassword.text) ||
                TextUtils.isEmpty(binding.edtPhone.text) ||
                TextUtils.isEmpty(binding.edtConfirmPassword.text)) {
            ViewUtil.showSnackbar(binding.root, this, "Error", "Please fill all fields", false)
        } else if(! binding.edtPassword.text.toString().equals(binding.edtConfirmPassword.text.toString())) {
            ViewUtil.showSnackbar(binding.root, this, "Error", "Password not matched", false)
        } else {
            showProgress("Registering", "Please wait...")
            viewModel.register(
                User(
                    "",
                    binding.edtName.text.toString(),
                    binding.edtPassword.text.toString(),
                    binding.edtConfirmPassword.text.toString(),
                    "+971",
                    binding.edtPhone.text.toString(),
                    binding.edtEmail.text.toString(),
                    "",
                    ""

            ))
        }
    }

    fun showProgress(title: String, message: String) {
        progressBar.setTitle(title)
        progressBar.setMessage(message)
        progressBar.show()
    }
}