package com.luizalabs.labsentregas.core.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface DeliveryDao {
    @Insert
    suspend fun insertDelivery(delivery: DeliveryEntity)

    @Query("SELECT * FROM DeliveryEntity")
    suspend fun getDeliveries(): List<DeliveryEntity>

    @Query("SELECT * FROM DeliveryEntity WHERE deliveryId = :deliveryId")
    suspend fun getDeliveryById(deliveryId: Int): DeliveryEntity

    @Delete
    suspend fun deleteDelivery(delivery: DeliveryEntity)

    @Update
    suspend fun updateDelivery(delivery: DeliveryEntity)
}