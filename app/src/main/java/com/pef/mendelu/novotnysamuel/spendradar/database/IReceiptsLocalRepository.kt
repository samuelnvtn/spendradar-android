package com.pef.mendelu.novotnysamuel.spendradar.database

import com.pef.mendelu.novotnysamuel.spendradar.model.Receipt
import com.pef.mendelu.novotnysamuel.spendradar.model.ReceiptItem
import com.pef.mendelu.novotnysamuel.spendradar.model.ReceiptWithItems
import kotlinx.coroutines.flow.Flow

interface IReceiptsLocalRepository {
    /**
     * Sleduje všechny účtenky i s jejich položkami.
     * Díky Flow se UI automaticky překreslí při jakékoliv změně v DB.
     */
    fun getAllReceiptsWithItems(): Flow<List<ReceiptWithItems>>

    /**
     * Získá detail jedné konkrétní účtenky.
     */
    suspend fun getReceiptById(id: Long): ReceiptWithItems?

    /**
     * Uloží kompletní nákup (hlavičku i položky) v jedné transakci.
     */
    suspend fun insertFullReceipt(receipt: Receipt, items: List<ReceiptItem>)

    /**
     * Smaže účtenku. Díky nastavení CASCADE v entitě se automaticky smažou i položky.
     */
    suspend fun deleteReceipt(receipt: Receipt)

    /**
     * Aktualizuje údaje o účtence (např. změna názvu obchodu).
     */
    suspend fun updateReceipt(receipt: Receipt)
}