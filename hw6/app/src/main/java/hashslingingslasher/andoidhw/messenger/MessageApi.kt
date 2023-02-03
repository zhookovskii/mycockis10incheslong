package hashslingingslasher.andoidhw.messenger

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface MessageApi {
    @GET("/1ch")
    suspend fun getListMessages(
        @Query("limit") limit: Int,
        @Query("lastKnownId") lastKnownId: Int
    ): List<Message>

    @POST("/1ch")
    suspend fun postTextMessage(
        @Body message: Message
    ): Response<String>

    @Multipart
    @POST("/1ch")
    suspend fun postImageMessage(
        @Part("json") json: RequestBody,
        @Part image: MultipartBody.Part
    ): Response<String>
}