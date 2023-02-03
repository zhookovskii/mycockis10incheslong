package hashslingingslasher.andoidhw.messenger

import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

var messageList = mutableListOf<Message>()

const val username = "zhookovskii"

const val BASE_URL = "http://213.189.221.170:8008"

@JsonClass(generateAdapter = true)
data class Message(
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
    val bm: Bitmap? = null
)

@Entity(tableName = "message_table")
data class TableMessage(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "from") val from: String,
    @ColumnInfo(name = "to") val to: String,
    @ColumnInfo(name = "text") val text: String,
    @ColumnInfo(name = "image") val image: Boolean,
    @ColumnInfo(name = "time") val time: Long
)