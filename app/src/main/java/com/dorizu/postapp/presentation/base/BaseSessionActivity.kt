package com.dorizu.postapp.presentation.base

import android.content.Intent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dorizu.postapp.R
import com.dorizu.postapp.presentation.login.LoginActivity
import com.dorizu.postapp.utils.SessionManager

abstract class BaseSessionActivity : AppCompatActivity() {

    override fun onResume() {
        super.onResume()
        SessionManager.startTimer {
            handleSessionTimeout()
        }
    }

    override fun onUserInteraction() {
        super.onUserInteraction()
        SessionManager.resetTimer()
    }

    override fun onPause() {
        super.onPause()
        SessionManager.stopTimer()
    }

    private fun handleSessionTimeout() {
        Toast.makeText(this, getString(R.string.session_timeout_message), Toast.LENGTH_LONG).show()
        val intent = Intent(this, LoginActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
        finish()
    }
}