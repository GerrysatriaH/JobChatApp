package com.gerrysatria.jobbotapp.activity.auth

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import androidx.appcompat.app.AppCompatActivity
import com.gerrysatria.jobbotapp.databinding.ActivityForgotPasswordBinding
import com.gerrysatria.jobbotapp.R
import com.gerrysatria.jobbotapp.data.ForgotPasswordRequest
import com.gerrysatria.jobbotapp.utils.State
import com.gerrysatria.jobbotapp.utils.show
import com.gerrysatria.jobbotapp.utils.showDialog
import com.gerrysatria.jobbotapp.utils.showDialogWithAction
import org.koin.androidx.viewmodel.ext.android.viewModel

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityForgotPasswordBinding
    private val viewModel: AuthViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            btnResetPassword.setOnClickListener {
                val email = forgotEmail.text.toString()
                val password = forgotPassword.text.toString()

                val emailError = if (email.isEmpty()) getString(R.string.email_empty)
                else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) getString(R.string.invalid_email)
                else null

                val passwordError = if (password.isEmpty()) getString(R.string.password_empty)
                else null

                forgotEmailLayout.error = emailError
                forgotPasswordLayout.error = passwordError

                var data = ForgotPasswordRequest(email, password)
                if (emailError == null && passwordError == null) forgotPassword(data)
                else finishLoadingState()
            }
        }
    }

    private fun forgotPassword(request: ForgotPasswordRequest) {
        viewModel.forgotPassword(request).observe(this) { state ->
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
            forgotEmail.isEnabled = false
            forgotPassword.isEnabled = false
            btnResetPassword.isEnabled = false
        }
    }

    private fun finishLoadingState(){
        binding.apply {
            progressBar.show(false)
            forgotEmail.isEnabled = true
            forgotPassword.isEnabled = true
            btnResetPassword.isEnabled = true
        }
    }
}