package hashslingingslasher.andoidhw.messenger

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response

class MessageRepository {

    suspend fun getMessagesFromNetwork(limit: Int, lastKnownId: Int) : List<Message> {
        return MessengerApp.messageApi.getListMessages(limit, lastKnownId)
    }

    suspend fun getMessagesFromDb() : List<Message> {
        return TableDataConverter(MessengerApp.db.messageDao().getAll()).convert()
    }

    suspend fun postTextMessageToNetwork(message: Message): Response<String> {
        return MessengerApp.messageApi.postTextMessage(message)
    }

    suspend fun postImageMessageToNetwork(
        json: RequestBody,
        body: MultipartBody.Part
    ): Response<String> {
        return MessengerApp.messageApi.postImageMessage(json, body)
    }

}
