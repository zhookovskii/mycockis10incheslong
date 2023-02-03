package com.hw.network

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "AppDatabase")
data class Entity(
    @PrimaryKey val id: Int,
    val name: String,
    val to: String,
    val date: Long,
    val isText: Boolean,
    val text: String?,
    val imageLink: String?
)

@Entity(tableName = "Channels")
data class Channel(
    @PrimaryKey val name: String
)