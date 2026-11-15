package com.dorizu.postapp.presentation.login

interface LoginContract {
    interface View {
        fun showLoading()
        fun hideLoading()
        fun showUsernameError(message: String)
        fun showPasswordError(message: String)
        fun clearErrors()
        fun navigateToDashboard(username: String)
    }

    interface Presenter {
        fun login(username: String, password: String)
        fun onDestroy()
    }
}
