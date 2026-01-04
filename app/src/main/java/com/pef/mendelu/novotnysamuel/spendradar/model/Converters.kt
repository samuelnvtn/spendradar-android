package com.pef.mendelu.novotnysamuel.spendradar.model

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.time.Instant

class Converters {
    private val gson = Gson()

    @TypeConverter
    fun fromInstant(value: Instant?): Long? {
        return value?.toEpochMilli()
    }

    @TypeConverter
    fun toInstant(value: Long?): Instant? {
        return value?.let { Instant.ofEpochMilli(it) }
    }

    @TypeConverter
    fun fromReceiptItems(items: List<ReceiptItem>?): String? {
        return items?.let { gson.toJson(it) }
    }

    @TypeConverter
    fun toReceiptItems(value: String?): List<ReceiptItem> {
        if (value.isNullOrBlank()) {
            return emptyList()
        }
        val listType = object : TypeToken<List<ReceiptItem>>() {}.type
        return gson.fromJson(value, listType)
    }
}
