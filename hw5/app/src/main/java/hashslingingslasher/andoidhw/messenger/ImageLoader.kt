package hashslingingslasher.andoidhw.messenger

import android.graphics.Bitmap
import org.json.JSONArray
import org.json.JSONObject
import org.json.JSONTokener
import java.net.ConnectException
import java.net.HttpURLConnection
import java.net.URL

class ImageLoader(
    private val id: Int,
    private val mode: String,
) : Runnable {
    @Volatile
    var bitmap: Bitmap? = null
    @Volatile
    var isOK: Boolean = true

    override fun run() {
        val connection =
            URL("http://213.189.221.170:8008/1ch?limit=1&lastKnownId=${id-1}")
                .openConnection() as HttpURLConnection
        try {
            connection.doInput = true
            connection.requestMethod = "GET"
            connection.connect()

            val response = connection.inputStream.bufferedReader().readText()
            connection.inputStream.close()
            connection.disconnect()

            var imageLink = ""
            val jsonArray = JSONTokener(response).nextValue() as JSONArray
            for (i in 0 until jsonArray.length()) {
                val dataString = jsonArray.getJSONObject(i).getString("data")
                val dataObject = JSONTokener(dataString).nextValue() as JSONObject
                val imageObject = JSONTokener(
                    dataObject.getString("Image")
                ).nextValue() as JSONObject
                imageLink = imageObject.getString("link")
            }
            val imageGetter = ImageBitmapGetter(imageLink, mode)
            val getImageThread = Thread(imageGetter)
            getImageThread.start()
            getImageThread.join()
            bitmap = imageGetter.bitmap
            getImageThread.interrupt()
        } catch (e: ConnectException) {
            isOK = false
        }
    }

}