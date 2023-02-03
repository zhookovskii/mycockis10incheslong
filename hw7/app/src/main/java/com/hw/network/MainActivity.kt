package com.hw.network

import android.content.*
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity(), ChannelSelector {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        commitTransaction()
    }

    private val chatActivityLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            commitTransaction()
        }

    private fun commitTransaction() {
        supportFragmentManager
            .beginTransaction()
            .add(R.id.chat_container, FragmentListChat())
            .commit()
    }

    override fun goToChannel(channel: String) {
        val chatFragment =
            supportFragmentManager.findFragmentById(R.id.chat_messages_container) as FragmentChat?
        chatFragment?.run {
            if (chatFragment.isVisible) {
                selectChannel(channel)
            } else {
                launchChat(channel)
            }
        }
            ?: run {
                launchChat(channel)
            }
    }

    private fun launchChat(channel: String) =
        chatActivityLauncher.launch(
            Intent(this, ChatActivity::class.java).apply {
                putExtra("channel", channel)
            }
        )


}