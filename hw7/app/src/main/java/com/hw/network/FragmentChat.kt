package com.hw.network

import android.app.Activity
import android.content.*
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.net.ConnectException
import java.util.concurrent.CopyOnWriteArrayList

class FragmentChat : Fragment() {

    private var channel = "1@channel"
    private val messagesList = CopyOnWriteArrayList<Message>()
    private lateinit var chatView: View
    private var lastId = 0
    private var isRunning = false
    private lateinit var db: MessagesDao
    private var recyclerView: RecyclerView? = null
    private lateinit var imageButton: ImageButton
    private lateinit var sendButton: ImageButton
    private lateinit var textInput: EditText
    private lateinit var channelName: TextView
    private var scope = CoroutineScope(Dispatchers.IO)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val args = arguments
        if (args != null) {
            args.getString("channel")?.let {
                channel = it
            }
        }
        return inflater.inflate(R.layout.chat, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        db = AppDatabase.getDatabase(view.context)?.messageDao()!!
        chatView = view
        recyclerView = view.findViewById(R.id.recyclerView)
        imageButton = view.findViewById(R.id.imageButtonImage)
        sendButton = view.findViewById(R.id.imageButtonMessage)
        textInput = view.findViewById(R.id.message)
        channelName = view.findViewById(R.id.channel_name)
        channelName.text = channel
        runChat()
        if (!isRunning) {
            isRunning = true
            scope.launch {
                fetchMessages()
            }
        }
    }

    private fun runChat() {
        imageButton.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_GET_CONTENT
            intent.type = "image/*"
            launchActivity.launch(intent)
        }
        sendButton.setOnClickListener {
            val messageText = textInput.text.toString()
            textInput.text.clear()
            scope.launch {
                ChatApp.messageApi.postMessageText(
                    Message(
                        true,
                        true,
                        237,
                        ChatApp.MY_NAME,
                        channel,
                        Data(Text(messageText), null),
                        System.currentTimeMillis()
                    )
                )
            }
        }
    }

    private val launchActivity =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                scope.launch {
                    try {
                        sendMessageImage(it.data?.data)
                    } catch (e: ConnectException) {}
                }
            }
        }

    private suspend fun sendMessageImage(uri: Uri?) = coroutineScope {
        if (uri != null) {
            runCatching {
                val file = getFileFromUri(uri)
                val to = channel
                if (file != null) {
                    val json =
                        "{\"from\":\"${ChatApp.MY_NAME}\",\"to\":\"$to\"}"
                    val requestFile = RequestBody
                        .create(MediaType.parse("multipart/form-data"), file)
                    val body = MultipartBody.Part
                        .createFormData("image", file.name, requestFile)
                    val requestJson = RequestBody
                        .create(MediaType.parse("multipart/form-data"), json)

                    ChatApp.messageApi.postMessageImage(
                        requestJson,
                        body
                    )
                    file.delete()
                }
            }
        }
    }

    private fun getFileFromUri(uri: Uri): File? {
        val bm = getBitmapFromUri(uri)
        val fileName = "${System.currentTimeMillis()}.png"
        if (bm != null) {
            return try {
                val file = File(chatView.context.cacheDir, fileName)
                file.createNewFile()
                val fileOutputStream = FileOutputStream(file)
                bm.compress(Bitmap.CompressFormat.PNG, 0, fileOutputStream)
                fileOutputStream.apply {
                    flush()
                    close()
                }
                file
            } catch (e: FileNotFoundException) {
                null
            } catch (e: IOException) {
                null
            }
        } else {
            return null
        }
    }

    private fun getBitmapFromUri(selectedFileUri: Uri): Bitmap? {
        var image: Bitmap? = null
        try {
            val parcelFileDescriptor =
                chatView
                    .context
                    .contentResolver
                    .openFileDescriptor(selectedFileUri, "r")
            val fileDescriptor = parcelFileDescriptor!!.fileDescriptor
            image = BitmapFactory.decodeFileDescriptor(fileDescriptor)
            parcelFileDescriptor.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return image
    }

    override fun onDestroy() {
        scope.cancel()
        super.onDestroy()
    }

    private suspend fun fetchMessagesFromNetwork() = coroutineScope {
        val start = messagesList.size
        val newMessages = if (channel.contains("channel")) {
            ChatApp
                .messageApi
                .fetchMessagesFromChannel(
                    channel,
                    100,
                    lastId
                )
        } else {
            ChatApp
                .messageApi
                .fetchMessagesFromUser(
                    channel,
                    100,
                    lastId
                ).filter { it.to == ChatApp.MY_NAME }
        }
        if (newMessages.isNotEmpty()) {
            for (message in newMessages) {
                messagesList.add(message)
                lastId = message.id
                message.isTextMessage = message.data.Text != null
                message.messageMy = message.from == ChatApp.MY_NAME
                db.insert(
                    Entity(
                        message.id,
                        message.from,
                        message.to,
                        message.time,
                        message.isTextMessage,
                        message.data.Text?.text,
                        message.data.Image?.link,
                    )
                )
            }
            val count = messagesList.size - start
            withContext(Dispatchers.Main) {
                updateRecyclerView(start, count)
            }
        }
    }

    private suspend fun fetchMessages() {
        messagesList.addAll(
            db.getMessagesFromChannel(channel)
                .map { it.transformMessage() }
        )
        withContext(Dispatchers.Main) {
            if (recyclerView == null) {
                recyclerView = chatView.findViewById(R.id.recyclerView)
            }
            recyclerView!!.apply {
                layoutManager = LinearLayoutManager(chatView.context).apply {
                    stackFromEnd = true
                }
                adapter = MessagesAdapter(chatView.context, messagesList)
            }
        }
        if (messagesList.isNotEmpty()) {
            lastId = messagesList.last().id
        }
        while (true) {
            try {
                fetchMessagesFromNetwork()
            } catch (e: ConnectException) {}
            delay(2000)
        }
    }

    private fun updateRecyclerView(start: Int, count: Int) {
        recyclerView!!.adapter?.notifyItemRangeInserted(start, count)
        if (count != 0) {
            recyclerView!!.scrollToPosition(start + count - 1)
        }
    }

    private fun Entity.transformMessage(): Message {
        if (isText) {
            return Message(
                name == ChatApp.MY_NAME,
                isText,
                id,
                name,
                to,
                Data(Text(text!!), null),
                date
            )
        } else {
            return Message(
                name == ChatApp.MY_NAME,
                isText,
                id,
                name,
                to,
                Data(null, Image(imageLink!!, null)),
                date
            )
        }
    }

    fun selectChannel(channel: String) {
        this.channel = channel
        channelName.text = channel
        scope.cancel()
        scope = CoroutineScope(Dispatchers.IO)
        isRunning = false
        recyclerView!!.adapter!!.notifyItemRangeRemoved(0, messagesList.size)
        messagesList.clear()
        scope.launch {
            fetchMessages()
        }
    }
}