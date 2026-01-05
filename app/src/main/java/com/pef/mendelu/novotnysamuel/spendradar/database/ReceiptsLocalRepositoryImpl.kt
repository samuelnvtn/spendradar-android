package com.pef.mendelu.novotnysamuel.spendradar.database

import com.pef.mendelu.novotnysamuel.spendradar.model.Receipt
import kotlinx.coroutines.flow.Flow

class ReceiptsLocalRepositoryImpl(private val receiptsDao: ReceiptsDao) : IReceiptsLocalRepository {

    override fun getAllReceipts(): Flow<List<Receipt>> {
        return receiptsDao.getAll()
    }

    override suspend fun getReceiptById(id: Long): Receipt? {
        return receiptsDao.getById(id)
    }

    override suspend fun insertReceipt(receipt: Receipt): Long {
        return receiptsDao.insertReceipt(receipt)
    }

    override suspend fun deleteReceipt(receipt: Receipt) {
        receiptsDao.deleteReceipt(receipt)
    }

    override suspend fun updateReceipt(receipt: Receipt) {
        receiptsDao.updateReceipt(receipt)
    }
}
