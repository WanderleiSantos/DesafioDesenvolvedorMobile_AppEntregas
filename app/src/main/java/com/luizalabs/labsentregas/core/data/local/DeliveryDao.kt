package com.luizalabs.labsentregas.core.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface DeliveryDao {

    @Upsert
    suspend fun upsertDelivery(delivery: DeliveryEntity)

    @Query("SELECT * FROM DeliveryEntity")
    suspend fun getDeliveries(): List<DeliveryEntity>

    @Query("SELECT * FROM DeliveryEntity WHERE deliveryId = :deliveryId")
    suspend fun getDeliveryById(deliveryId: Int): DeliveryEntity
}