package hashslingingslasher.andoidhw.messenger

import androidx.room.*

@Dao
interface MessageDao {
    @Query("SELECT * FROM message_table")
    fun getAll() : List<TableMessage>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertMessages(vararg messages: TableMessage)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun replaceMessages(vararg messages: TableMessage)

    @Query("DELETE FROM message_table")
    fun dementiaTime()

    @Query("DELETE FROM message_table WHERE id = :iD")
    fun deleteFromTable(iD: Int)

    @Query("SELECT * FROM message_table WHERE id = :iD")
    fun getById(iD: Int) : TableMessage?
}