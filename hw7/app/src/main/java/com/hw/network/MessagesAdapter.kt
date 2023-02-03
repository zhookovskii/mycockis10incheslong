package com.hw.network

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*


class MessagesAdapter(
    private val context: Context,
    private val messages: List<Message>
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val TEXT = 1
    val IMAGE = 2

    private val formatDate = SimpleDateFormat("HH:mm dd/MM/yyyy", Locale.ENGLISH)

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val messageView = view.findViewById<LinearLayout>(R.id.message_view)!!
        val name = view.findViewById<TextView>(R.id.name)!!
        val text = view.findViewById<TextView>(R.id.text)!!
        val date = view.findViewById<TextView>(R.id.time)!!
    }

    class ViewHolderImage(view: View) : RecyclerView.ViewHolder(view) {
        val messageView = view.findViewById<LinearLayout>(R.id.image_view)!!
        val name = view.findViewById<TextView>(R.id.name)!!
        val image = view.findViewById<ImageView>(R.id.image)!!
        val date = view.findViewById<TextView>(R.id.time)!!
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TEXT -> ViewHolder(
                LayoutInflater.from(context).inflate(R.layout.text, parent, false)
            )
            else -> {
                ViewHolderImage(
                    LayoutInflater.from(context).inflate(R.layout.image, parent, false)
                )
            }
        }
    }

    override fun getItemViewType(position: Int): Int = when {
        messages[position].isTextMessage-> TEXT
        else -> IMAGE
    }

    override fun getItemCount(): Int = messages.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val type: Int = getItemViewType(position)
        val message = messages[position]
        when (type) {
            TEXT -> {
                val holderView = holder as ViewHolder
                holderView.name.text = message.from
                holderView.text.text = message.data.Text!!.text
                holderView.date.text = formatDate.format(Date(message.time))
                if (message.messageMy){
                    holderView.messageView.setBackgroundResource(R.drawable.fon_message_my)
                } else {
                    holderView.messageView.setBackgroundResource(R.drawable.fon_message)
                }
            }
            IMAGE -> {
                val holderView = holder as ViewHolderImage
                holderView.image.setOnClickListener {
                    val intentStart = Intent(context, OpenImage::class.java)
                    intentStart.putExtra("link", message.data.Image!!.link)
                    context.startActivity(intentStart)
                }
                holderView.name.text = message.from
                holderView.date.text = formatDate.format(Date(message.time))
                if (message.messageMy){
                    holderView.messageView.setBackgroundResource(R.drawable.fon_message_my)
                } else {
                    holderView.messageView.setBackgroundResource(R.drawable.fon_message)
                }
                Picasso.get()
                    .load(ChatApp.BASE_URL + "/thumb/" + message.data.Image!!.link)
                    .into(holder.image)
            }
        }
    }
}