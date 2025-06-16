package com.gerrysatria.jobbotapp.activity.chat

import androidx.lifecycle.ViewModel
import com.gerrysatria.jobbotapp.data.MessageChatRequest
import com.gerrysatria.jobbotapp.data.repository.ChatRepository
import com.gerrysatria.jobbotapp.data.repository.ModelRepository
import okhttp3.MultipartBody

class MainViewModel (
    private val repository: ModelRepository,
    private val chatRepository: ChatRepository
) : ViewModel() {
    fun getRecommendationByText(text: String, chatId: Int) = repository.getJobRecommendationByText(text, chatId)
    fun getRecommendationByImgOrPDF(file: MultipartBody.Part, chatId: Int) = repository.getJobRecommendationByImgOrPDF(file, chatId)
    fun sendMessageUser(request: MessageChatRequest) = chatRepository.sentMessageUser(request)
    fun getMessagesByChatId(chatId: Int) = chatRepository.getMessagesByChatId(chatId)
    fun deleteMessagesChat(chatId: Int) = chatRepository.deleteHistoryChat(chatId)
}