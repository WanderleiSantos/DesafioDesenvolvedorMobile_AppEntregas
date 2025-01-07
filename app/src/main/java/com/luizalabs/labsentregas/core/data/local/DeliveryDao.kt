package com.luizalabs.labsentregas.core.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface DeliveryDao {
    @Insert
    suspend fun insertDelivery(delivery: DeliveryEntity)

    @Query("SELECT * FROM DeliveryEntity")
    suspend fun getDeliveries(): List<DeliveryEntity>

    @Query("SELECT * FROM DeliveryEntity WHERE deliveryId = :deliveryId")
    suspend fun getDeliveryById(deliveryId: Int): DeliveryEntity
}