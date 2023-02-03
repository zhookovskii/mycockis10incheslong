package hashslingingslasher.andoidhw.messenger

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*

open class MessageViewHolder(root: View) : RecyclerView.ViewHolder(root) {
    val fromView: TextView = root.findViewById(R.id.message_from)
    val timeView: TextView = root.findViewById(R.id.message_time)

    open fun bind(message: Message) {
        fromView.text = message.from
        timeView.text = message.time.toDateTime()
    }

    protected fun Long.toDateTime(): String {
        val datePattern = "dd MMM yyyy HH:mm"
        val dateFormat = SimpleDateFormat(datePattern, Locale.ENGLISH)
        return dateFormat.format(this)
    }
}

class TextMessageViewHolder(root: View) : MessageViewHolder(root) {
    val textView: TextView = root.findViewById(R.id.message_text)

    override fun bind(message: Message) {
        textView.text = message.data.Text!!.text
        fromView.text = message.from
        timeView.text = message.time.toDateTime()
    }
}

class ImageMessageViewHolder(root: View) : MessageViewHolder(root) {
    val imageView: ImageView = root.findViewById(R.id.message_image)

    override fun bind(message: Message) {
        fromView.text = message.from
        timeView.text = message.time.toDateTime()
    }
}

class UserTextMessageViewHolder(root: View) : MessageViewHolder(root) {
    val textView: TextView = root.findViewById(R.id.message_text)

    override fun bind(message: Message) {
        textView.text = message.data.Text!!.text
        fromView.text = message.from
        timeView.text = message.time.toDateTime()
    }
}

class UserImageMessageViewHolder(root: View) : MessageViewHolder(root) {
    val imageView: ImageView = root.findViewById(R.id.message_image)

    override fun bind(message: Message) {
        fromView.text = message.from
        timeView.text = message.time.toDateTime()
    }
}

