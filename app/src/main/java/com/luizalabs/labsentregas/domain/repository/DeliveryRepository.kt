package com.luizalabs.labsentregas.domain.repository


import com.luizalabs.labsentregas.core.data.local.DeliveryEntity

interface DeliveryRepository {
    suspend fun insertDelivery(delivery: DeliveryEntity)
    suspend fun getDeliveries(): List<DeliveryEntity>
    suspend fun getDeliveryById(deliveryId: Int): DeliveryEntity
    suspend fun deleteDelivery(delivery: DeliveryEntity)
    suspend fun updateDelivery(delivery: DeliveryEntity)
}