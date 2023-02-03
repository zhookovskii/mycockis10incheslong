package com.hw.network

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface Api {
    @GET("/channels")
    suspend fun getChannels(): List<String>

    @GET("/channel/{channel}")
    suspend fun fetchMessagesFromChannel(
        @Path("channel") channel: String,
        @Query("limit") limit: Int,
        @Query("lastKnownId") lastKnownId: Int
    ): List<Message>

    @GET("/inbox/{username}")
    suspend fun fetchMessagesFromUser(
        @Path("username") username: String,
        @Query("limit") limit: Int,
        @Query("lastKnownId") lastKnownId: Int
    ): List<Message>

    @POST("/1ch")
    suspend fun postMessageText(
        @Body message: Message
    ): Response<String>

    @Multipart
    @POST("/1ch")
    suspend fun postMessageImage(
        @Part("json") json: RequestBody,
        @Part image: MultipartBody.Part
    ): Response<String>
}