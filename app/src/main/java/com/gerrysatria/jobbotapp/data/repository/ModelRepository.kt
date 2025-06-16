package com.gerrysatria.jobbotapp.data.repository

import android.content.Context
import androidx.lifecycle.liveData
import com.gerrysatria.jobbotapp.R
import com.gerrysatria.jobbotapp.data.service.ApiService
import com.gerrysatria.jobbotapp.utils.State
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.HttpException

class ModelRepository (
    private val apiService: ApiService,
    private val context: Context
) {

    fun getJobRecommendationByText (text: String, chatId: Int) = liveData {
        emit(State.Loading)
        try {
            val response = apiService.getJobRecommendationByText(text, chatId)
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

    fun getJobRecommendationByImgOrPDF (file: MultipartBody.Part, chatId: Int) = liveData {
        emit(State.Loading)
        try {
            val chatId = chatId.toString().toRequestBody("text/plain".toMediaType())
            val response = apiService.getJobRecommendationByFile(file, chatId)
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