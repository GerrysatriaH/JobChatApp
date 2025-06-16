package com.gerrysatria.jobbotapp.activity.chat

import android.net.Uri

sealed class ChatItem {
    data class SendText (val message: String) : ChatItem()
    data class SendDoc (val fileUri: Uri) : ChatItem()
    data class ReceiveText (val message: String) : ChatItem()
    object BotTyping : ChatItem()
}