package com.hw.network

import android.app.Application
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.squareup.picasso.Picasso
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class ChatApp : Application() {

    override fun onCreate() {
        super.onCreate()
        app = this

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

        messageApi = retrofit.create(Api::class.java)
        db = AppDatabase.getDatabase(this)?.messageDao()!!
    }

    companion object {
        const val MY_NAME = "pulnyasheva"
        const val BASE_URL = "http://213.189.221.170:8008"
        lateinit var app: ChatApp
            private set
        lateinit var messageApi: Api
        lateinit var moshi: Moshi
        lateinit var db: MessagesDao
    }

}