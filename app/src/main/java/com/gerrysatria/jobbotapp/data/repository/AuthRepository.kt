package com.gerrysatria.jobbotapp.data.repository

import android.content.Context
import androidx.lifecycle.liveData
import com.gerrysatria.jobbotapp.R
import com.gerrysatria.jobbotapp.data.ForgotPasswordRequest
import com.gerrysatria.jobbotapp.data.LoginRequest
import com.gerrysatria.jobbotapp.data.RegisterRequest
import com.gerrysatria.jobbotapp.data.service.ApiService
import com.gerrysatria.jobbotapp.utils.State
import org.json.JSONObject
import retrofit2.HttpException

class AuthRepository (
    private val apiService: ApiService,
    private val context: Context
) {

    fun register(request: RegisterRequest) = liveData {
        emit(State.Loading)
        try {
            val response = apiService.register(request)
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

    fun login(request: LoginRequest) = liveData {
        emit(State.Loading)
        try {
            val response = apiService.login(request)
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

    fun forgotPassword(request: ForgotPasswordRequest) = liveData {
        emit(State.Loading)
        try {
            val response = apiService.forgotPassword(request)
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

    fun getUserById(userId: Int) = liveData {
        emit(State.Loading)
        try {
            val response = apiService.getUserById(userId)
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