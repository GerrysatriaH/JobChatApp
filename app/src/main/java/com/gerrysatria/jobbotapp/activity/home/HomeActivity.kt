package com.gerrysatria.jobbotapp.activity.home

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.gerrysatria.jobbotapp.R
import com.gerrysatria.jobbotapp.activity.LandingActivity
import com.gerrysatria.jobbotapp.activity.profile.ProfileActivity
import com.gerrysatria.jobbotapp.activity.chat.MainActivity
import com.gerrysatria.jobbotapp.activity.profile.ProfileViewModel
import com.gerrysatria.jobbotapp.data.HistoryChatRequest
import com.gerrysatria.jobbotapp.databinding.ActivityHomeBinding
import com.gerrysatria.jobbotapp.utils.CHAT_ID_KEY
import com.gerrysatria.jobbotapp.utils.State
import com.gerrysatria.jobbotapp.utils.USER_ID_KEY
import com.gerrysatria.jobbotapp.utils.show
import com.gerrysatria.jobbotapp.utils.showDialog
import com.gerrysatria.jobbotapp.utils.showDialogWithAction
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var homeAdapter: HomeAdapter
    private val viewModel: HomeViewModel by viewModel()
    private val profileViewModel: ProfileViewModel by viewModel()
    private var userId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupRecyclerView()

        viewModel.userId.observe(this) { id ->
            userId = id
            if (userId != null) {
                getUserData()
                getHistoryChat(userId!!)
            }
        }

        binding.apply {
            fabChat.setOnClickListener {
                userId?.let {
                    createNewChat(HistoryChatRequest(it))
                } ?: run {
                    showDialog(this@HomeActivity, getString(R.string.error), getString(R.string.error_api))
                }
            }
            imageUser.setOnClickListener {
                val intent = Intent(this@HomeActivity, ProfileActivity::class.java)
                intent.putExtra(USER_ID_KEY, userId)
                startActivity(intent)
            }
        }
    }

    private fun getUserData() {
        profileViewModel.getUserById(userId!!).observe(this){ state ->
            when(state){
                is State.Loading -> binding.progressBar.show(true)
                is State.Success -> {
                    binding.progressBar.show(false)
                    binding.userText.text = state.data.username
                }
                is State.Error -> {
                    binding.progressBar.show(false)
                    showDialogWithAction(this, getString(R.string.error), getString(R.string.error_get_user_data)){
                        viewModel.clearUserId()
                        startActivity(Intent(this@HomeActivity, LandingActivity::class.java).apply {
                            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        })
                    }
                }
            }
        }
    }

    private fun setupRecyclerView() {
        binding.rvChat.apply {
            homeAdapter = HomeAdapter { selectedChat ->
                val intent = Intent(this@HomeActivity, MainActivity::class.java)
                intent.putExtra(CHAT_ID_KEY, selectedChat.id)
                startActivity(intent)
            }
            adapter = homeAdapter
            layoutManager = LinearLayoutManager(this@HomeActivity)
        }
    }

    private fun createNewChat(userId: HistoryChatRequest) {
        viewModel.createNewChat(userId).observe(this) { state ->
            when (state) {
                is State.Loading -> binding.progressBar.show(true)
                is State.Success -> {
                    binding.progressBar.show(false)
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra(CHAT_ID_KEY, state.data.id)
                    startActivity(intent)
                }
                is State.Error -> {
                    binding.progressBar.show(false)
                    showDialog(this, getString(R.string.error), "Gagal membuat chat: ${state.error}")
                }
            }
        }
    }

    private fun getHistoryChat(userId: Int){
        viewModel.getHistoryChat(userId).observe(this){ state ->
            when(state) {
                is State.Loading -> binding.progressBar.show(true)
                is State.Success -> {
                    binding.progressBar.show(false)
                    if (state.data.isEmpty()) {
                        binding.rvChat.show(false)
                        binding.emptyChat.show(true)
                    } else {
                        binding.rvChat.show(true)
                        binding.emptyChat.show(false)
                        homeAdapter.submitList(state.data)
                    }
                }
                is State.Error -> {
                    binding.progressBar.show(false)
                    showDialog(this, getString(R.string.error), getString(R.string.error_get_history_data))
                }
            }
        }
    }
}