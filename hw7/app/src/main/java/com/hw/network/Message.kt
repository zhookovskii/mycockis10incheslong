package com.hw.network

import android.graphics.Bitmap
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Message (
    @Transient
    var messageMy: Boolean = false,
    @Transient
    var isTextMessage: Boolean = false,
    @field:Json(name = "id")
    val id: Int,
    @field:Json(name = "from")
    val from: String,
    @field:Json(name = "to")
    val to: String,
    @field:Json(name = "data")
    val data: Data,
    @field:Json(name = "time")
    val time: Long
)

@JsonClass(generateAdapter = true)
data class Data(
    @field:Json(name = "Text")
    val Text: Text? = null,
    @field:Json(name = "Image")
    val Image: Image? = null
)

@JsonClass(generateAdapter = true)
data class Text(
    @field:Json(name = "text")
    val text: String
)

@JsonClass(generateAdapter = true)
data class Image(
    @field:Json(name = "link")
    val link: String,
    @Transient
    val bitmap: Bitmap? = null
)