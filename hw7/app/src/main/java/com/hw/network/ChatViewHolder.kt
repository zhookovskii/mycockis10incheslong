package com.hw.network

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ChatViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val chatView: TextView = view.findViewById(R.id.chat_item)

    fun bind(chat: String) {
        chatView.text = chat
    }
}