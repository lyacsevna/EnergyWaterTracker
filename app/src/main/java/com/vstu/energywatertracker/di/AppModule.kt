package com.vstu.energywatertracker.di

import android.content.Context
import com.vstu.energywatertracker.data.local.database.AppDatabase
import com.vstu.energywatertracker.data.repository.MeterRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getDatabase(context)
    }

    @Provides
    @Singleton
    fun provideMeterRepository(database: AppDatabase): MeterRepository {
        return MeterRepository(database.meterReadingDao())
    }
}