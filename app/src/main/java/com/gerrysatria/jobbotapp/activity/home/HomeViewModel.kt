package com.gerrysatria.jobbotapp.activity.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.gerrysatria.jobbotapp.data.HistoryChatRequest
import com.gerrysatria.jobbotapp.data.UserSessionManager
import com.gerrysatria.jobbotapp.data.repository.ChatRepository

class HomeViewModel (
    private val repository: ChatRepository,
    sessionManager: UserSessionManager
) : ViewModel() {
    val userId: LiveData<Int?> = sessionManager.userIdFlow.asLiveData()
    fun createNewChat(request: HistoryChatRequest) = repository.createHistoryChat(request)
    fun getHistoryChat(chatId: Int) = repository.getHistoryChat(chatId)
}