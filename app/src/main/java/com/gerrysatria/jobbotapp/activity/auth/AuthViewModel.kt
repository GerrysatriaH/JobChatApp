package com.gerrysatria.jobbotapp.activity.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gerrysatria.jobbotapp.data.ForgotPasswordRequest
import com.gerrysatria.jobbotapp.data.LoginRequest
import com.gerrysatria.jobbotapp.data.RegisterRequest
import com.gerrysatria.jobbotapp.data.UserSessionManager
import com.gerrysatria.jobbotapp.data.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class AuthViewModel (
    private val repository: AuthRepository,
    private val sessionManager: UserSessionManager
) : ViewModel() {
    val userId: Flow<Int?> get() = sessionManager.userIdFlow
    fun register(request: RegisterRequest) = repository.register(request)
    fun login(request: LoginRequest) = repository.login(request)
    fun forgotPassword(request: ForgotPasswordRequest) = repository.forgotPassword(request)
    fun saveUserId(userId: Int) = viewModelScope.launch {
        sessionManager.saveUserId(userId)
    }
    fun clearUserId() = viewModelScope.launch {
        sessionManager.clearUserId()
    }
}