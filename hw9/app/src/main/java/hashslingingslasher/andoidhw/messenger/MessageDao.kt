package hashslingingslasher.andoidhw.messenger

import androidx.room.*

@Dao
interface MessageDao {
    @Query("SELECT * FROM message_table")
    suspend fun getAll() : List<TableMessage>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMessages(vararg messages: TableMessage)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun replaceMessages(vararg messages: TableMessage)

    @Query("DELETE FROM message_table")
    suspend fun dementiaTime()

    @Query("DELETE FROM message_table WHERE id = :iD")
    suspend fun deleteFromTable(iD: Int)

    @Query("SELECT * FROM message_table WHERE id = :iD")
    suspend fun getById(iD: Int) : TableMessage?
}