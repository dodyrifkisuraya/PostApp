package com.dorizu.postapp.presentation.login

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LoginPresenter(
    private val view: LoginContract.View
) : LoginContract.Presenter {

    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    override fun login(username: String, password: String) {
        view.clearErrors()

        val cleanUsername = username.trim()
        val cleanPassword = password.trim()

        var isValid = true

        if (cleanUsername.isEmpty()) {
            view.showUsernameError("Username tidak boleh kosong")
            isValid = false
        }

        if (cleanPassword.isEmpty()) {
            view.showPasswordError("Password tidak boleh kosong")
            isValid = false
        }

        if (isValid) {
            scope.launch {
                view.showLoading()
                delay(600)
                view.hideLoading()
                view.navigateToDashboard(cleanUsername)
            }
        }
    }

    override fun onDestroy() {
        scope.cancel()
    }
}
