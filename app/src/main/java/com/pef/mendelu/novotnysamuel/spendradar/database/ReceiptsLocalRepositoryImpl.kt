package com.pef.mendelu.novotnysamuel.spendradar.database

import com.pef.mendelu.novotnysamuel.spendradar.model.Receipt
import com.pef.mendelu.novotnysamuel.spendradar.model.ReceiptItem
import com.pef.mendelu.novotnysamuel.spendradar.model.ReceiptWithItems
import kotlinx.coroutines.flow.Flow

class ReceiptsLocalRepositoryImpl(private val receiptsDao: ReceiptsDao) : IReceiptsLocalRepository {

    override fun getAllReceiptsWithItems(): Flow<List<ReceiptWithItems>> {
        return receiptsDao.getAllWithItems()
    }

    override suspend fun getReceiptById(id: Long): ReceiptWithItems? {
        return receiptsDao.getById(id)
    }

    override suspend fun insertFullReceipt(receipt: Receipt, items: List<ReceiptItem>) {
        // Voláme transakční metodu v DAO, která vyřeší IDčka
        receiptsDao.insertFullReceipt(receipt, items)
    }

    override suspend fun deleteReceipt(receipt: Receipt) {
        receiptsDao.deleteReceipt(receipt)
    }

    override suspend fun updateReceipt(receipt: Receipt) {
        receiptsDao.updateReceipt(receipt)
    }
}