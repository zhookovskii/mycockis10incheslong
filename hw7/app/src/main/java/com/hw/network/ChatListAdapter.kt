package com.hw.network

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class ChatListAdapter(
    private val context: Context,
    private val channels: List<String>,
    private val onClick: (String) -> Unit
): RecyclerView.Adapter<ChatViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val holder = ChatViewHolder(
            LayoutInflater
                .from(context)
                .inflate(R.layout.chat_item, parent, false)
        )
        holder.chatView.setOnClickListener {
            onClick(channels[holder.adapterPosition])
        }
        return holder
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) =
        holder.bind(channels[position])

    override fun getItemCount(): Int = channels.size
}