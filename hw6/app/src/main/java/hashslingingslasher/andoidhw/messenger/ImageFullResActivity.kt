package hashslingingslasher.andoidhw.messenger

import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso

class ImageFullResActivity : AppCompatActivity() {

    private lateinit var imageView: ImageView
    private var id: String = ""
    private var link: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.image_full_res_layout)

        id = intent.getStringExtra("id")!!
        link = intent.getStringExtra("link")!!
        imageView = findViewById(R.id.full_res_image)
    }

    override fun onStart() {
        super.onStart()
        try {
            Picasso.get()
                .load("$BASE_URL/img/$link")
                .placeholder(R.drawable.loadingfull)
                .into(imageView)
        } catch (e: Exception) {
            imageView.setImageResource(R.drawable.loading)
            mainHandler.post {
                Toast.makeText(applicationContext, "Unable to load image", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }
}