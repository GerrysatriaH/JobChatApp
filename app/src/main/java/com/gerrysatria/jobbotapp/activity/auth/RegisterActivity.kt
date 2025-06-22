package com.gerrysatria.jobbotapp.activity.auth

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import androidx.appcompat.app.AppCompatActivity
import com.gerrysatria.jobbotapp.data.RegisterRequest
import com.gerrysatria.jobbotapp.databinding.ActivityRegisterBinding
import com.gerrysatria.jobbotapp.utils.State
import com.gerrysatria.jobbotapp.utils.show
import com.gerrysatria.jobbotapp.utils.showDialog
import com.gerrysatria.jobbotapp.R
import com.gerrysatria.jobbotapp.utils.showDialogWithAction
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val viewModel: AuthViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            btnToLogin.setOnClickListener {
                startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                finish()
            }
            btnRegister.setOnClickListener {
                val email = registerEmail.text.toString()
                val username = email.substringBefore("@")
                val password = registerPassword.text.toString()

                val emailError = if (email.isEmpty()) getString(R.string.email_empty)
                else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) getString(R.string.invalid_email)
                else null

                val passwordError = if (password.isEmpty()) getString(R.string.password_empty)
                else null

                registerEmailLayout.error = emailError
                registerPasswordLayout.error = passwordError

                var data = RegisterRequest(username, email, password)
                if (emailError == null && passwordError == null) registerUser(data)
                else finishLoadingState()
            }
        }
    }

    private fun registerUser(request: RegisterRequest) {
        viewModel.register(request).observe(this) { state ->
            when (state) {
                is State.Loading -> startLoadingState()
                is State.Success -> {
                    finishLoadingState()
                    showDialogWithAction(this, getString(R.string.success), state.data.message){
                        startActivity(Intent(this, LoginActivity::class.java))
                        finish()
                    }
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
            registerEmail.isEnabled = false
            registerPassword.isEnabled = false
            btnRegister.isEnabled = false
            btnToLogin.isEnabled = false
        }
    }

    private fun finishLoadingState(){
        binding.apply {
            progressBar.show(false)
            registerEmail.isEnabled = true
            registerPassword.isEnabled = true
            btnRegister.isEnabled = true
            btnToLogin.isEnabled = true
        }
    }
}