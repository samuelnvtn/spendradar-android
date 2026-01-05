package com.pef.mendelu.novotnysamuel.spendradar.communication

import android.graphics.Bitmap
import com.pef.mendelu.novotnysamuel.spendradar.communication.GeminiParser

interface IGeminiRemoteRepository {
    suspend fun parseReceipt(image: Bitmap): CommunicationResult<GeminiParser.GeminiReceiptResponse>
}
