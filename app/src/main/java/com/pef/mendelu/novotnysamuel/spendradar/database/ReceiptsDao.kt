package com.pef.mendelu.novotnysamuel.spendradar.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.pef.mendelu.novotnysamuel.spendradar.model.Receipt
import kotlinx.coroutines.flow.Flow

@Dao
interface ReceiptsDao {

    @Insert
    suspend fun insertReceipt(receipt: Receipt): Long

    @Update
    suspend fun updateReceipt(receipt: Receipt)

    @Delete
    suspend fun deleteReceipt(receipt: Receipt)

    @Query("SELECT * FROM receipts ORDER BY timestamp DESC")
    fun getAll(): Flow<List<Receipt>>

    @Query("SELECT * FROM receipts WHERE id = :id")
    suspend fun getById(id: Long): Receipt?
}
