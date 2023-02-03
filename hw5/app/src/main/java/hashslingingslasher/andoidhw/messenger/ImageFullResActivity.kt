package hashslingingslasher.andoidhw.messenger

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.io.File
import java.io.FileOutputStream

class ImageFullResActivity : AppCompatActivity() {

    private lateinit var imageView: ImageView
    private var imageBitmap: Bitmap? = null
    private var id: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.image_full_res_layout)

        id = intent.getStringExtra("id")!!
        imageView = findViewById(R.id.full_res_image)
    }

    override fun onStart() {
        super.onStart()
        for (f in cacheDir.listFiles()!!) {
            if (f.name == "${id}.png") {
                val fis = f.inputStream()
                imageBitmap = BitmapFactory.decodeStream(fis)
                fis.close()
            }
        }
        if (imageBitmap != null) {
            imageView.setImageBitmap(imageBitmap)
        }
    }

    override fun onResume() {
        super.onResume()
        if (imageBitmap == null) {
            imageBitmap = launchImageLoader(id.toInt())
            if (imageBitmap != null) {
                imageView.setImageBitmap(imageBitmap)
                writeImageToCache(imageBitmap!!, id)
            } else {
                mainHandler.post {
                    Toast.makeText(
                        applicationContext,
                        "Unable to load image, check your internet connection",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun launchImageLoader(id: Int): Bitmap? {
        val il = ImageLoader(id, "img")
        val ilThread = Thread(il)
        ilThread.start()
        ilThread.join()
        val bm = il.bitmap
        ilThread.interrupt()
        return if (il.isOK) {
            bm
        } else {
            null
        }
    }

    private fun writeImageToCache(bm: Bitmap, name: String) {
        val f = File(cacheDir, "$name.png")
        f.createNewFile()
        val fos = FileOutputStream(f)
        bm.compress(Bitmap.CompressFormat.PNG, 0, fos)
        fos.apply { flush(); close() }
    }
}