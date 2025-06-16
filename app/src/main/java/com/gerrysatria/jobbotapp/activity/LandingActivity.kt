package com.gerrysatria.jobbotapp.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.gerrysatria.jobbotapp.activity.auth.LoginActivity
import com.gerrysatria.jobbotapp.activity.auth.RegisterActivity
import com.gerrysatria.jobbotapp.activity.home.HomeActivity
import com.gerrysatria.jobbotapp.activity.home.HomeViewModel
import com.gerrysatria.jobbotapp.databinding.ActivityLandingBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class LandingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLandingBinding
    private val viewModel: HomeViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLandingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.userId.observe(this) { userId ->
            if (userId != null) {
                startActivity(Intent(this@LandingActivity, HomeActivity::class.java))
                finish()
            }
        }

        binding.btnLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        binding.btnSignUp.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}