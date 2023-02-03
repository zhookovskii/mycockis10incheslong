package hashslingingslasher.andoidhw.messenger

import android.app.Activity
import android.content.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.widget.*
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var sendButton: ImageButton
    private lateinit var messageInput: EditText
    private lateinit var attachFileButton: ImageButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewManager: LinearLayoutManager

    var messageService: MessageService? = null
    var isBound = false

    var recyclerPosition = 0

    private val boundServiceConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binderBridge: MessageService.MessageBinder =
                service as MessageService.MessageBinder
            messageService = binderBridge.getMessageService()
            isBound = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            isBound = false
            messageService = null
        }
    }

    override fun onStart() {
        super.onStart()
        val intent = Intent(this, MessageService::class.java)
        startService(intent)
        bindService(intent, boundServiceConnection, BIND_AUTO_CREATE)
    }

    override fun onStop() {
        super.onStop()
        if (isBound) {
            unbindService(boundServiceConnection)
        }
    }

    companion object {
        private const val RECYCLER_POSITION = "messenger.MainActivity.recyclerPosition"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_main)
        viewManager = LinearLayoutManager(this)
        recyclerView = findViewById(R.id.recycler_view)

        recyclerView.addOnScrollListener(
            object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        val manager = recyclerView.layoutManager as LinearLayoutManager
                        recyclerPosition = manager.findFirstVisibleItemPosition()
                    }
                }
            }
        )

        messageInput = findViewById(R.id.message_input)

        sendButton = findViewById(R.id.send_button)
        sendButton.setOnClickListener {
            Log.i("MESSAGE_SENDER", "broadcasting message!")
            if (messageInput.text.toString().isBlank()) {
                // do nothing
            } else {
                val intent = Intent("SEND_MESSAGE")
                intent.putExtra("type", 0)
                intent.putExtra("text", messageInput.text.toString())
                intent.putExtra("from", username)
                intent.putExtra("to", "1@channel")
                intent.putExtra("when", time())
                LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
                messageInput.setText("")
            }
        }

        attachFileButton = findViewById(R.id.file_button)
        attachFileButton.setOnClickListener {
            val chooseFileIntent = Intent()
            chooseFileIntent.type = "image/*"
            chooseFileIntent.action = Intent.ACTION_GET_CONTENT
            sendFileActivity.launch(chooseFileIntent)
        }

        LocalBroadcastManager.getInstance(this).registerReceiver(
            messageReceiver,
            IntentFilter("NEW_MESSAGES")
        )
    }

    private val messageReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent != null) {
                Log.i("FROM_MAIN", "received request to update view")
                val mode = intent.getIntExtra("mode", -1)
                updateRecyclerView(recyclerView, viewManager)
                if (mode == 0) {    // new messages
                    recyclerView.scrollToPosition(messageList.size - 1)
                    recyclerPosition = messageList.size - 1
                } else {    // view update
                    recyclerView.scrollToPosition(recyclerPosition)
                }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(RECYCLER_POSITION, recyclerPosition)
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        updateRecyclerView(recyclerView, viewManager)
        recyclerPosition = savedInstanceState.getInt(RECYCLER_POSITION)
        recyclerView.scrollToPosition(recyclerPosition)
    }

    private val sendFileActivity = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                if (data != null) {
                    Log.i("SMERT", "sending image")
                    val intent = Intent("SEND_MESSAGE")
                    intent.putExtra("type", 1)
                    intent.putExtra("uri", data.data.toString())
                    intent.putExtra("from", username)
                    intent.putExtra("to", "1@channel")
                    intent.putExtra("when", time())
                    LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
                }
            }
    }

    private fun updateRecyclerView(recyclerView: RecyclerView,
                           viewManager: LinearLayoutManager) {
        recyclerView.apply {
            layoutManager = viewManager
            adapter = MessageAdapter(messageList) {
                if (it.data.Image != null) {
                    val intent = Intent(this@MainActivity,
                        ImageFullResActivity::class.java)
                    intent.putExtra("id", it.id.toString())
                    intent.putExtra("link", it.data.Image.link)
                    startActivity(intent)
                } else if (it.data.Text != null) {
                    Toast.makeText(context, "Clicked on message from ${it.from}",
                        Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun time() = System.currentTimeMillis()

}
