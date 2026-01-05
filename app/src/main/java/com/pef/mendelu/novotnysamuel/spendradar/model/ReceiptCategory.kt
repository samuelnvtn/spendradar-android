package com.pef.mendelu.novotnysamuel.spendradar.model

enum class ReceiptCategory(val storageValue: String) {
    FOOD_DRINK("FOOD_DRINK"),
    GROCERIES("GROCERIES"),
    PHARMACY("PHARMACY"),
    ELECTRONICS("ELECTRONICS"),
    FASHION("FASHION"),
    OTHER("OTHER");

    companion object {
        fun fromStorage(value: String?): ReceiptCategory {
            return values().firstOrNull { it.storageValue == value } ?: OTHER
        }
    }
}
