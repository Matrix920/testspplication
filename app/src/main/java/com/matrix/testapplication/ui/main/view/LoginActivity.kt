package com.matrix.testapplication.ui.main.view

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.lifecycle.ViewModelProvider
import com.matrix.testapplication.R
import com.matrix.testapplication.application.UsersApplication
import com.matrix.testapplication.data.model.User
import com.matrix.testapplication.databinding.ActivityLoginBinding
import com.matrix.testapplication.databinding.ActivityRegisterBinding
import com.matrix.testapplication.ui.main.viewmodel.UserViewModel
import com.matrix.testapplication.utils.TextUtil
import com.matrix.testapplication.utils.ViewUtil
import kotlin.math.log

class LoginActivity : AppCompatActivity() {

    private val viewModel: UserViewModel by lazy {
        ViewModelProvider(this, UserViewModel.UserViewModelFactory(
            (application as UsersApplication).repository
        ))
            .get(UserViewModel::class.java)

    }

    lateinit var progressBar: ProgressDialog

    lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        progressBar = ProgressDialog(this)

        viewModel.userState.observe(this) { isLogin ->
            if(isLogin) {
                goToMainActivity()
            }
        }

        viewModel.userResponse.observe(this) { userResponse ->
            progressBar.dismiss()
            userResponse?.let {
                ViewUtil.showSnackbar(binding.root, this, TextUtil.getTitle(it.success), userResponse.message, userResponse.success)
            }
        }

        binding.btnLogin.setOnClickListener { login() }
        binding.txtRegister.setOnClickListener { goToRegisterActivity() }
        binding.ivPassword.setOnClickListener{ ViewUtil.togglePassword(binding.edtPassword)}
    }

    private fun goToRegisterActivity() {
        val intent = Intent(this, RegisterActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }

    private fun goToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.putExtra("fromLogin", true)
        startActivity(intent)
        finish()
    }

    private fun login() {
        if(TextUtils.isEmpty(binding.edtEmail.text) ||
            TextUtils.isEmpty(binding.edtPassword.text)) {
            ViewUtil.showSnackbar(binding.root, this, "Error", "Please fill all fields", true)
        }  else {
            showProgress("Logging In", "Please wait...")
            viewModel.login(binding.edtEmail.text.toString(), binding.edtPassword.text.toString())
        }
    }

    fun showProgress(title: String, message: String) {
        progressBar.setTitle(title)
        progressBar.setMessage(message)
        progressBar.show()
    }
}