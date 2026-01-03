package com.pef.mendelu.novotnysamuel.spendradar.di

import com.pef.mendelu.novotnysamuel.spendradar.database.ReceiptsDao
import com.pef.mendelu.novotnysamuel.spendradar.database.ReceiptsDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DaoModule {

    @Provides
    @Singleton
    fun provideDao(database: ReceiptsDatabase): ReceiptsDao {
        return database.receiptsDao()
    }
}