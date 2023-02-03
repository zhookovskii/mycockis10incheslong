package hashslingingslasher.andoidhw.messenger

import androidx.lifecycle.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.net.ConnectException

class MessageViewModel : ViewModel() {

    private val repository = MessageRepository()

    private var lastKnownId = 0

    private val messageList = mutableListOf<Message>()

    val messages: LiveData<List<Message>> = liveData {
        val data = repository.getMessagesFromDb()
        messageList.addAll(data)
        emit(messageList)
        lastKnownId = data.last().id
        while (true) {
            val messages = try {
                repository.getMessagesFromNetwork(100, lastKnownId)
            } catch (e: ConnectException) {
                emptyList()
            }
            if (messages.isNotEmpty()) {
                val tableData = messages.map {
                    TableDataConverter().toTable(it)
                }.toTypedArray()
                MessengerApp.db.messageDao().insertMessages(
                    *tableData
                )
                messageList.addAll(messages)
                emit(messageList)
                lastKnownId = messages.last().id
            }
            delay(2000)
        }
    }

    fun postTextMessage(factory: MessageFactory) {
        val msg = factory.textMessage()
        if (msg != null) {
            viewModelScope.launch {
                repository.postTextMessageToNetwork(msg)
            }
        }
    }

    fun postImageMessage(factory: MessageFactory) {
        val data = factory.imageMessage()
        if (data != null) {
            viewModelScope.launch {
                repository.postImageMessageToNetwork(
                    data.json,
                    data.body
                )
                factory.sweepSweepSweep()
            }
        }
    }
}