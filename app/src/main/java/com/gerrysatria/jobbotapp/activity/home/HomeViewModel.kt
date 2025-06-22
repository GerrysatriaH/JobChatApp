package com.gerrysatria.jobbotapp.activity.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.gerrysatria.jobbotapp.data.HistoryChatRequest
import com.gerrysatria.jobbotapp.data.UserSessionManager
import com.gerrysatria.jobbotapp.data.repository.ChatRepository
import kotlinx.coroutines.launch

class HomeViewModel (
    private val repository: ChatRepository,
    private val sessionManager: UserSessionManager
) : ViewModel() {
    val userId: LiveData<Int?> = sessionManager.userIdFlow.asLiveData()
    fun clearUserId() = viewModelScope.launch {
        sessionManager.clearUserId()
    }
    fun createNewChat(request: HistoryChatRequest) = repository.createHistoryChat(request)
    fun getHistoryChat(chatId: Int) = repository.getHistoryChat(chatId)
}