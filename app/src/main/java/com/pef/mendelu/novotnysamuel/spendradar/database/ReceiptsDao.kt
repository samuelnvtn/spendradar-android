package com.pef.mendelu.novotnysamuel.spendradar.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.pef.mendelu.novotnysamuel.spendradar.model.Receipt
import com.pef.mendelu.novotnysamuel.spendradar.model.ReceiptItem
import com.pef.mendelu.novotnysamuel.spendradar.model.ReceiptWithItems
import kotlinx.coroutines.flow.Flow

@Dao
interface ReceiptsDao {

    // ZÁKLADNÍ OPERACE PRO ÚČTENKU

    @Insert
    suspend fun insertReceipt(receipt: Receipt): Long

    @Update
    suspend fun updateReceipt(receipt: Receipt)

    @Delete
    suspend fun deleteReceipt(receipt: Receipt)

    // OPERACE PRO POLOŽKY

    @Insert
    suspend fun insertItems(items: List<ReceiptItem>)

    @Query("DELETE FROM receipt_items WHERE receiptId = :receiptId")
    suspend fun deleteItemsForReceipt(receiptId: Long)

    // Vrátí všechno: Účtenku i se seznamem jejích položek
    @Transaction
    @Query("SELECT * FROM receipts ORDER BY timestamp DESC")
    fun getAllWithItems(): Flow<List<ReceiptWithItems>>

    // Detail jedné účtenky
    @Transaction
    @Query("SELECT * FROM receipts WHERE id = :id")
    suspend fun getById(id: Long): ReceiptWithItems?



    // TRANSAKČNÍ LOGIKA (Klíčové pro integritu)
    /**
     * Uloží celou účtenku i s položkami najednou.
     * Pokud se nepovede uložit položky, zruší se i vložení účtenky.
     */
    @Transaction
    suspend fun insertFullReceipt(receipt: Receipt, items: List<ReceiptItem>) {
        val newReceiptId = insertReceipt(receipt)
        val itemsWithCorrectId = items.map { it.copy(receiptId = newReceiptId) }
        insertItems(itemsWithCorrectId)
    }
}