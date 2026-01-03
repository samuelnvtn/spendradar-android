package com.pef.mendelu.novotnysamuel.spendradar.di

import com.pef.mendelu.novotnysamuel.spendradar.database.IReceiptsLocalRepository
import com.pef.mendelu.novotnysamuel.spendradar.database.ReceiptsDao
import com.pef.mendelu.novotnysamuel.spendradar.database.ReceiptsLocalRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideRepository(dao: ReceiptsDao): IReceiptsLocalRepository {
        return ReceiptsLocalRepositoryImpl(dao)
    }
}