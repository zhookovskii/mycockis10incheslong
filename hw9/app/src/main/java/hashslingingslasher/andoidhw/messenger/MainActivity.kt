package hashslingingslasher.andoidhw.messenger

import android.app.Activity
import android.content.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var sendButton: ImageButton
    private lateinit var messageInput: EditText
    private lateinit var attachFileButton: ImageButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewManager: LinearLayoutManager

    var recyclerPosition = 0

    private val messageViewModel: MessageViewModel by viewModels()

    companion object {
        private const val RECYCLER_POSITION = "messenger.MainActivity.recyclerPosition"
    }

    private val messageObserver = Observer<List<Message>> {
        messages ->
        run {
            messageList = messages as MutableList<Message>
            updateRecyclerView(recyclerView, viewManager)
            recyclerPosition = messageList.size - 1
            recyclerView.scrollToPosition(recyclerPosition)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_main)
        viewManager = LinearLayoutManager(this)
        recyclerView = findViewById(R.id.recycler_view)

        messageViewModel.messages.observe(this, messageObserver)

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
            if (messageInput.text.toString().isNotBlank()) {
                messageViewModel.postTextMessage(
                    MessageFactory(
                        messageInput.text.toString(),
                        null,
                        username,
                        "1@channel",
                        time(),
                        this
                    )
                )
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
                    messageViewModel.postImageMessage(
                        MessageFactory(
                            null,
                            data.data,
                            username,
                            "1@channel",
                            time(),
                            this
                        )
                    )
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
