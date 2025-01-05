package com.luizalabs.labsentregas.di

import android.app.Application
import androidx.room.Room
import com.luizalabs.labsentregas.core.data.local.DeliveryDataBase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
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
}