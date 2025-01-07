package com.luizalabs.labsentregas.di

import com.luizalabs.labsentregas.core.data.repository.DeliveryRepositoryImpl
import com.luizalabs.labsentregas.domain.repository.DeliveryRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindDeliveryRepository(
        deliveryRepositoryImpl: DeliveryRepositoryImpl
    ): DeliveryRepository
}