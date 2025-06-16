package com.gerrysatria.jobbotapp.activity.chat

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.InsetDrawable
import android.net.Uri
import android.os.Bundle
import android.util.TypedValue
import android.view.Menu
import android.view.View
import android.widget.PopupMenu
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.menu.MenuBuilder
import androidx.recyclerview.widget.LinearLayoutManager
import com.gerrysatria.jobbotapp.databinding.ActivityMainBinding
import com.gerrysatria.jobbotapp.R
import com.gerrysatria.jobbotapp.activity.home.HomeActivity
import com.gerrysatria.jobbotapp.data.MessageChatRequest
import com.gerrysatria.jobbotapp.utils.CHAT_ID_KEY
import com.gerrysatria.jobbotapp.utils.CameraUtils
import com.gerrysatria.jobbotapp.utils.CameraUtils.getImageUri
import com.gerrysatria.jobbotapp.utils.State
import com.gerrysatria.jobbotapp.utils.dialogDeleteAction
import com.gerrysatria.jobbotapp.utils.show
import com.gerrysatria.jobbotapp.utils.showDialog
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var chatAdapter: ChatAdapter
    private val viewModel : MainViewModel by viewModel()
    private var currentPreviewUri: Uri? = null
    private var chatId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        chatId = intent.getIntExtra(CHAT_ID_KEY, 0)
        setupRecyclerView()
        loadMessages(chatId)

        binding.apply {
            btnAttachment.setOnClickListener { showAttachmentMenu(it) }
            topBar.topAppBar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }
            topBar.topAppBar.setOnMenuItemClickListener { menuItem ->
                when(menuItem.itemId){
                    R.id.menu_delete -> {
                        dialogDeleteAction(this@MainActivity, getString(R.string.title_delete_chat), getString(R.string.delete_chat)){
                            viewModel.deleteMessagesChat(chatId).observe(this@MainActivity) { state ->
                                when (state) {
                                    is State.Loading -> progressBar.show(true)
                                    is State.Success -> {
                                        progressBar.show(false)
                                        showDialog(this@MainActivity, getString(R.string.success), getString(R.string.success_delete_chat))
                                        startActivity(Intent(this@MainActivity, HomeActivity::class.java))
                                        finish()
                                    }
                                    is State.Error -> {
                                        progressBar.show(false)
                                        showDialog(this@MainActivity, getString(R.string.error), getString(R.string.error_delete_chat))
                                    }
                                }
                            }
                        }
                        true
                    }
                    else -> false
                }
            }
            btnSent.setOnClickListener {
                val isPreviewFileVisible = previewFileLayout.root.visibility == View.VISIBLE
                if (isPreviewFileVisible) {
                    val uri = currentPreviewUri
                    if (uri != null) {
                        previewFileLayout.root.visibility = View.GONE
                        topBar.root.visibility = View.VISIBLE
                        sendFileMessage(uri)
                    } else {
                        showDialog(this@MainActivity, getString(R.string.error), getString(R.string.error_file))
                    }
                } else {
                    val text = messageEditText.text.toString()
                    if (text.isNotBlank()) {
                        sendTextMessage(text)
                        messageEditText.setText("")
                    }
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top_bar_menu, menu)
        return true
    }

    private fun setupRecyclerView() {
        chatAdapter = ChatAdapter()
        binding.listChat.apply {
            adapter = chatAdapter
            layoutManager = LinearLayoutManager(this@MainActivity).apply {
                stackFromEnd = true
            }
        }
    }

    private fun loadMessages(chatId: Int) {
        viewModel.getMessagesByChatId(chatId).observe(this) { state ->
            when (state) {
                is State.Loading -> binding.progressBar.show(true)
                is State.Success -> {
                    binding.progressBar.show(false)
                    val items = state.data.map { message ->
                        when {
                            message.sender == "bot" -> ChatItem.ReceiveText(message.message)
                            message.sender == "user" && message.fileUrl != null -> ChatItem.SendDoc(Uri.parse(message.fileUrl))
                            else -> ChatItem.SendText(message.message)
                        }
                    }
                    chatAdapter.submitList(items) {
                        binding.listChat.scrollToPosition(items.size - 1)
                    }
                }
                is State.Error -> {
                    binding.progressBar.show(false)
                    showDialog(this, getString(R.string.error), getString(R.string.error_get_chat_data))
                }
            }
        }
    }

    @SuppressLint("RestrictedApi")
    private fun showAttachmentMenu(anchor: View) {
        val popup = PopupMenu(this, anchor)
        popup.menuInflater.inflate(R.menu.menu_attachment, popup.menu)

        if (popup.menu is MenuBuilder) {
            val menuBuilder = popup.menu as MenuBuilder
            menuBuilder.setOptionalIconsVisible(true)

            val iconMarginPx = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 8f, resources.displayMetrics
            ).toInt()

            for (item in menuBuilder.visibleItems) {
                item.icon?.let { icon ->
                    item.icon = InsetDrawable(icon, iconMarginPx, 0, iconMarginPx, 0)
                }
            }
        }
        popup.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_take_photo -> {
                    openCamera()
                    true
                }
                R.id.menu_upload_file -> {
                    openFilePicker()
                    true
                }
                else -> false
            }
        }
        popup.show()
    }

    private val launcherCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) showImage()
    }

    private val launcherFilePicker = registerForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        if (uri != null) {
            currentPreviewUri = uri
            showFile()
        }
    }

    private fun openFilePicker(){
        launcherFilePicker.launch(arrayOf("image/*", "application/pdf"))
    }

    private fun openCamera(){
        currentPreviewUri = getImageUri(this)
        currentPreviewUri?.let { launcherCamera.launch(it) }
    }

    private fun showImage() {
        currentPreviewUri?.let { uri ->
            binding.apply {
                previewFileLayout.root.visibility = View.VISIBLE
                previewFileLayout.btnClosePreview.setOnClickListener {
                    topBar.root.visibility = View.VISIBLE
                    previewFileLayout.root.visibility = View.GONE
                }
                previewFileLayout.imagePreview.setImageURI(uri)
                previewFileLayout.fileName.text = CameraUtils.getFileName(this@MainActivity, uri)
            }
        }
    }

    private fun showFile() {
        currentPreviewUri?.let {
            binding.apply {
                previewFileLayout.root.visibility = View.VISIBLE
                previewFileLayout.btnClosePreview.setOnClickListener {
                    topBar.root.visibility = View.VISIBLE
                    previewFileLayout.root.visibility = View.GONE
                }

                val mimeType = contentResolver.getType(it)
                if (mimeType?.startsWith("image/") == true) previewFileLayout.imagePreview.setImageURI(it)
                else previewFileLayout.imagePreview.setImageResource(R.drawable.ic_file_preview)

                previewFileLayout.fileName.text = CameraUtils.getFileName(this@MainActivity, it)
            }
        }
    }

    private fun sendTextMessage(text: String) {
        val messageRequest = MessageChatRequest(
            chatId = chatId,
            sender = "user",
            message = text
        )
        val currentList = chatAdapter.currentList.toMutableList()
        viewModel.sendMessageUser(messageRequest).observe(this) { state ->
            when (state) {
                is State.Loading -> {}
                is State.Success -> {
                    currentList.add(ChatItem.SendText(text))
                    chatAdapter.submitList(currentList) {
                        binding.listChat.scrollToPosition(currentList.size - 1)
                    }
                    viewModel.getRecommendationByText(text, chatId).observe(this) { recommendationState ->
                        when (recommendationState) {
                            is State.Loading -> {
                                currentList.add(ChatItem.BotTyping)
                                chatAdapter.submitList(currentList) {
                                    binding.listChat.scrollToPosition(currentList.size - 1)
                                }
                            }
                            is State.Success -> {
                                val botMessage = recommendationState.data.recommendationText
                                currentList.removeAll { it is ChatItem.BotTyping }
                                currentList.add(ChatItem.ReceiveText(botMessage.toString()))
                                chatAdapter.submitList(currentList) {
                                    binding.listChat.scrollToPosition(currentList.size - 1)
                                }
                            }
                            is State.Error -> {
                                currentList.removeAll { it is ChatItem.BotTyping }
                                showDialog(this, getString(R.string.error), recommendationState.error)
                                chatAdapter.submitList(currentList)
                            }
                        }
                    }
                }
                is State.Error -> showDialog(this, getString(R.string.error), getString(R.string.error_send_message))
            }
        }
    }

    private fun sendFileMessage(uri: Uri) {
        val fileName = CameraUtils.getFileName(this, uri)
        val mimeType = contentResolver.getType(uri) ?: "application/octet-stream"
        val inputStream = contentResolver.openInputStream(uri)
        if (inputStream != null) {
            val requestFile = inputStream.readBytes().toRequestBody(mimeType.toMediaTypeOrNull())
            val multipartBody = MultipartBody.Part.createFormData(
                "file", fileName, requestFile
            )

            val currentList = chatAdapter.currentList.toMutableList()
            val messageRequest = MessageChatRequest(
                chatId = chatId,
                sender = "user",
                message = "[File] $fileName",
                isFile = true,
                fileName = fileName,
                fileUrl = uri.toString()
            )

            viewModel.sendMessageUser(messageRequest).observe(this) { state ->
                when (state) {
                    is State.Loading -> {}
                    is State.Success -> {
                        currentList.add(ChatItem.SendDoc(uri))
                        chatAdapter.submitList(currentList) {
                            binding.listChat.scrollToPosition(currentList.size - 1)
                        }
                        viewModel.getRecommendationByImgOrPDF(multipartBody, chatId).observe(this) { recommendationState ->
                            when (recommendationState) {
                                is State.Loading -> {
                                    currentList.add(ChatItem.BotTyping)
                                    chatAdapter.submitList(currentList) {
                                        binding.listChat.scrollToPosition(currentList.size - 1)
                                    }
                                }
                                is State.Success -> {
                                    currentList.removeAll { it is ChatItem.BotTyping }
                                    currentList.add(ChatItem.ReceiveText(recommendationState.data.recommendationText.toString()))
                                    chatAdapter.submitList(currentList) {
                                        binding.listChat.scrollToPosition(currentList.size - 1)
                                    }
                                }
                                is State.Error -> {
                                    currentList.removeAll { it is ChatItem.BotTyping }
                                    chatAdapter.submitList(currentList)
                                    showDialog(this, getString(R.string.error), getString(R.string.error_process_file))
                                }
                            }
                        }
                    }
                    is State.Error -> showDialog(this, getString(R.string.error), getString(R.string.error_send_message))
                }
            }
        } else showDialog(this, getString(R.string.error), getString(R.string.error_read_file))
    }
}