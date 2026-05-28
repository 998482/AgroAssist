package com.example.agroinnovexa20.di

import android.content.Context
import com.example.agroinnovexa20.data.local.Room.AppDatabase
import com.example.agroinnovexa20.data.local.Room.DatabaseProvider
import com.example.agroinnovexa20.data.local.Room.WeatherDao
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
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return DatabaseProvider.getDatabase(context)
    }

    @Provides
    fun provideWeatherDao(db: AppDatabase): WeatherDao {
        return db.weatherDao()
    }
}