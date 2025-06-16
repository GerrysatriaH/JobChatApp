package com.gerrysatria.jobbotapp.data.service

import com.gerrysatria.jobbotapp.data.ForgotPasswordRequest
import com.gerrysatria.jobbotapp.data.HistoryChatRequest
import com.gerrysatria.jobbotapp.data.LoginRequest
import com.gerrysatria.jobbotapp.data.MessageChatRequest
import com.gerrysatria.jobbotapp.data.RegisterRequest
import com.gerrysatria.jobbotapp.data.response.DeleteHistoryChatResponse
import com.gerrysatria.jobbotapp.data.response.HistoryChatResponse
import com.gerrysatria.jobbotapp.data.response.MessageResponse
import com.gerrysatria.jobbotapp.data.response.ModelResponse
import com.gerrysatria.jobbotapp.data.response.User
import com.gerrysatria.jobbotapp.data.response.UserResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiService {

    @POST("register")
    suspend fun register(
        @Body request: RegisterRequest
    ): UserResponse

    @POST("login")
    suspend fun login(
        @Body request: LoginRequest
    ): UserResponse
    
    @POST("forgot-password")
    suspend fun forgotPassword(
        @Body request: ForgotPasswordRequest
    ): UserResponse

    @GET("users/{user_id}")
    suspend fun getUserById(
        @Path("user_id") userId: Int
    ): User

    @POST("messages_chats")
    suspend fun sendMessageChat(
        @Body request: MessageChatRequest
    ): MessageResponse

    @GET("messages_chats/{chat_id}")
    suspend fun getMessagesByChatId(
        @Path("chat_id") chatId: Int
    ): List<MessageResponse>

    @POST("history_chats")
    suspend fun createHistoryChat(
        @Body request: HistoryChatRequest
    ): HistoryChatResponse

    @GET("history_chats/user/{user_id}")
    suspend fun getHistoryChat(
        @Path("user_id") userId: Int
    ): List<HistoryChatResponse>

    @DELETE("history_chats/user/{user_id}")
    suspend fun deleteAllHistoryChats(
        @Path("user_id") userId: Int
    ): DeleteHistoryChatResponse

    @DELETE("history_chats/{chat_id}")
    suspend fun deleteMessagesByChatId(
        @Path("chat_id") chatId: Int
    ): DeleteHistoryChatResponse

    @FormUrlEncoded
    @POST("getJobRecommendationbyText")
    suspend fun getJobRecommendationByText(
        @Field("text") text: String,
        @Field("chat_id") chatId: Int
    ): ModelResponse

    @Multipart
    @POST("getJobRecommendationbyImgOrPDF")
    suspend fun getJobRecommendationByFile(
        @Part file: MultipartBody.Part,
        @Part("chat_id") chatId: RequestBody
    ): ModelResponse
}