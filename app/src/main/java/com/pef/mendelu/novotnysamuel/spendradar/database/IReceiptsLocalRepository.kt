package com.pef.mendelu.novotnysamuel.spendradar.database

import com.pef.mendelu.novotnysamuel.spendradar.model.Receipt
import kotlinx.coroutines.flow.Flow

interface IReceiptsLocalRepository {
    fun getAllReceipts(): Flow<List<Receipt>>

    suspend fun getReceiptById(id: Long): Receipt?

    suspend fun insertReceipt(receipt: Receipt)

    suspend fun deleteReceipt(receipt: Receipt)

    suspend fun updateReceipt(receipt: Receipt)
}
