package com.pef.mendelu.novotnysamuel.spendradar.communication

import android.graphics.Bitmap
import com.pef.mendelu.novotnysamuel.spendradar.communication.GeminiParser
import java.io.IOException

class GeminiRemoteRepositoryImpl : IGeminiRemoteRepository {

    override suspend fun parseReceipt(image: Bitmap): CommunicationResult<GeminiParser.GeminiReceiptResponse> {
        return try {
            val result = GeminiParser.parseReceipt(image)
            if (result == null) {
                CommunicationResult.Error(CommunicationError.Remote("Empty response"))
            } else {
                CommunicationResult.Success(result)
            }
        } catch (e: IOException) {
            CommunicationResult.Error(CommunicationError.NoInternet)
        } catch (e: Exception) {
            CommunicationResult.Error(CommunicationError.Unknown(e.message))
        }
    }
}
