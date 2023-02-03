package com.hw.network

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [Entity::class, Channel::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun messageDao(): MessagesDao?

    companion object{

        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase? {
            if (INSTANCE == null) {
                synchronized(this) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                            context.applicationContext,
                            AppDatabase::class.java, "AppDatabase"
                        ).build()
                    }
                }
            }
            return INSTANCE
        }
    }
}
