package com.pef.mendelu.novotnysamuel.spendradar.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.pef.mendelu.novotnysamuel.spendradar.model.Converters
import com.pef.mendelu.novotnysamuel.spendradar.model.Receipt

@Database(entities = [Receipt::class], version = 2, exportSchema = false)
@TypeConverters(Converters::class)
abstract class ReceiptsDatabase : RoomDatabase() {

    abstract fun receiptsDao() : ReceiptsDao

    companion object {
        private var INSTANCE: ReceiptsDatabase? = null
        fun getDatabase(context: Context): ReceiptsDatabase {
            if (INSTANCE == null) {
                synchronized(ReceiptsDatabase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                            context.applicationContext,
                            ReceiptsDatabase::class.java,
                            "receipts_database"
                        ).fallbackToDestructiveMigration().build()
                    }
                }
            }
            return INSTANCE!!
        }
    }

}
