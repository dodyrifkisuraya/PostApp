package com.dorizu.postapp.presentation.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.dorizu.postapp.databinding.ActivityLoginBinding
import com.dorizu.postapp.presentation.dashboard.DashboardActivity
import com.dorizu.postapp.utils.applySystemBarPadding

class LoginActivity : AppCompatActivity(), LoginContract.View {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var presenter: LoginContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.root.applySystemBarPadding()

        presenter = LoginPresenter(this)

        setupAction()
    }

    private fun setupAction() {
        binding.btnLogin.setOnClickListener {
            val username = binding.etUsername.text?.toString().orEmpty()
            val password = binding.etPassword.text?.toString().orEmpty()
            presenter.login(username, password)
        }
    }

    override fun showLoading() {
        binding.pbLoginLoading.visibility = View.VISIBLE
        binding.btnLogin.isEnabled = false
    }

    override fun hideLoading() {
        binding.pbLoginLoading.visibility = View.GONE
        binding.btnLogin.isEnabled = true
    }

    override fun showUsernameError(message: String) {
        binding.tilUsername.error = message
    }

    override fun showPasswordError(message: String) {
        binding.tilPassword.error = message
    }

    override fun clearErrors() {
        binding.tilUsername.error = null
        binding.tilPassword.error = null
    }

    override fun navigateToDashboard(username: String) {
        val intent = Intent(this, DashboardActivity::class.java).apply {
            putExtra(DashboardActivity.EXTRA_USERNAME, username)
        }
        startActivity(intent)
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestroy()
    }
}