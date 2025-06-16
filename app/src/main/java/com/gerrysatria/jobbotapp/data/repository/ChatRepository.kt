package com.gerrysatria.jobbotapp.data.repository

import android.content.Context
import androidx.lifecycle.liveData
import com.gerrysatria.jobbotapp.R
import com.gerrysatria.jobbotapp.data.HistoryChatRequest
import com.gerrysatria.jobbotapp.data.MessageChatRequest
import com.gerrysatria.jobbotapp.data.service.ApiService
import com.gerrysatria.jobbotapp.utils.State
import org.json.JSONObject
import retrofit2.HttpException

class ChatRepository (
    private val apiService: ApiService,
    private val context: Context
) {

    fun sentMessageUser(request: MessageChatRequest) = liveData {
        emit(State.Loading)
        try {
            val response = apiService.sendMessageChat(request)
            emit(State.Success(response))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorMessage = try { JSONObject(errorBody ?: "").getString("detail") }
                catch (_: Exception) { context.getString(R.string.error_api) }
            emit(State.Error(errorMessage))
        } catch (_: Exception) {
            emit(State.Error(context.getString(R.string.error_api)))
        }
    }

    fun createHistoryChat(request: HistoryChatRequest) = liveData {
        emit(State.Loading)
        try {
            val response = apiService.createHistoryChat(request)
            emit(State.Success(response))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorMessage = try { JSONObject(errorBody ?: "").getString("detail") }
                catch (_: Exception) { context.getString(R.string.error_api) }
            emit(State.Error(errorMessage))
        } catch (_: Exception) {
            emit(State.Error(context.getString(R.string.error_api)))
        }
    }

    fun getHistoryChat(chatId: Int) = liveData {
        emit(State.Loading)
        try {
            val response = apiService.getHistoryChat(chatId)
            emit(State.Success(response))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorMessage = try { JSONObject(errorBody ?: "").getString("detail") }
                catch (_: Exception) { context.getString(R.string.error_api) }
            emit(State.Error(errorMessage))
        } catch (_: Exception) {
            emit(State.Error(context.getString(R.string.error_api)))
        }
    }

    fun deleteAllHistoryChats(userId: Int) = liveData {
        emit(State.Loading)
        try {
            val response = apiService.deleteAllHistoryChats(userId)
            emit(State.Success(response))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorMessage = try { JSONObject(errorBody ?: "").getString("detail") }
                catch (_: Exception) { context.getString(R.string.error_api) }
            emit(State.Error(errorMessage))
        } catch (_: Exception) {
            emit(State.Error(context.getString(R.string.error_api)))
        }
    }

    fun deleteHistoryChat(chatId: Int) = liveData {
        emit(State.Loading)
        try {
            val response = apiService.deleteMessagesByChatId(chatId)
            emit(State.Success(response))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorMessage = try { JSONObject(errorBody ?: "").getString("detail") }
                catch (_: Exception) { context.getString(R.string.error_api) }
            emit(State.Error(errorMessage))
        } catch (_: Exception) {
            emit(State.Error(context.getString(R.string.error_api)))
        }
    }

    fun getMessagesByChatId(chatId: Int) = liveData {
        emit(State.Loading)
        try {
            val response = apiService.getMessagesByChatId(chatId)
            emit(State.Success(response))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorMessage = try { JSONObject(errorBody ?: "").getString("detail") }
                catch (_: Exception) { context.getString(R.string.error_api) }
            emit(State.Error(errorMessage))
        } catch (_: Exception) {
            emit(State.Error(context.getString(R.string.error_api)))
        }
    }
}