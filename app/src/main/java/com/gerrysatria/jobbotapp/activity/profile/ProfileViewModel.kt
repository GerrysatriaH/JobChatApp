package com.gerrysatria.jobbotapp.activity.profile

import androidx.lifecycle.ViewModel
import com.gerrysatria.jobbotapp.data.repository.AuthRepository
import com.gerrysatria.jobbotapp.data.repository.ChatRepository

class ProfileViewModel(
    private val authRepository: AuthRepository,
    private val chatRepository: ChatRepository
) : ViewModel() {
    fun deleteAllHistoryChats(userId: Int) = chatRepository.deleteAllHistoryChats(userId)
    fun getUserById(userId: Int) = authRepository.getUserById(userId)
}