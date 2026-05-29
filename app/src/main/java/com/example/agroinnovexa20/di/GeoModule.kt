package com.example.agroinnovexa20.di



import com.example.agroinnovexa20.data.repository.GeocodingRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Agar tumhare paas already AppModule.kt hai to
 * sirf @Provides wala block usme copy karo — naya file mat banao.
 *
 * Agar naya file chahiye to yeh as-is rakho.
 */
@Module
@InstallIn(SingletonComponent::class)
object GeoModule {

    @Provides
    @Singleton
    fun provideGeocodingRepository(): GeocodingRepository = GeocodingRepository()
}
