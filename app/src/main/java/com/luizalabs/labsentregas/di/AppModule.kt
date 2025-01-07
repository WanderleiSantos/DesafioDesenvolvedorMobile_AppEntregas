package com.luizalabs.labsentregas.di

import android.app.Application
import androidx.room.Room
import com.luizalabs.labsentregas.core.data.local.DeliveryDataBase
import com.luizalabs.labsentregas.core.data.remote.IBGEApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providesDeliveryDatabase(app: Application) : DeliveryDataBase {
        return Room.databaseBuilder(
            app,
            DeliveryDataBase::class.java,
            "deliverydb.db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://servicodados.ibge.gov.br/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideIBGEApiService(retrofit: Retrofit): IBGEApiService {
        return retrofit.create(IBGEApiService::class.java)
    }
}