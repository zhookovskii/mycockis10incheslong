package com.hw.network

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso

class OpenImage : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.image_open)
        val image = findViewById<ImageView>(R.id.image_big)
        val link = intent.getStringExtra("link")
        Picasso.get()
            .load("${ChatApp.BASE_URL}/img/$link")
            .into(image)
    }
}