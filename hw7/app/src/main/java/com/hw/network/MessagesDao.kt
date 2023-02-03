package com.hw.network

import androidx.room.*

@Dao
interface MessagesDao {

    @Query("SELECT * FROM AppDatabase WHERE id = :idCurrent")
    suspend fun loadAllById(idCurrent: Int): Entity

    @Query("SELECT * FROM AppDatabase WHERE `to` = :channel")
    suspend fun getMessagesFromChannel(channel: String): List<Entity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertChannel(channel: Channel)

    @Query("SELECT * FROM Channels")
    suspend fun getAllChannels(): List<String>

    @Delete
    suspend fun delete(element: Entity)

    @Insert
    suspend fun insertAll(list: List<Entity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(element: Entity)

}