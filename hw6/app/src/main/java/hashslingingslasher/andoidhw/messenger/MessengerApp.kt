package hashslingingslasher.andoidhw.messenger

import android.app.Application
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.squareup.picasso.Picasso
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class MessengerApp : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this

        moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        val picasso = Picasso.Builder(applicationContext)
            .build()
        Picasso.setSingletonInstance(picasso)

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

        messageApi =
            retrofit.create(MessageApi::class.java)
    }

    companion object {
        lateinit var instance: MessengerApp
            private set
        lateinit var messageApi: MessageApi
        lateinit var moshi: Moshi
    }
}