package com.gerrysatria.jobbotapp.activity.chat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gerrysatria.jobbotapp.databinding.ItemReceiveChatBinding
import com.gerrysatria.jobbotapp.databinding.ItemSentChatBinding
import com.gerrysatria.jobbotapp.databinding.ItemSentDocChatBinding
import com.gerrysatria.jobbotapp.databinding.ItemTypingChatBinding

class ChatAdapter : ListAdapter<ChatItem, RecyclerView.ViewHolder>(DiffCallback) {

    companion object {
        private const val TYPE_SEND_TEXT = 1
        private const val TYPE_SEND_DOC = 2
        private const val TYPE_RECEIVE_TEXT = 3
        private const val TYPE_BOT_TYPING = 4

        val DiffCallback = object : DiffUtil.ItemCallback<ChatItem>() {
            override fun areItemsTheSame(oldItem: ChatItem, newItem: ChatItem): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: ChatItem, newItem: ChatItem): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is ChatItem.SendText -> TYPE_SEND_TEXT
            is ChatItem.SendDoc -> TYPE_SEND_DOC
            is ChatItem.ReceiveText -> TYPE_RECEIVE_TEXT
            is ChatItem.BotTyping -> TYPE_BOT_TYPING
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            TYPE_SEND_TEXT -> {
                val view = ItemSentChatBinding.inflate(inflater, parent, false)
                SendTextViewHolder(view)
            }
            TYPE_SEND_DOC -> {
                val view = ItemSentDocChatBinding.inflate(inflater, parent, false)
                SendDocViewHolder(view)
            }
            TYPE_RECEIVE_TEXT -> {
                val view = ItemReceiveChatBinding.inflate(inflater, parent, false)
                ReceiveTextViewHolder(view)
            }
            TYPE_BOT_TYPING -> {
                val view = ItemTypingChatBinding.inflate(inflater, parent, false)
                BotTypingViewHolder(view)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = getItem(position)) {
            is ChatItem.SendText -> (holder as SendTextViewHolder).bind(item)
            is ChatItem.SendDoc -> (holder as SendDocViewHolder).bind(item)
            is ChatItem.ReceiveText -> (holder as ReceiveTextViewHolder).bind(item)
            is ChatItem.BotTyping -> (holder as BotTypingViewHolder)
        }
    }

    class SendTextViewHolder(private val itemBinding : ItemSentChatBinding) : RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(item: ChatItem.SendText) {
            itemBinding.textChat.text = item.message
        }
    }
    class SendDocViewHolder(private val itemBinding : ItemSentDocChatBinding) : RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(item: ChatItem.SendDoc) {
            val fullPath = item.fileUri.lastPathSegment ?: "file"
            val fileName = fullPath.substringAfterLast('/')
            itemBinding.fileName.text = fileName
        }
    }
    class ReceiveTextViewHolder(private val itemBinding : ItemReceiveChatBinding) : RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(item: ChatItem.ReceiveText) {
            itemBinding.textChat.text = item.message
        }
    }
    class BotTypingViewHolder(itemBinding: ItemTypingChatBinding) : RecyclerView.ViewHolder(itemBinding.root)
}
