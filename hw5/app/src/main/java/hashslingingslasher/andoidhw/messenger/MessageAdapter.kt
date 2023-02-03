package hashslingingslasher.andoidhw.messenger

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import java.lang.IllegalArgumentException

class MessageAdapter(
    private val messages: List<Message>,
    private val onClick: (Message) -> Unit
) : RecyclerView.Adapter<MessageViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val holder = when (viewType) {
            0 -> {
                TextMessageViewHolder(
                    LayoutInflater
                        .from(parent.context)
                        .inflate(R.layout.message_layout, parent, false)
                )
            }
            1 -> {
                ImageMessageViewHolder(
                    LayoutInflater
                        .from(parent.context)
                        .inflate(R.layout.image_message_layout, parent, false)
                )
            }
            2 -> {
                UserTextMessageViewHolder(
                    LayoutInflater
                        .from(parent.context)
                        .inflate(R.layout.user_message_layout, parent, false)
                )
            }
            3 -> {
                UserImageMessageViewHolder(
                    LayoutInflater
                        .from(parent.context)
                        .inflate(R.layout.user_image_message_layout, parent, false)
                )
            }
            else -> throw IllegalArgumentException("Unknown message type")
        }
        when (holder) {
            is TextMessageViewHolder -> {
                holder.textView.setOnClickListener {
                    onClick(messages[holder.bindingAdapterPosition])
                }
            }
            is ImageMessageViewHolder -> {
                holder.imageView.setOnClickListener {
                    onClick(messages[holder.bindingAdapterPosition])
                }
            }
            is UserTextMessageViewHolder -> {
                holder.textView.setOnClickListener {
                    onClick(messages[holder.bindingAdapterPosition])
                }
            }
            is UserImageMessageViewHolder -> {
                holder.imageView.setOnClickListener {
                    onClick(messages[holder.bindingAdapterPosition])
                }
            }
        }
        return holder
    }

    override fun getItemViewType(position: Int): Int {
        val msg = messages[position]
        if (msg.from != username) {
            return if (msg.msgData.msgText != null) {
                0 // this message is of text type
            } else if (msg.msgData.bitmapImage != null) {
                1 // this message is of image type
            } else {
                4 // this message is of unknown type
            }
        } else {
            return if (msg.msgData.msgText != null) {
                2 // this message is of user_text type
            } else if (msg.msgData.bitmapImage != null) {
                3 // this message is of user_image type
            } else {
                4 // this message is of unknown type
            }
        }
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) =
        holder.bind(messages[position])


    override fun getItemCount(): Int = messages.size

}