package com.hw.network

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.*
import java.net.ConnectException
import java.util.concurrent.CopyOnWriteArraySet

class FragmentListChat : Fragment() {

    private val scope = CoroutineScope(Dispatchers.IO)
    private var isRunning = false
    private lateinit var db: MessagesDao
    private lateinit var addButton: ImageButton
    private val channels = CopyOnWriteArraySet<String>()
    private lateinit var recyclerView: RecyclerView

    companion object {
        private const val IS_RUNNING = "com.hw.network.isRunning"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.chat_list, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        db = AppDatabase.getDatabase(view.context)?.messageDao()!!
        addButton = view.findViewById(R.id.add_channel)
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            addButton.isVisible = false
        } else {
            addButton.setOnClickListener {
                val intent = Intent(activity, NewChannelActivity::class.java)
                startActivity(intent)
            }
        }
        recyclerView = view.findViewById(R.id.chat_list)
        if (!isRunning) {
            isRunning = true
            scope.launch {
                getChannels(view)
            }
        }
    }

    private suspend fun getChannels(view: View) {
        try {
            getChannelsFromNetwork(view)
        } catch (e: ConnectException) {
            loadChannelsFromDb(view)
        }
    }

    private suspend fun getChannelsFromNetwork(view: View) = coroutineScope {
        val channelsFromDb = db.getAllChannels().toSet()
        val channelsFromNetwork = ChatApp.messageApi.getChannels().toSet()
        val newChannels = channelsFromNetwork.minus(channelsFromDb)
        if (newChannels.isNotEmpty()) {
            for (channel in newChannels) {
                db.insertChannel(Channel(channel))
            }
        }
        channels.apply {
            addAll(channelsFromDb)
            addAll(channelsFromNetwork)
        }
        withContext(Dispatchers.Main) {
            showChannels(view)
        }
    }

    private suspend fun loadChannelsFromDb(view: View) = coroutineScope {
        scope.launch {
            channels.addAll(db.getAllChannels())
            withContext(Dispatchers.Main) {
                showChannels(view)
            }
        }
    }

    private suspend fun showChannels(view: View) = coroutineScope {
        recyclerView.apply {
            layoutManager = LinearLayoutManager(view.context)
            adapter = ChatListAdapter(view.context, channels.toList()) { channel ->
                val selector = activity as ChannelSelector
                selector.goToChannel(channel)
            }
        }
    }

    override fun onDestroy() {
        scope.cancel()
        super.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean(IS_RUNNING, isRunning)
        super.onSaveInstanceState(outState)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        isRunning = savedInstanceState?.getBoolean(IS_RUNNING) ?: false
    }

}