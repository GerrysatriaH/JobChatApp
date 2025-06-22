package com.gerrysatria.jobbotapp.activity.profile

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.gerrysatria.jobbotapp.R
import com.gerrysatria.jobbotapp.activity.LandingActivity
import com.gerrysatria.jobbotapp.activity.auth.AuthViewModel
import com.gerrysatria.jobbotapp.activity.home.HomeActivity
import com.gerrysatria.jobbotapp.utils.showDialog
import com.gerrysatria.jobbotapp.databinding.ActivityProfileBinding
import com.gerrysatria.jobbotapp.utils.State
import com.gerrysatria.jobbotapp.utils.USER_ID_KEY
import com.gerrysatria.jobbotapp.utils.dialogDeleteAction
import com.gerrysatria.jobbotapp.utils.show
import com.gerrysatria.jobbotapp.utils.showDialogWithAction
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private val viewModel: ProfileViewModel by viewModel()
    private val authViewModel: AuthViewModel by viewModel()
    private var userId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        userId = intent.getIntExtra(USER_ID_KEY, 0)
        logout()
        deleteAllChat()
        getUserData()

        binding.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun getUserData() {
        viewModel.getUserById(userId!!).observe(this) { state ->
            when (state) {
                is State.Loading -> binding.progressBar.show(true)
                is State.Success -> {
                    binding.progressBar.show(false)
                    binding.apply {
                        userName.text = state.data.username
                        userEmail.text = state.data.email
                    }
                }
                is State.Error -> {
                    binding.progressBar.show(false)
                    showDialog(this, getString(R.string.error), getString(R.string.error_get_user_data))
                }
            }
        }
    }

    private fun deleteAllChat() {
        binding.btnDeleteAllChat.setOnClickListener {
            dialogDeleteAction(this, getString(R.string.title_delete_all_chat), getString(R.string.delete_all_chat)){
                viewModel.deleteAllHistoryChats(userId!!).observe(this) { state ->
                    when(state){
                        is State.Loading -> binding.progressBar.show(true)
                        is State.Success -> {
                            binding.progressBar.show(false)
                            showDialogWithAction(this, getString(R.string.success), getString(R.string.success_delete_all_chat)){
                                startActivity(Intent(this, HomeActivity::class.java).apply {
                                    flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                })
                            }
                        }
                        is State.Error -> {
                            binding.progressBar.show(false)
                            showDialog(this, getString(R.string.error), getString(R.string.error_delete_all_chat))
                        }
                    }
                }
            }
        }
    }

    private fun logout() {
        binding.btnLogOut.setOnClickListener {
            authViewModel.clearUserId()
            val intent = Intent(this, LandingActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            }
            startActivity(intent)
        }
    }
}

