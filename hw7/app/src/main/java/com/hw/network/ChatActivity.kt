package com.hw.network

import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.FragmentActivity

class ChatActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            finish()
        } else {
            loadFragment()
        }
    }

    private fun loadFragment() {
        val channel = intent.getStringExtra("channel")
        supportFragmentManager
            .beginTransaction()
            .replace(
                R.id.chat_messages_container,
                FragmentChat().apply {
                    arguments = Bundle().apply {
                        putString("channel", channel)
                    }
                }
            ).commit()
    }
}