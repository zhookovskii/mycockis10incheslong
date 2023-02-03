package hashslingingslasher.andoidhw.messenger

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
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
            return if (msg.data.Text != null) {
                0 // this message is of text type
            } else if (msg.data.Image != null) {
                1 // this message is of image type
            } else {
                4 // this message is of unknown type
            }
        } else {
            return if (msg.data.Text != null) {
                2 // this message is of user_text type
            } else if (msg.data.Image != null) {
                3 // this message is of user_image type
            } else {
                4 // this message is of unknown type
            }
        }
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val msg = messages[position]
        if (getItemViewType(position) == 1) {
            Picasso.get()
                .load(BASE_URL + "/img/" + msg.data.Image!!.link)
                .placeholder(R.drawable.loading)
                .resize(500, 0)
                .into((holder as ImageMessageViewHolder).imageView)
        } else if (getItemViewType(position) == 3) {
            Picasso.get()
                .load(BASE_URL + "/img/" + msg.data.Image!!.link)
                .placeholder(R.drawable.loading)
                .resize(500, 0)
                .into((holder as UserImageMessageViewHolder).imageView)
        }
        holder.bind(messages[position])
    }



    override fun getItemCount(): Int = messages.size

}