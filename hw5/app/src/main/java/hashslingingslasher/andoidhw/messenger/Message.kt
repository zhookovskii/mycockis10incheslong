package hashslingingslasher.andoidhw.messenger

import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

data class Message(val id: Int, val from: String, val to: String, val msgData: MsgData, val time: Long)

data class MsgData(val msgText: String?, val bitmapImage: BitmapImage?)

data class BitmapImage(val link: String, val bitmap: Bitmap?)

var messageList = mutableListOf<Message>()

const val username = "zhookovskii"

@Entity(tableName = "message_table")
data class TableMessage(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "from") val from: String,
    @ColumnInfo(name = "to") val to: String,
    @ColumnInfo(name = "text") val text: String,
    @ColumnInfo(name = "image") val image: Boolean,
    @ColumnInfo(name = "time") val time: Long
)