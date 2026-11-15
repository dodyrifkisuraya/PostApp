package com.dorizu.postapp.presentation.dashboard

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dorizu.postapp.databinding.ActivityDashboardBinding

class DashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val username = intent.getStringExtra(EXTRA_USERNAME).orEmpty()
        binding.tvProfileUsername.text = username
    }

    companion object {
        const val EXTRA_USERNAME = "extra_username"
    }
}