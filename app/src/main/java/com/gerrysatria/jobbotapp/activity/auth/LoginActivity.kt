package com.gerrysatria.jobbotapp.activity.auth

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import androidx.appcompat.app.AppCompatActivity
import com.gerrysatria.jobbotapp.R
import com.gerrysatria.jobbotapp.activity.home.HomeActivity
import com.gerrysatria.jobbotapp.data.LoginRequest
import com.gerrysatria.jobbotapp.databinding.ActivityLoginBinding
import com.gerrysatria.jobbotapp.utils.State
import com.gerrysatria.jobbotapp.utils.show
import com.gerrysatria.jobbotapp.utils.showToast
import com.gerrysatria.jobbotapp.utils.showDialog
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel: AuthViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            txtForgotPassword.setOnClickListener {
                startActivity(Intent(this@LoginActivity, ForgotPasswordActivity::class.java))
            }
            btnToSignup.setOnClickListener {
                startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
                finish()
            }
            btnLogin.setOnClickListener {
                val email = loginEmail.text.toString().trim()
                val password = loginPassword.text.toString().trim()

                val emailError = if (email.isEmpty()) getString(R.string.email_empty)
                else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) getString(R.string.invalid_email)
                else null

                val passwordError = if (password.isEmpty()) getString(R.string.password_empty)
                else null

                loginEmailLayout.error = emailError
                loginPasswordLayout.error = passwordError

                if (emailError == null && passwordError == null) loginUser(LoginRequest(email, password))
                else finishLoadingState()
            }
        }
    }

    private fun loginUser(request: LoginRequest) {
        viewModel.login(request).observe(this) { state ->
            when (state) {
                is State.Loading -> startLoadingState()
                is State.Success -> {
                    finishLoadingState()
                    showToast(this, state.data.message)

                    val userId = state.data.user.id
                    viewModel.saveUserId(userId)

                    startActivity(Intent(this, HomeActivity::class.java))
                    finish()
                }
                is State.Error -> {
                    finishLoadingState()
                    showDialog(this, getString(R.string.error), state.error)
                }
            }
        }
    }

    private fun startLoadingState(){
        binding.apply {
            progressBar.show(true)
            loginEmail.isEnabled = false
            loginPassword.isEnabled = false
            btnLogin.isEnabled = false
            btnToSignup.isEnabled = false
        }
    }

    private fun finishLoadingState(){
        binding.apply {
            progressBar.show(false)
            loginEmail.isEnabled = true
            loginPassword.isEnabled = true
            btnLogin.isEnabled = true
            btnToSignup.isEnabled = true
        }
    }
}