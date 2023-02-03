package com.hw.network

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NewChannelActivity : AppCompatActivity() {

    private lateinit var channelName: EditText
    private lateinit var firstMessage: EditText
    private lateinit var button: Button

    companion object {
        private const val CURRENT_NAME = "com.hw.network.currentName"
        private const val CURRENT_MESSAGE = "com.hw.network.currentMessage"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.new_channel)
        channelName = findViewById(R.id.channel_name_input)
        firstMessage = findViewById(R.id.first_message_input)
        button = findViewById(R.id.new_channel_button)
        button.setOnClickListener {
            if (approveChannel()) {
                lifecycleScope.launch(Dispatchers.IO) {
                    ChatApp.messageApi.postMessageText(
                        Message(
                            true,
                            true,
                            17,
                            from = ChatApp.MY_NAME,
                            to = channelName.text.toString(),
                            data = Data(Text(firstMessage.text.toString()), null),
                            System.currentTimeMillis()
                        )
                    )
                }
                finish()
            }
        }
    }

    private fun approveChannel(): Boolean {
        val channel = channelName.text.toString()
        if (firstMessage.text.isBlank()) {
            Toast.makeText(this, "write a message!!", Toast.LENGTH_SHORT).show()
            return false
        }
        if (channel.contains(" ")) {
            Toast.makeText(this, "no spaces!!", Toast.LENGTH_SHORT).show()
            return false
        }
        for (c in channel) {
            if (c !in 'a'..'z' && c !in '0'..'9' && c != '@') {
                Toast.makeText(this,
                    "only english letters or digits!!", Toast.LENGTH_SHORT).show()
                return false
            }
        }
        if (channel == "@channel") {
            Toast.makeText(this,
                "add something in front of @channel", Toast.LENGTH_SHORT).show()
            return false
        }
        if (!channel.endsWith("@channel")) {
            Toast.makeText(this, "should end with @channel", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(CURRENT_NAME, channelName.text.toString())
        outState.putString(CURRENT_MESSAGE, firstMessage.text.toString())
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        channelName.setText(savedInstanceState.getString(CURRENT_NAME))
        firstMessage.setText(savedInstanceState.getString(CURRENT_MESSAGE))
    }
}