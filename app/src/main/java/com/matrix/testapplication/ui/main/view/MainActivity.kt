package com.matrix.testapplication.ui.main.view

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import com.matrix.testapplication.R
import com.matrix.testapplication.application.UsersApplication
import com.matrix.testapplication.data.model.DbUser
import com.matrix.testapplication.databinding.ActivityMainBinding
import com.matrix.testapplication.ui.main.viewmodel.UserViewModel
import com.matrix.testapplication.utils.TextUtil
import com.matrix.testapplication.utils.ViewUtil

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    private val viewModel: UserViewModel by lazy {
        ViewModelProvider(this, UserViewModel.UserViewModelFactory(
            (application as UsersApplication).repository
        ))
            .get(UserViewModel::class.java)
    }

    lateinit var progressBar: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        viewModel.refreshState()

        val toolbar: Toolbar = binding.toolbar.root
        setSupportActionBar(toolbar)
        supportActionBar?.title = "              Home Page"

        progressBar = ProgressDialog(this)

        viewModel.userState.observe(this) { isLogin ->
            if(! isLogin) {
                goToWelcomeActivity()
            }
        }

        viewModel.userData.observe(this) { user ->
            user?.let {
                updateUserInformation(user)
            }
        }

        viewModel.usersResponse.observe(this) {response ->
            progressBar.dismiss()
            ViewUtil.showSnackbar(view, this, TextUtil.getTitle(response.success), response.message, response.success)
        }

        binding.btnChangePassword.setOnClickListener { goToChangePasswordActivity() }
        binding.btnUpdateInformation.setOnClickListener { goToUpdateInformationActivity() }
        binding.btnDeleteAccount.setOnClickListener { showDeleteDialog() }
        binding.btnLogout.setOnClickListener { logout() }

        viewModel.refreshUser()
    }

    private fun logout() {
        viewModel.logout()
    }

    private fun showDeleteDialog() {
        val deleteDialog = DeleteDialog(this, android.R.style.Theme_Dialog)
        deleteDialog.setCancelable(true)
        deleteDialog.setCanceledOnTouchOutside(true)
        deleteDialog.addOnButtonsClickListener(object : DeleteDialog.OnDialogButtonClick {
            override fun onYesClick() {
                deleteDialog.dismiss()
                showProgress("Deleting", "Please Wait...")
                viewModel.deleteUser()
            }

            override fun onNoClick() {
                deleteDialog.dismiss()
            }
        })

        deleteDialog.show()
    }

    private fun goToUpdateInformationActivity() {
        val intent = Intent(this, UpdateInformationActivity::class.java)
        startActivity(intent)
    }

    private fun goToChangePasswordActivity() {
        val intent = Intent(this, ChangePasswordActivity::class.java)
        startActivity(intent)
    }

    private fun updateUserInformation(user: DbUser) {
        binding.txtName.text = user.name
        binding.txtEmail.text = user.email
        binding.txtPhone.text = user.country_code + " " +user.phone
    }

    private fun goToWelcomeActivity() {
        val intent = Intent(this, WelcomeActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }

    fun showProgress(title: String, message: String) {
        progressBar.setTitle(title)
        progressBar.setMessage(message)
        progressBar.show()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
        finish()
    }
}