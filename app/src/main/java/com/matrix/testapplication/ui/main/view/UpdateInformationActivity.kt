package com.matrix.testapplication.ui.main.view

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import com.matrix.testapplication.R
import com.matrix.testapplication.application.UsersApplication
import com.matrix.testapplication.data.model.DbUser
import com.matrix.testapplication.data.model.User
import com.matrix.testapplication.databinding.ActivityMainBinding
import com.matrix.testapplication.databinding.ActivityRegisterBinding
import com.matrix.testapplication.databinding.ActivityUpdateInformationBinding
import com.matrix.testapplication.ui.main.viewmodel.UserViewModel
import com.matrix.testapplication.utils.TextUtil
import com.matrix.testapplication.utils.ViewUtil

class UpdateInformationActivity : AppCompatActivity() {

    private val viewModel: UserViewModel by lazy {
        ViewModelProvider(this, UserViewModel.UserViewModelFactory(
            (application as UsersApplication).repository
        ))
            .get(UserViewModel::class.java)

    }

    lateinit var progressBar: ProgressDialog

    lateinit var binding : ActivityUpdateInformationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUpdateInformationBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val toolbar: Toolbar = binding.toolbar.root
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Update Information"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        progressBar = ProgressDialog(this)

        binding.btnSave.setOnClickListener { updateInformation() }

        viewModel.userData.observe(this) {user ->
            user?.let { updateUserInformation(user) }
        }
        viewModel.userResponse.observe(this) { response ->
            progressBar.dismiss()
            ViewUtil.showSnackbar(view, this, TextUtil.getTitle(response.success), response.message, response.success)
            if(response.success)
                viewModel.refreshUser()
        }
    }

    private fun updateUserInformation(user: DbUser) {
        binding.edtName.setText(user.name)
        binding.edtEmail.setText(user.email)
        binding.edtPhone.setText(user.phone)
    }

    private fun updateInformation() {
        if(TextUtils.isEmpty(binding.edtName.text) ||
            TextUtils.isEmpty(binding.edtEmail.text) ||
            TextUtils.isEmpty(binding.edtPhone.text) ) {
            ViewUtil.showSnackbar(binding.root, this, "Error", "Please fill all fields", false)
        } else {
            showProgress("Updating", "Please wait...")
            viewModel.updateUserInformation(
                User(
                    "",
                    binding.edtName.text.toString(),
                    "",
                    "",
                    "+971",
                    binding.edtPhone.text.toString(),
                    binding.edtEmail.text.toString(),
                    "",
                    ""

                )
            )
        }
    }

    fun showProgress(title: String, message: String) {
        progressBar.setTitle(title)
        progressBar.setMessage(message)
        progressBar.show()
    }
}