package hashslingingslasher.andoidhw.messenger

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException

/*
    Class to convert intents to Message objects
 */

class MessageFactory(
    private val text: String?,
    private val uri: Uri?,
    private val from: String,
    private val to: String,
    private val time: Long,
    private val context: Context
) {
    @Volatile
    private var temporaryFile: File? = null

    data class ImageData(
        val json: RequestBody,
        val body: MultipartBody.Part
    )

    fun textMessage(): Message? {
        if (time == -1L) return null
        return Message (
            237,
            from,
            to,
            Data(Text(text!!), null),
            time
        )
    }

    fun imageMessage(): ImageData? {
        temporaryFile = uriToFile(uri!!)
        if (temporaryFile != null) {
            val json =
                "{\"from\":\"$from\",\"to\":\"$to\"}"
            val requestFile = RequestBody
                .create(MediaType.parse("multipart/form-data"), temporaryFile!!)
            val body = MultipartBody.Part
                .createFormData("image", temporaryFile!!.name, requestFile)
            val requestJson = RequestBody
                .create(MediaType.parse("multipart/form-data"), json)
            return ImageData(
                requestJson,
                body
            )
        }
        return null
    }

    fun sweepSweepSweep() = temporaryFile?.delete()

    private fun uriToFile(uri: Uri): File? {
        val bm = uriToBitmap(uri)
        val fileName = "${System.currentTimeMillis()}"
        if (bm != null) {
            return try {
                val file = File(context.cacheDir, fileName)
                file.createNewFile()
                val fileOutputStream = FileOutputStream(file)
                bm.compress(Bitmap.CompressFormat.PNG, 0, fileOutputStream)
                fileOutputStream.apply {
                    flush()
                    close()
                }
                file
            } catch (e: FileNotFoundException) {
                null
            } catch (e: IOException) {
                null
            }
        } else {
            return null
        }
    }

    private fun uriToBitmap(selectedFileUri: Uri): Bitmap? {
        var image: Bitmap? = null
        try {
            val parcelFileDescriptor = context
                .contentResolver.openFileDescriptor(selectedFileUri, "r")
            val fileDescriptor = parcelFileDescriptor!!.fileDescriptor
            image = BitmapFactory.decodeFileDescriptor(fileDescriptor)
            parcelFileDescriptor.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return image
    }

}